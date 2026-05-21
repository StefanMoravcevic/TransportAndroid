package com.programdoo.transport.data.models.dtos.scannedpackages;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SaveScannedPackagesRequestModel implements Serializable {
    @SerializedName("id")
    public int id;
    @SerializedName("packageNo")
    public String packageNo;
    @SerializedName("userId")
    public int userId;
    @SerializedName("scannedDateTime")
    public LocalDateTime scannedDateTime;
    @SerializedName("longitude")
    public Double longitude;
    @SerializedName("latitude")
    public Double latitude;
}
