package com.programdoo.transport.data.models.dtos.companies;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class SaveOrgUnitRequestModel {
    @SerializedName("id")
    protected int id;
    @SerializedName("code")
    protected String code;
    @SerializedName("name")
    protected String name;
    @SerializedName("companyId")
    protected int companyId;
    @SerializedName("parentOrgUnitId")
    protected Integer parentOrgUnitId;
    @SerializedName("defaultAccountId")
    protected Integer defaultAccountId;
    @SerializedName("defaultCostId")
    protected Integer defaultCostId;
    @SerializedName("phoneNumber")
    protected String phoneNumber;
    @SerializedName("email")
    protected String email;
    @SerializedName("active")
    protected boolean active;
}
