package com.programdoo.transport.data.models.dtos.vehicleEngagements;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VehicleEngagementDto extends SaveVehicleEngagementRequestModel{
    @SerializedName("vehicle")
    private String vehicle;

    @SerializedName("employee")
    private String employee;

    @SerializedName("route")
    private String route;

    @SerializedName("fuel")
    private String fuel;

    @SerializedName("driversMobilePhone")
    private String driversMobilePhone;

    @SerializedName("unassignedByName")
    private String unassignedByName;

    @SerializedName("email")
    private String email;

    @SerializedName("firstEngagement")
    private LocalDateTime firstEngagement;

    @SerializedName("lastDisengagement")
    private LocalDateTime lastDisengagement;

    @SerializedName("totalHours")
    private int totalHours;

    @SerializedName("totalMinutes")
    private int totalMinutes;

    @SerializedName("ordinaryNightHours")
    private int ordinaryNightHours;

    @SerializedName("ordinaryNightMinutes")
    private int ordinaryNightMinutes;

    @SerializedName("specificNightHours")
    private int specificNightHours;

    @SerializedName("specificNightMinutes")
    private int specificNightMinutes;

    @SerializedName("overtimeHours")
    private int overtimeHours;

    @SerializedName("overtimeMinutes")
    private int overtimeMinutes;

    @SerializedName("milleageForDay")
    private String milleageForDay;

    @SerializedName("specialRoute")
    private String specialRoute;

    @SerializedName("orgUnit")
    private String orgUnit;

    @SerializedName("indebtednessDateOnly")
    private String indebtednessDateOnly;

    @SerializedName("indebtednessTimeOnly")
    private String indebtednessTimeOnly;

    @SerializedName("divorceDateOnly")
    private String divorceDateOnly;

    @SerializedName("divorceTimeOnly")
    private String divorceTimeOnly;

    @SerializedName("indebtednessDateFormatted")
    private String indebtednessDateFormatted;

    @SerializedName("divorceDateFormatted")
    private String divorceDateFormatted;

    @SerializedName("indebtednessMilleageFormatted")
    private String indebtednessMilleageFormatted;

    @SerializedName("divorceMilleageFormatted")
    private String divorceMilleageFormatted;
}
