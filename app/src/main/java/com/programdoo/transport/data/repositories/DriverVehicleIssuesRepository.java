package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.models.dtos.driverVehicleIssues.DriverVehicleIssueDto;
import com.programdoo.transport.data.models.dtos.driverVehicleIssues.SaveDriverVehicleIssueRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.SaveTravelOrderRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.TravelOrderDto;
import com.programdoo.transport.data.models.requests.driverVehicleIssues.SearchDriverVehicleIssuesParams;
import com.programdoo.transport.data.models.requests.travelOrders.SearchTravelOrdersParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.DriverVehicleIssuesService;
import com.programdoo.transport.data.services.TravelOrdersService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class DriverVehicleIssuesRepository {

    public final DriverVehicleIssuesService service;

    private final BehaviorSubject<SearchDriverVehicleIssuesParams> searchDriverVehicleIssuesParams
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<DriverVehicleIssueDto>> driverVehicleIssues;

    private final PublishSubject<Object> refreshDriverVehicleIssueTrigger = PublishSubject.create();

    @Inject
    public DriverVehicleIssuesRepository(
            DriverVehicleIssuesService service) {
        this.service = service;
        driverVehicleIssues  = RepositoryOperators.createRefreshableDataStream(
                searchDriverVehicleIssuesParams, refreshDriverVehicleIssueTrigger, service::searchDriverIssues);
    }

    public void searchDriverVehicleIssues(SearchDriverVehicleIssuesParams searchParams) {
        searchDriverVehicleIssuesParams.onNext(searchParams);
    }

    public Observable<Integer> saveDriverVehicleIssue(SaveDriverVehicleIssueRequestModel saveData) {

        return service.saveDriverVehicleIssue(saveData)
                .flatMap(response -> {

                    if (response.isValid()) {
                        return Observable.just(response.getPayload());
                    } else {
                        return Observable.error(
                                new RuntimeException(response.getMessage())
                        );
                    }
                });
    }


}
