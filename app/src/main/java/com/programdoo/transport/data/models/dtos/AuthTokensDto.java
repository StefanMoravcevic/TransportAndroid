package com.programdoo.transport.data.models.dtos;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class AuthTokensDto {
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("refreshToken")
    private String refreshToken;
}
