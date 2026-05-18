package com.programdoo.transport.data.models.dtos.memberships;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class MembershipDto extends  SaveMembershipRequestModel implements Serializable {
    @SerializedName("membershipName")
    protected String membershipName;
    @SerializedName("membershipType")
    protected String membershipType;
    @SerializedName("finishedSessions")
    protected int finishedSessions;
    @SerializedName("scheduledSessions")
    protected int scheduledSessions;
    @SerializedName("freeSessions")
    protected int freeSessions;
    @SerializedName("availableSessions")
    protected int availableSessions;
    @SerializedName("cancelledSessions")
    protected int cancelledSessions;
    @SerializedName("daysValid")
    protected int daysValid;
    @SerializedName("traineeName")
    protected String traineeName;
    @SerializedName("purchaseDateFmt")
    protected String purchaseDateFmt;
    @SerializedName("paymentDateFmt")
    protected String paymentDateFmt;
    @SerializedName("dateFromFmt")
    protected String dateFromFmt;
    @SerializedName("dateToFmt")
    protected String dateToFmt;
    @SerializedName("orgUnitName")
    protected String orgUnitName;
    @SerializedName("traineeOrgUnitName")
    protected String traineeOrgUnitName;
    @SerializedName("trainerName")
    protected String trainerName;
    @SerializedName("paymentMethod")
    protected String paymentMethod;
    @SerializedName("currencyName")
    protected String currencyName;
    @SerializedName("typeId")
    protected int typeId;
}