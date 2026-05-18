package com.programdoo.transport.data.models.dtos.companies;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OrgUnitDto extends SaveOrgUnitRequestModel {
    @SerializedName("defaultAccount")
    protected String defaultAccount;
    @SerializedName("defaultCostName")
    protected String defaultCostName;
    @SerializedName("defaultAccountCurrencyId")
    protected Integer defaultAccountCurrencyId;
    @SerializedName("company")
    protected String company;
    @SerializedName("parentOrgUnit")
    protected String parentOrgUnit;
    @SerializedName("address")
    protected String address;
}
