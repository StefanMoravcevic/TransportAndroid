package com.programdoo.transport.data.models.dtos.activities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

@Data
public class SaveActivityRequestModel implements Serializable {
    @SerializedName("id")
    protected int id;
    @SerializedName("trainerComment")
    protected String trainerComment;
    @SerializedName("date")
    protected LocalDateTime date;
    @SerializedName("activityTypeId")
    protected Integer activityTypeId;
    @SerializedName("traineeId")
    protected Integer traineeId;
    @SerializedName("time")
    protected LocalTime time;
    @SerializedName("activityDescription")
    protected String activityDescription;
    @SerializedName("rating")
    protected Integer rating;

}