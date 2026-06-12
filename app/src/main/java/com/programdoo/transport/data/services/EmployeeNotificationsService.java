package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.employees.EmployeeDocumentAlertDto;
import com.programdoo.transport.data.models.dtos.employeesNotifications.EmployeeNotificationDto;
import com.programdoo.transport.data.models.dtos.memberships.SaveMembershipRequestModel;
import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EmployeeNotificationsService {
    @GET("employeeNotifications/getNotifications/{employeeId}")
    Observable<ResponseModelList<EmployeeNotificationDto>> getEmployeeNotifications(
            @Path("employeeId") int employeeId
    );

    @GET("employeeNotifications/getReadNotifications/{employeeId}")
    Observable<ResponseModelList<EmployeeNotificationDto>> getReadEmployeeNotifications(
            @Path("employeeId") int employeeId
    );

    @POST("employeeNotifications/markAllAsRead/{employeeId}")
    Observable<ResponseModelBase> markAllAsRead(@Path("employeeId") int employeeId);

    @POST("employeeNotifications/markAsRead/{notificationId}")
    Observable<ResponseModelBase> markAsRead(@Path("notificationId") int notificationId);
}
