package com.programdoo.transport.data.models.dtos.travelOrders;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SaveTravelOrderRequestModel {
    @SerializedName("id")
    protected int id;
    @SerializedName("EmployeeId")
    public int EmployeeId;
    @SerializedName("VehicleId")
    public int VehicleId;
    @SerializedName("SupplementId")
    public int SupplementId;
    @SerializedName("EngagementId")
    public int EngagementId;
    @SerializedName("TravelOrderStatusId")
    public int TravelOrderStatusId;
    @SerializedName("Date")
    public LocalDateTime Date;
    @SerializedName("TransportationVehicleId")
    public int TransportationVehicleId;
    @SerializedName("OrgUnitId")
    public int OrgUnitId;
    @SerializedName("StateId")
    public int StateId;
    @SerializedName("ReturnDate")
    public LocalDateTime ReturnDate;
    @SerializedName("BorderCrossingFromDate")
    public LocalDateTime BorderCrossingFromDate;
    @SerializedName("BorderCrossingToDate")
    public LocalDateTime BorderCrossingToDate;
    @SerializedName("TravelGoal")
    public String TravelGoal;
    @SerializedName("Destination")
    public String Destination;
    @SerializedName("DailyAllowance")
    public Float DailyAllowance;
    @SerializedName("TravelOrderNumber")
    public String TravelOrderNumber;
    @SerializedName("DateForFormat")
    public String DateForFormat;
    @SerializedName("ReturnDateForFormat")
    public String ReturnDateForFormat;
    @SerializedName("BorderCrossingToDateForFormat")
    public String BorderCrossingToDateForFormat;
    @SerializedName("BorderCrossingToDateForFormat")
    public String BorderCrossingFromDateForFormat;
}
