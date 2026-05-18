package com.programdoo.transport.data.repositories;

import android.util.Pair;

import com.programdoo.transport.data.models.dtos.masterData.MasterDataDto;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.services.MasterDataService;
import com.programdoo.transport.utils.AuthErrors;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@ActivityRetainedScoped
public class MasterDataRepository {
    private final MasterDataService service;

    private final Map<String, BehaviorSubject<Pair<String, String>>> requestsMap = new HashMap<>();
    private final Map<String, Observable<ResponseModelList<MasterDataDto>>> observablesMap = new HashMap<>();

    @Inject
    public MasterDataRepository(
            MasterDataService masterDataService) {
        this.service = masterDataService;
    }
    public Observable<ResponseModelList<MasterDataDto>> GetSelectOptionsByTable(String tableName, String descriptionExpression) {
        BehaviorSubject<Pair<String, String>> subject = requestsMap
                .computeIfAbsent(tableName, key -> BehaviorSubject.create());
        Observable<ResponseModelList<MasterDataDto>> observable = observablesMap
                .computeIfAbsent(tableName, key ->
                        subject.switchMap(request ->
                                service.getSelectOptionsByTable(request.first, request.second)
                                        .subscribeOn(Schedulers.io())
                                        .onErrorResumeNext(error -> {
                                            if (AuthErrors.isSessionExpired(error)) return Observable.empty();
                                            return Observable.error(error);
                                        }))
                                .replay(1)
                                .refCount());
        subject.onNext(new Pair<>(tableName, descriptionExpression));
        return observable;
    }

//    public Observable<ResponseModelList<MasterDataDto>> GetFilteredSelectOptionsByTable(String tableName, String keyColumnName, int columnValue, String descriptionColumnName) {
//        return service.getFilteredSelectOptionsByTable(tableName, keyColumnName, columnValue, descriptionColumnName);
//    }
}
