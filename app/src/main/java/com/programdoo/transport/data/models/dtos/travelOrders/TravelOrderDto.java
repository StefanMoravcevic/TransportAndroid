package com.programdoo.transport.data.models.dtos.travelOrders;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;


@Data
public class TravelOrderDto extends SaveTravelOrderRequestModel implements Serializable {


    @SerializedName("employee")
    private String employee;

    @SerializedName("vehicle")
    private String vehicle;

    @SerializedName("state")
    private String state;

    @SerializedName("transportationVehicle")
    private String transportationVehicle;

    @SerializedName("orgUnit")
    private String orgUnit;

    @SerializedName("supplement")
    private String supplement;

    @SerializedName("jobType")
    private String jobType;

    @SerializedName("travelOrderStatus")
    private String travelOrderStatus;

    @SerializedName("dailyAllowanceToShow")
    private Double dailyAllowanceToShow;

    @SerializedName("freePlace")
    private String freePlace;

    @SerializedName("approvedByName")
    private String approvedByName;

    @SerializedName("calculatedByName")
    private String calculatedByName;

    @SerializedName("stateHours")
    private Integer stateHours;

    @SerializedName("itemsAmount")
    private Double itemsAmount;

    @SerializedName("dateFormatted")
    private String dateFormatted;

    @SerializedName("returnDateFormatted")
    private String returnDateFormatted;

    @SerializedName("borderCrossingToDateFormatted")
    private String borderCrossingToDateFormatted;

    @SerializedName("borderCrossingFromDateFormatted")
    private String borderCrossingFromDateFormatted;

}
