package com.programdoo.transport.data.models.dtos.travelOrders;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SaveTravelOrderRequestModel implements  Serializable{
    @SerializedName("id")
    protected Integer id;
    @SerializedName("employeeId")
    public Integer employeeId;
    @SerializedName("vehicleId")
    public Integer vehicleId;
    @SerializedName("userId")
    public Integer userId;
    @SerializedName("supplementId")
    public Integer supplementId;
    @SerializedName("engagementId")
    public Integer engagementId;
    @SerializedName("travelOrderStatusId")
    public Integer travelOrderStatusId;
    @SerializedName("date")
    public LocalDateTime date;
    @SerializedName("transportationVehicleId")
    public Integer transportationVehicleId;
    @SerializedName("orgUnitId")
    public Integer orgUnitId;
    @SerializedName("stateId")
    public Integer stateId;
    @SerializedName("returnDate")
    public LocalDateTime returnDate;
    @SerializedName("borderCrossingFromDate")
    public LocalDateTime borderCrossingFromDate;
    @SerializedName("borderCrossingToDate")
    public LocalDateTime borderCrossingToDate;
    @SerializedName("travelGoal")
    public String travelGoal;
    @SerializedName("destination")
    public String destination;
    @SerializedName("dailyAllowance")
    public Double dailyAllowance;
    @SerializedName("travelOrderNumber")
    public String travelOrderNumber;
    @SerializedName("dateForFormat")
    public String dateForFormat;
    @SerializedName("returnDateForFormat")
    public String returnDateForFormat;
    @SerializedName("borderCrossingToDateForFormat")
    public String borderCrossingToDateForFormat;
    @SerializedName("borderCrossingFromDateForFormat")
    public String borderCrossingFromDateForFormat;
    @SerializedName("payedCosts")
    public boolean payedCosts;
    @SerializedName("payDailyAllowance")
    public boolean payDailyAllowance;
    @SerializedName("exchangeRate")
    public Double exchangeRate;
    @SerializedName("calculationDate")
    public LocalDateTime calculationDate;
    @SerializedName("createdDate")
    public LocalDateTime createdDate;
    @SerializedName("dateCalculated")
    public LocalDateTime dateCalculated;
    @SerializedName("isApproved")
    public boolean isApproved;
    @SerializedName("approvedBy")
    public Integer approvedBy;
    @SerializedName("calculatedBy")
    public Integer calculatedBy;
}
