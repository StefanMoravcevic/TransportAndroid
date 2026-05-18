package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;
import com.programdoo.transport.data.models.dtos.appointments.AppointmentRecurrencePatternDto;
import com.programdoo.transport.data.models.dtos.appointments.SaveAppointmentRecurrencePatternRequestModel;
import com.programdoo.transport.data.models.dtos.appointments.SaveAppointmentRequestModel;
import com.programdoo.transport.data.models.dtos.appointments.SaveAppointmentsByPatternRequestModel;
import com.programdoo.transport.data.models.requests.appointments.SearchAppointmentsParams;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AppointmentsService {
    @POST("appointments/search")
    Observable<ResponseModelList<AppointmentDto>> searchAppointments(@Body SearchAppointmentsParams searchParams);
    @POST("appointments")
    Single<ResponseModel<Integer>> saveAppointment(@Body SaveAppointmentRequestModel saveData);
    @GET("appointments/{id}")
    Observable<ResponseModel<AppointmentDto>> getAppointment(@Path("id") int id);
    @DELETE("appointments/delete/{id}/{userId}")
    Single<ResponseModelBase> deleteAppointment(@Path("id") int id, @Path("userId") int userId);

    @GET("appointments/recurrencePatterns/{id}")
    Observable<ResponseModel<AppointmentRecurrencePatternDto>> getRecurrencePattern(@Path("id") int id);
    @POST("appointments/recurrencePatterns")
    Single<ResponseModel<Integer>> saveRecurrencePattern(@Body SaveAppointmentRecurrencePatternRequestModel saveData);
    @POST("appointments/saveByRecurrencePattern")
    Single<ResponseModel<Integer>> saveAppointmentsByRecurrencePattern(@Body SaveAppointmentsByPatternRequestModel saveData);
}
