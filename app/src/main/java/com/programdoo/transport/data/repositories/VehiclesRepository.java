package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.vehicles.VehicleDto;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.data.models.requests.vehicles.SearchVehicleParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.EmployeesService;
import com.programdoo.transport.data.services.VehiclesService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class VehiclesRepository {

    private VehiclesService service;


    private final BehaviorSubject<SearchVehicleParams> searchVehicleRequests
            = BehaviorSubject.create();


    @Getter
    private final Observable<ResponseModelList<VehicleDto>> vehicles;

    @Inject
    public VehiclesRepository(
            VehiclesService service,
            AuthEventBus authEvents) {
        this.service = service;

        vehicles = RepositoryOperators.createDataStream(
                searchVehicleRequests, service::searchVehicles);

    }
    public void searchVehicles(SearchVehicleParams searchParams) {
        searchVehicleRequests.onNext(searchParams);
    }
}
