package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.dtos.vehicleEngagements.VehicleEngagementDto;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.requests.vehicleEngagements.SearchVehicleEngagementsParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface VehicleEngagementsService {
    @POST("vehicleEngagements/searchGrouped")
    Observable<ResponseModelList<VehicleEngagementDto>> searchVehicleEngagements(@Body SearchVehicleEngagementsParams searchParams);
}
