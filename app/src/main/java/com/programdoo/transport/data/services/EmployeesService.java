package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmployeesService {
    @POST("employees/search")
    Observable<ResponseModelList<EmployeeDto>> searchEmployees(@Body SearchEmployeesParams searchParams);
}
