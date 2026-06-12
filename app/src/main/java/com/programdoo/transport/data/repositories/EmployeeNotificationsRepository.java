package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.employeesNotifications.EmployeeNotificationDto;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.EmployeeNotificationsService;
import com.programdoo.transport.data.services.EmployeesService;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;

public class EmployeeNotificationsRepository {

    private EmployeeNotificationsService service;

    @Getter
    private final Observable<ResponseModelList<EmployeeNotificationDto>> notifications;

    @Getter
    private final Observable<ResponseModelList<EmployeeNotificationDto>> readNotifications;

    private final BehaviorSubject<Integer> expiringNotificationsRequest
            = BehaviorSubject.create();

    private final BehaviorSubject<Integer> readNotificationsRequest
            = BehaviorSubject.create();

    @Inject
    public EmployeeNotificationsRepository(
            EmployeeNotificationsService service,
            AuthEventBus authEvents) {
        this.service = service;

        notifications = RepositoryOperators.createDataStream(
                expiringNotificationsRequest,
                service::getEmployeeNotifications
        );

        readNotifications = RepositoryOperators.createDataStream(
                readNotificationsRequest,
                service::getReadEmployeeNotifications
        );
    }

    public void loadExpiringDocuments(int employeeId) {
        expiringNotificationsRequest.onNext(employeeId);
    }

    public void loadReadDocuments(int employeeId) {
        readNotificationsRequest.onNext(employeeId);
    }

    public Completable markAllAsRead(int employeeId) {
        return service.markAllAsRead(employeeId)
                .flatMapCompletable(result -> {
                    if (result.isValid()) {
                        return Completable.complete();
                    } else {
                        return Completable.error(
                                new RuntimeException(result.getMessage()));
                    }
                });
    }

    public Completable markAsRead(int notificationId) {

        return service.markAsRead(notificationId)
                .flatMapCompletable(result -> {
                    if (result.isValid()) return Completable.complete();
                    else return Completable.error(new RuntimeException(result.getMessage()));
                });
    }
}
