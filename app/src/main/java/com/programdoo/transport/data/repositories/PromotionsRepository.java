package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.models.dtos.promotions.PromotionDto;
import com.programdoo.transport.data.models.requests.promotions.SearchPromotionsParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.PromotionsService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class PromotionsRepository {
    private PromotionsService service;

    private final BehaviorSubject<SearchPromotionsParams> searchParamsRequests
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<PromotionDto>> promotions;

    @Inject
    public PromotionsRepository(
            PromotionsService promotionsService) {
        this.service = promotionsService;

        promotions = RepositoryOperators.createDataStream(
                searchParamsRequests, service::searchPromotions);
    }

    public void searchPromotions(SearchPromotionsParams searchParams) {
        searchParamsRequests.onNext(searchParams);
    }
}
