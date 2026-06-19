package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.dtos.receipts.ReceiptModel;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReceiptsService {
    @POST("fuelConsumptions/parseSuf")
    Observable<ResponseModelBase> parseSuf(@Body ReceiptModel model);
}
