package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;
import com.programdoo.transport.data.models.dtos.memberships.MembershipDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.dtos.scannedpackages.SaveScannedPackagesRequestModel;
import com.programdoo.transport.data.models.requests.appointments.SearchAppointmentsParams;
import com.programdoo.transport.data.models.requests.memberships.SearchMembershipsParams;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.PoolCarReservationsService;
import com.programdoo.transport.data.services.ScannedPackagesService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class PoolCarReservationRepository {

    private final PoolCarReservationsService service;

    private final BehaviorSubject<SearchPoolCarReservationParams> searchPoolCarReservationParams
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<PoolCarReservationDto>> poolCarReservations;

    private final PublishSubject<Object> refreshPoolCarReservationTrigger = PublishSubject.create();
    @Inject
    public PoolCarReservationRepository(
            PoolCarReservationsService service) {
        this.service = service;
        poolCarReservations  = RepositoryOperators.createRefreshableDataStream(
                searchPoolCarReservationParams, refreshPoolCarReservationTrigger, service::searchPoolCarReservations);
    }

    public void searchPoolCarReservations(SearchPoolCarReservationParams searchParams) {
        searchPoolCarReservationParams.onNext(searchParams);
    }

    public void refreshPoolCarReservations() {
        this.refreshPoolCarReservationTrigger.onNext(new Object());
    }

    public Completable savePoolCarReservations(SavePoolCarReservationRequestModel saveData) {
        return service.savePoolCarReservation(saveData)
                .flatMapCompletable(result -> {
                    if (result.isValid()) return Completable.complete();
                    else return Completable.error(new RuntimeException(result.getMessage()));
                });
    }
}
