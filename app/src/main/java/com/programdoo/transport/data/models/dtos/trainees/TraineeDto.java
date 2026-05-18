package com.programdoo.transport.data.models.dtos.trainees;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TraineeDto extends SaveTraineeRequestModel implements Serializable {
    @SerializedName("birthdateFmt")
    protected String birthdateFmt;
    @SerializedName("gender")
    protected String gender;
    @SerializedName("currentTrainerName")
    protected String currentTrainerName;
    @SerializedName("currentTrainerNameShort")
    protected String currentTrainerNameShort;
    @SerializedName("trialTrainerName")
    protected String trialTrainerName;
    @SerializedName("trialTrainerNameShort")
    protected String trialTrainerNameShort;
    @SerializedName("orgUnitName")
    protected String orgUnitName;
    @SerializedName("lastSessionDate")
    protected LocalDateTime lastSessionDate;
    @SerializedName("nextSessionDate")
    protected LocalDateTime nextSessionDate;
    @SerializedName("membershipName")
    protected String membershipName;
    @SerializedName("membershipExpiryDate")
    protected LocalDateTime membershipExpiryDate;
    @SerializedName("lastPurchaseDate")
    protected LocalDateTime lastPurchaseDate;
    @SerializedName("lastMembershipExpiryDate")
    protected LocalDateTime lastMembershipExpiryDate;
    @SerializedName("sessionsLeft")
    protected Integer sessionsLeft;
    @SerializedName("promotionName")
    protected String promotionName;
    @SerializedName("upcomingAppointmentsNo")
    protected Integer upcomingAppointmentsNo;
    @SerializedName("totalSessions")
    protected Integer totalSessions;
    @SerializedName("numberOfInvalidSessions")
    protected int numberOfInvalidSessions;
    @SerializedName("height")
    protected Integer height;
    @SerializedName("limitations")
    protected List<String> limitations = new ArrayList<>();
    @SerializedName("goals")
    protected List<String> goals = new ArrayList<>();
    @SerializedName("foundUsMethod")
    protected String foundUsMethod;
    @SerializedName("numberOfFinishedSessions")
    protected Integer numberOfFinishedSessions;
    @SerializedName("traineeOrgUnits")
    protected List<String> traineeOrgUnits = new ArrayList<>();

    public String getFullName() {
        return MessageFormat.format("{0} {1}", this.name, this.surname);
    }
}
