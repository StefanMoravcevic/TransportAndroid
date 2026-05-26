package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.dtos.vehicles.VehicleDto;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.requests.vehicles.SearchVehicleParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface VehiclesService {
    @POST("vehicles/search")
    Observable<ResponseModelList<VehicleDto>> searchVehicles(@Body SearchVehicleParams searchParams);
}
