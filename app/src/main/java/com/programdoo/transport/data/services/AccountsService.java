package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.AuthTokensDto;
import com.programdoo.transport.data.models.requests.accounts.TokenRequestModel;
import com.programdoo.transport.data.models.responses.ResponseModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountsService {
    @POST("accounts/generateToken")
    Observable<ResponseModel<String>> generateToken(@Body TokenRequestModel token);
}
