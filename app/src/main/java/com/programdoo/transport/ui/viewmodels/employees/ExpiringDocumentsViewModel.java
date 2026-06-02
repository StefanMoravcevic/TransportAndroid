package com.programdoo.transport.ui.viewmodels.employees;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.programdoo.transport.data.models.dtos.employees.EmployeeDocumentAlertDto;
import com.programdoo.transport.data.models.dtos.employeesNotifications.EmployeeNotificationDto;
import com.programdoo.transport.data.repositories.EmployeeNotificationsRepository;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;

@HiltViewModel
public class ExpiringDocumentsViewModel extends BaseViewModel {

    private final EmployeesRepository employeesRepository;
    private final EmployeeNotificationsRepository employeeNotificationsRepository;

    private final MutableLiveData<List<EmployeeDocumentAlertDto>> expiringDocuments =
            new MutableLiveData<>();

    private Disposable disposable;

    public LiveData<List<EmployeeDocumentAlertDto>> getExpiringDocuments() {
        return expiringDocuments;
    }

    private final MutableLiveData<List<EmployeeNotificationDto>> expiringDocumentsNotifications =
            new MutableLiveData<>();

    public LiveData<List<EmployeeNotificationDto>> getExpiringDocumentsNotifications() {
        return expiringDocumentsNotifications;
    }

    @Getter @Setter
    private final MutableLiveData<Integer> unreadCount = new MutableLiveData<>();


    public LiveData<Integer> getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int count) {
        unreadCount.setValue(count); // ili postValue ako si u background thread-u
    }

    @Inject
    public ExpiringDocumentsViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            com.programdoo.transport.data.eventbus.AuthEventBus authEvent,
            EmployeesRepository employeesRepository,
            EmployeeNotificationsRepository employeeNotificationsRepository) {

        super(preferences, session, authEvent);

        this.employeesRepository = employeesRepository;
        this.employeeNotificationsRepository = employeeNotificationsRepository;
    }

    public void loadExpiringDocuments(int employeeId) {

        employeesRepository.loadExpiringDocuments(employeeId);

        disposable = employeesRepository.getExpiringDocuments()
                .subscribe(
                        response -> {
                            if (response != null && response.getPayload() != null) {
                                expiringDocuments.postValue(response.getPayload());
                            } else {
                                expiringDocuments.postValue(Collections.emptyList());
                            }
                        },
                        throwable -> {
                            expiringDocuments.postValue(Collections.emptyList());
                        }
                );
    }

    public void loadExpiringNotificationDocuments(int employeeId) {

        employeeNotificationsRepository.loadExpiringDocuments(employeeId);

        disposable = employeeNotificationsRepository.getNotifications()
                .subscribe(
                        response -> {
                            if (response != null && response.getPayload() != null) {

                                List<EmployeeNotificationDto> notifications = response.getPayload();

                                expiringDocumentsNotifications.postValue(notifications);

                                int unread = 0;

                                for (EmployeeNotificationDto notification : notifications) {
                                    if (!notification.isRead()) unread++;
                                }

                                unreadCount.postValue(unread);

                            } else {
                                expiringDocumentsNotifications.postValue(Collections.emptyList());
                                unreadCount.postValue(0);
                            }
                        },
                        throwable -> {
                            expiringDocumentsNotifications.postValue(Collections.emptyList());
                            unreadCount.postValue(0);
                        }
                );
    }

    public void markAllNotificationsAsRead(int employeeId) {

        disposable = employeeNotificationsRepository
                .markAllAsRead(employeeId)
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            // SUCCESS → reload sa servera (ispravno stanje)
                            loadExpiringNotificationDocuments(employeeId);
                        },
                        throwable -> {
                            // opcionalno: log
                        }
                );
    }

    public void markNotificationAsRead(int notificationId, int employeeId) {

        disposable = employeeNotificationsRepository
                .markAsRead(notificationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            loadExpiringNotificationDocuments(employeeId);
                        },
                        Throwable::printStackTrace
                );
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public int getLoggedEmployeeId() {
        return session.getEntityId();
    }
}