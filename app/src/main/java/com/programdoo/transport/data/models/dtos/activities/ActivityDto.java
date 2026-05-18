package com.programdoo.transport.data.models.dtos.activities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class ActivityDto extends SaveActivityRequestModel implements Serializable {
    @SerializedName("traineeName")
    protected String traineeName;
    @SerializedName("activityName")
    protected String activityName;

}
