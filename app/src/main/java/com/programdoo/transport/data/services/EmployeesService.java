package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.employees.EmployeeDocumentAlertDto;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EmployeesService {
    @POST("employees/search")
    Observable<ResponseModelList<EmployeeDto>> searchEmployees(@Body SearchEmployeesParams searchParams);

    @GET("employees/getExpiringDocuments/{employeeId}")
    Observable<ResponseModelList<EmployeeDocumentAlertDto>> getEmployeeExpiringDocuments(
            @Path("employeeId") int employeeId
    );
}
