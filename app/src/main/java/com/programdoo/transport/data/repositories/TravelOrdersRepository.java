package com.programdoo.transport.data.repositories;

import android.util.Log;

import com.google.gson.Gson;
import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.SaveTravelOrderRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.TravelOrderDto;
import com.programdoo.transport.data.models.dtos.vehicleEngagements.VehicleEngagementDto;
import com.programdoo.transport.data.models.requests.travelOrders.SearchTravelOrdersParams;
import com.programdoo.transport.data.models.requests.vehicleEngagements.SearchVehicleEngagementsParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.TravelOrdersService;
import com.programdoo.transport.data.services.VehicleEngagementsService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class TravelOrdersRepository {
    public final TravelOrdersService service;

    private final BehaviorSubject<SearchTravelOrdersParams> searchTravelOrdersParams
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<TravelOrderDto>> travelOrders;

    private final PublishSubject<Object> refreshTravelOrderTrigger = PublishSubject.create();

    @Inject
    public TravelOrdersRepository(
            TravelOrdersService service) {
        this.service = service;
        travelOrders  = RepositoryOperators.createRefreshableDataStream(
                searchTravelOrdersParams, refreshTravelOrderTrigger, service::searchTravelOrders);
    }

    public void searchTravelOrders(SearchTravelOrdersParams searchParams) {
        Log.d("TRAVEL_DEBUG", "SEND PARAMS: " + new Gson().toJson(searchParams));
        searchTravelOrdersParams.onNext(searchParams);
    }

    public void refreshTravelOrders() {
        this.refreshTravelOrderTrigger.onNext(new Object());
    }

    public Completable saveTravelOrder(SaveTravelOrderRequestModel saveData) {
        return service.saveTravelOrder(saveData)
                .flatMapCompletable(result -> {
                    if (result.isValid()) return Completable.complete();
                    else return Completable.error(new RuntimeException(result.getMessage()));
                });
    }
}
