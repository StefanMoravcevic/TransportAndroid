package com.programdoo.transport.data.models.dtos.driverVehicleIssues;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;


@Data
public class SaveDriverVehicleIssueRequestModel implements Serializable {
    @SerializedName("id")
    public int id;

    @SerializedName("employeeId")
    public Integer employeeId;

    @SerializedName("vehicleId")
    public Integer vehicleId;

    @SerializedName("vehicleDefectTypeId")
    public Integer vehicleDefectTypeId;

    @SerializedName("date")
    public LocalDateTime date;
    @SerializedName("description")
    public String description;
}
