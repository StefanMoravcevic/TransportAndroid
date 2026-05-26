package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.memberships.MembershipDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.dtos.scannedpackages.SaveScannedPackagesRequestModel;
import com.programdoo.transport.data.models.requests.memberships.SearchMembershipsParams;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PoolCarReservationsService {

    @POST("poolCarReservations/search")
    Observable<ResponseModelList<PoolCarReservationDto>> searchPoolCarReservations(@Body SearchPoolCarReservationParams searchParams);

    @POST("poolCarReservations")
    Observable<ResponseModel<Integer>> savePoolCarReservation(@Body SavePoolCarReservationRequestModel model);

}
