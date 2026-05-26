package com.programdoo.transport.data.models.dtos.poolCarReservations;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SavePoolCarReservationRequestModel implements Serializable{
    @SerializedName("id")
    public int id;
    @SerializedName("employeeId")
    public int employeeId;
    @SerializedName("vehicleId")
    public int vehicleId;
    @SerializedName("dateFrom")
    public LocalDateTime dateFrom;
    @SerializedName("dateTo")
    public LocalDateTime dateTo;
    @SerializedName("createdBy")
    public int createdBy;
    @SerializedName("isDivorced")
    public boolean isDivorced;
    @SerializedName("note")
    public String note;
    @SerializedName("isDeleted")
    public boolean isDeleted;
    @SerializedName("deletedDate")
    public LocalDateTime deletedDate;
    @SerializedName("deletedBy")
    public Integer deletedBy;
}
