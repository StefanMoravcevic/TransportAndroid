package com.programdoo.transport.data.models.dtos.driverVehicleIssues;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;


@Data
public class DriverVehicleIssueDto extends SaveDriverVehicleIssueRequestModel implements Serializable {
    @SerializedName("employee")
    public String employee;

    @SerializedName("vehicle")
    public String vehicle;

    @SerializedName("vehicleDefectType")
    public String vehicleDefectType;

    @SerializedName("dateFormatted")
    public String dateFormatted;
}
