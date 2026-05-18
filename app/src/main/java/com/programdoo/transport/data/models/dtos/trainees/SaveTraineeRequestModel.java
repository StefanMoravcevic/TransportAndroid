package com.programdoo.transport.data.models.dtos.trainees;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SaveTraineeRequestModel implements Serializable {
    @SerializedName("id")
    protected int id;
    @SerializedName("name")
    protected String name;
    @SerializedName("surname")
    protected String surname;
    @SerializedName("nickname")
    protected String nickname;
    @SerializedName("email")
    protected String email;
    @SerializedName("phoneNumber")
    protected String phoneNumber;
    @SerializedName("birthdate")
    protected LocalDateTime birthdate;
    @SerializedName("genderId")
    protected Integer genderId;
    @SerializedName("trialDate")
    protected LocalDateTime trialDate;
    @SerializedName("trialTrainerId")
    protected Integer trialTrainerId;
    @SerializedName("previousFitnessExperience")
    protected String previousFitnessExperience;
    @SerializedName("foundUsMethodId")
    protected Integer foundUsMethodId;
    @SerializedName("orgUnitId")
    protected Integer orgUnitId;
    @SerializedName("allOrgUnits")
    protected boolean allOrgUnits;
    @SerializedName("currentTrainerId")
    protected Integer currentTrainerId;
    @SerializedName("active")
    protected boolean active;
    @SerializedName("terminationReason")
    protected String terminationReason;
    @SerializedName("promotionId")
    protected Integer promotionId;
    @SerializedName("inTrialPeriod")
    protected boolean inTrialPeriod;
    @SerializedName("profilePhotoId")
    protected Integer profilePhotoId;
    @SerializedName("receiveCalendarEvents")
    protected boolean receiveCalendarEvents;
    @SerializedName("receiveEmailNotifications")
    protected boolean receiveEmailNotifications;
    @SerializedName("userCulture")
    protected String userCulture;
    @SerializedName("limitationsIds")
    protected List<Integer> limitationsIds = new ArrayList<>();
    @SerializedName("goalsIds")
    protected List<Integer> goalsIds = new ArrayList<>();
    @SerializedName("traineeOrgUnitsIds")
    protected List<Integer> traineeOrgUnitsIds = new ArrayList<>();
}