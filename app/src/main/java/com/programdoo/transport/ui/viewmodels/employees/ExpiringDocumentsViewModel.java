package com.programdoo.transport.ui.viewmodels.employees;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.programdoo.transport.data.models.dtos.employees.EmployeeDocumentAlertDto;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class ExpiringDocumentsViewModel extends BaseViewModel {

    private final EmployeesRepository employeesRepository;

    private final MutableLiveData<List<EmployeeDocumentAlertDto>> expiringDocuments =
            new MutableLiveData<>();

    private Disposable disposable;

    public LiveData<List<EmployeeDocumentAlertDto>> getExpiringDocuments() {
        return expiringDocuments;
    }

    @Inject
    public ExpiringDocumentsViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            com.programdoo.transport.data.eventbus.AuthEventBus authEvent,
            EmployeesRepository employeesRepository) {

        super(preferences, session, authEvent);

        this.employeesRepository = employeesRepository;
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