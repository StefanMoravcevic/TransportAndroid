package com.programdoo.transport.data.models.dtos.appointments;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AppointmentDto extends SaveAppointmentRequestModel
    implements Serializable {
    @SerializedName("sessionsLeftAfter")
    protected int sessionsLeftAfter;
    @SerializedName("membershipExpiryDate")
    protected LocalDateTime membershipExpiryDate;
    @SerializedName("trainerName")
    protected String trainerName;
    @SerializedName("traineeName")
    protected String traineeName;
    @SerializedName("traineeEmail")
    protected String traineeEmail;
    @SerializedName("orgUnitName")
    protected String orgUnitName;
    @SerializedName("address")
    protected String address;
    /* ISO format se koristi u javascriptu na web stranici, ovde je vrv beskoristan*/
    @SerializedName("isoDate")
    protected String isoDate;
    @SerializedName("isoStartTime")
    protected String isoStartTime;
    @SerializedName("isoEndTime")
    protected String isoEndTime;
    @SerializedName("dateFmt")
    protected String dateFmt;
    @SerializedName("startTimeFmt")
    protected String startTimeFmt;
    @SerializedName("endTimeFmt")
    protected String endTimeFmt;
    @SerializedName("eventTitle")
    protected String eventTitle;
    @SerializedName("inTrialPeriod")
    protected boolean inTrialPeriod;
    @SerializedName("valid")
    protected boolean valid;
    @SerializedName("finishedStr")
    protected String finishedStr;
}
