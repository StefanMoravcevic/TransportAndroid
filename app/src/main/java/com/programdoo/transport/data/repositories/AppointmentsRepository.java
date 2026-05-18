package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;
import com.programdoo.transport.data.models.dtos.appointments.AppointmentRecurrencePatternDto;
import com.programdoo.transport.data.models.dtos.appointments.SaveAppointmentRecurrencePatternRequestModel;
import com.programdoo.transport.data.models.dtos.appointments.SaveAppointmentRequestModel;
import com.programdoo.transport.data.models.dtos.appointments.SaveAppointmentsByPatternRequestModel;
import com.programdoo.transport.data.models.requests.appointments.SearchAppointmentsParams;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.AppointmentsService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class AppointmentsRepository {
    private final AppointmentsService service;

    private final BehaviorSubject<SearchAppointmentsParams> searchAppointmentsRequests
            = BehaviorSubject.create();
    private final PublishSubject<Object> refreshAppointmentsTrigger = PublishSubject.create();
    private final BehaviorSubject<Integer> getAppointmentRequests
            = BehaviorSubject.create();
    private final BehaviorSubject<Integer> getRecurrenceRequests
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<AppointmentDto>> appointments;
    @Getter
    private final Observable<ResponseModel<AppointmentDto>> appointment;
    @Getter
    private final Observable<ResponseModel<AppointmentRecurrencePatternDto>> recurrence;
    @Getter
    private final Observable<Object> refreshAppointmentsCompleted;

    @Inject
    public AppointmentsRepository(
            AppointmentsService service) {
        this.service = service;

        appointments = RepositoryOperators.createRefreshableDataStream(
                searchAppointmentsRequests, refreshAppointmentsTrigger, service::searchAppointments);
        appointment = RepositoryOperators.createDataStream(
                getAppointmentRequests, service::getAppointment);
        recurrence = RepositoryOperators.createDataStream(
                getRecurrenceRequests, service::getRecurrencePattern);
        refreshAppointmentsCompleted = RepositoryOperators.createRefreshCompletedStream(
                refreshAppointmentsTrigger, appointments);
    }

    // commands
    public Single<Integer> saveAppointment(SaveAppointmentRequestModel saveData) {
        return service.saveAppointment(saveData)
                .subscribeOn(Schedulers.io())
                .flatMap(result -> {
                    if (result.isValid()) return Single.just(result.getPayload());
                    else return Single.error(new RuntimeException(result.getMessage()));
                })
                .doOnSuccess(id -> refreshAppointments());
    }
    public Completable deleteAppointment(int id, int userId) {
        return service.deleteAppointment(id, userId)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(result -> {
                    if (result.isValid()) return Completable.complete();
                    else return Completable.error(new RuntimeException(result.getMessage()));
                })
                .doOnComplete(this::refreshAppointments);
    }
    public Single<Integer> saveRecurrencePattern(SaveAppointmentRecurrencePatternRequestModel saveData) {
        return service.saveRecurrencePattern(saveData)
                .subscribeOn(Schedulers.io())
                .flatMap(result -> {
                    if (result.isValid()) return Single.just(result.getPayload());
                    else return Single.error(new RuntimeException(result.getMessage()));
                });
    }
    public Single<Integer> saveAppointmentsByRecurrencePattern(SaveAppointmentsByPatternRequestModel saveData) {
        return service.saveAppointmentsByRecurrencePattern(saveData)
                .subscribeOn(Schedulers.io())
                .flatMap(result -> {
                    if (result.isValid()) return Single.just(result.getPayload());
                    else return Single.error(new RuntimeException(result.getMessage()));
                });
    }

    // intents
    public void searchAppointments(SearchAppointmentsParams searchParams) {
        searchAppointmentsRequests.onNext(searchParams);
    }
    public void getAppointment(int id) {
        getAppointmentRequests.onNext(id);
    }
    public void getRecurrencePattern(int id) {
        getRecurrenceRequests.onNext(id);
    }

    public void refreshAppointments() {
        refreshAppointmentsTrigger.onNext(new Object());
    }
}
