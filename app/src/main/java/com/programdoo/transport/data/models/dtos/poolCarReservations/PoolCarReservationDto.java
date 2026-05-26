package com.programdoo.transport.data.models.dtos.poolCarReservations;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class PoolCarReservationDto extends SavePoolCarReservationRequestModel implements Serializable
{
    @SerializedName("vehicle")
    public String vehicle;

    @SerializedName("employee")
    public String employee;

    @SerializedName("user")
    public String user;

    @SerializedName("dateFromFormatted")
    public String dateFromFormatted;

    @SerializedName("dateToFormatted")
    public String dateToFormatted;
}
