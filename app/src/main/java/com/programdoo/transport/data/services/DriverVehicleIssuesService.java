package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.driverVehicleIssues.DriverVehicleIssueDto;
import com.programdoo.transport.data.models.dtos.driverVehicleIssues.SaveDriverVehicleIssueRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.SaveTravelOrderRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.TravelOrderDto;
import com.programdoo.transport.data.models.requests.driverVehicleIssues.SearchDriverVehicleIssuesParams;
import com.programdoo.transport.data.models.requests.travelOrders.SearchTravelOrdersParams;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DriverVehicleIssuesService {

    @POST("driverVehicleIssues/search")
    Observable<ResponseModelList<DriverVehicleIssueDto>> searchDriverIssues(@Body SearchDriverVehicleIssuesParams searchParams);

    @POST("driverVehicleIssues")
    Observable<ResponseModel<Integer>> saveDriverVehicleIssue(@Body SaveDriverVehicleIssueRequestModel requestModel);
}
