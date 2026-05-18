package com.programdoo.transport.data.models.dtos;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class SaveRefreshTokenRequestModel {
    @SerializedName("id")
    public Integer id;
    @SerializedName("token")
    public String token;
    @SerializedName("userId")
    public Integer userId;
    @SerializedName("expirationDate")
    public LocalDateTime expirationDate;
    @SerializedName("deviceId")
    public Integer deviceId;
}
