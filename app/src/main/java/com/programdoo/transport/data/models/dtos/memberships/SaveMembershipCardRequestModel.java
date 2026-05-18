package com.programdoo.transport.data.models.dtos.memberships;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class SaveMembershipCardRequestModel {
    @SerializedName("id")
    private int id;
    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;
    @SerializedName("typeId")
    private int typeId;
    @SerializedName("numberOfSessions")
    private int numberOfSessions;
    @SerializedName("numberOfDays")
    private int numberOfDays;
    @SerializedName("orgUnitId")
    private Integer orgUnitId;

    @SerializedName("price")
    private double price;

    public SaveMembershipCardRequestModel() {

    }
}
