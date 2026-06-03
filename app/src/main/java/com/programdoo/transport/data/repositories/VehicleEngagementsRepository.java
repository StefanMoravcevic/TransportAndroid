package com.programdoo.transport.data.repositories;


import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.dtos.vehicleEngagements.VehicleEngagementDto;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.requests.vehicleEngagements.SearchVehicleEngagementsParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.PoolCarReservationsService;
import com.programdoo.transport.data.services.VehicleEngagementsService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class VehicleEngagementsRepository {

    private final VehicleEngagementsService service;

    private final BehaviorSubject<SearchVehicleEngagementsParams> searchVehicleEngagementsParams
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<VehicleEngagementDto>> vehicleEngagements;

    private final PublishSubject<Object> refreshVehicleEngagementsTrigger = PublishSubject.create();

    @Inject
    public VehicleEngagementsRepository(
            VehicleEngagementsService service) {
        this.service = service;
        vehicleEngagements  = RepositoryOperators.createRefreshableDataStream(
                searchVehicleEngagementsParams, refreshVehicleEngagementsTrigger, service::searchVehicleEngagements);
    }

    public void searchVehicleEngagements(SearchVehicleEngagementsParams searchParams) {
        searchVehicleEngagementsParams.onNext(searchParams);
    }

    public void refreshVehicleEngagements() {
        this.refreshVehicleEngagementsTrigger.onNext(new Object());
    }

}
