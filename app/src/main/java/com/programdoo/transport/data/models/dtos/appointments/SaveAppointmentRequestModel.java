package com.programdoo.transport.data.models.dtos.appointments;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

@Data
public class SaveAppointmentRequestModel implements Serializable {
    @SerializedName("id")
    protected int id;
    @SerializedName("recurrencePatternId")
    protected Integer recurrencePatternId;
    @SerializedName("exceptionId")
    protected Integer exceptionId;
    @SerializedName("requestId")
    protected Integer requestId;
    @SerializedName("trainerId")
    protected int trainerId;
    @SerializedName("traineeId")
    protected int traineeId;
    @SerializedName("membershipId")
    protected Integer membershipId;
    @SerializedName("orgUnitId")
    protected int orgUnitId;
    @SerializedName("date")
    protected LocalDateTime date;
    @SerializedName("startTime")
    protected LocalTime startTime;
    @SerializedName("endTime")
    protected LocalTime endTime;
    @SerializedName("sessionDescription")
    protected String sessionDescription;
    @SerializedName("cancelled")
    protected boolean cancelled;
    @SerializedName("finished")
    protected boolean finished;
    @SerializedName("createdAt")
    protected LocalDateTime createdAt;
}
