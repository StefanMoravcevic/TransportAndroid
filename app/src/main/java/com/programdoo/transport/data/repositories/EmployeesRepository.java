package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDocumentAlertDto;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.EmployeesService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class EmployeesRepository {
    private EmployeesService service;

    private final BehaviorSubject<SearchEmployeesParams> searchEmployeesRequests
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<EmployeeDto>> employees;

    private final BehaviorSubject<Integer> expiringDocumentsRequests
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<EmployeeDocumentAlertDto>> expiringDocuments;

    @Inject
    public EmployeesRepository(
            EmployeesService service,
            AuthEventBus authEvents) {
        this.service = service;

        employees = RepositoryOperators.createDataStream(
                searchEmployeesRequests, service::searchEmployees);

        expiringDocuments = RepositoryOperators.createDataStream(
                expiringDocumentsRequests,
                service::getEmployeeExpiringDocuments
        );
    }

    public void searchEmployees(SearchEmployeesParams searchParams) {
        searchEmployeesRequests.onNext(searchParams);
    }

    public void loadExpiringDocuments(int employeeId) {
        expiringDocumentsRequests.onNext(employeeId);
    }
}
