package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.travelOrders.SaveTravelOrderRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.TravelOrderDto;
import com.programdoo.transport.data.models.dtos.vehicleEngagements.VehicleEngagementDto;
import com.programdoo.transport.data.models.requests.travelOrders.SearchTravelOrdersParams;
import com.programdoo.transport.data.models.requests.vehicleEngagements.SearchVehicleEngagementsParams;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TravelOrdersService {
    @POST("travelOrders/search")
    Observable<ResponseModelList<TravelOrderDto>> searchTravelOrders(@Body SearchTravelOrdersParams searchParams);

    @POST("travelOrders")
    Observable<ResponseModel<Integer>> saveTravelOrder(@Body SaveTravelOrderRequestModel requestModel);
}
