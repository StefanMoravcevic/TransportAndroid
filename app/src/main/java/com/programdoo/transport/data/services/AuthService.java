package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.AuthTokensDto;
import com.programdoo.transport.data.models.dtos.SaveRefreshTokenRequestModel;
import com.programdoo.transport.data.models.responses.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("accounts/loginWithRefreshToken")
    Call<ResponseModel<AuthTokensDto>> loginWithRefreshToken(@Body SaveRefreshTokenRequestModel model);
}
