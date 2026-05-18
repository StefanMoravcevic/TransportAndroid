package com.programdoo.transport.data.models.dtos.employees;

import com.google.gson.annotations.SerializedName;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeDto extends SaveEmployeeRequestModel {
    @SerializedName("dispatcher")
    protected String dispatcher;
    @SerializedName("dispatcherId")
    protected Integer dispatcherId;
    @SerializedName("jobType")
    protected String jobType;
    @SerializedName("company")
    protected String company;
    @SerializedName("orgUnit")
    protected String orgUnit;
    @SerializedName("partner")
    protected String partner;
    @SerializedName("state")
    protected String state;
    @SerializedName("city")
    protected String city;
    @SerializedName("stateShort")
    protected String stateShort;
    @SerializedName("gender")
    protected String gender;
    @SerializedName("employeeOrgUnits")
    protected List<String> employeeOrgUnits = new ArrayList<>();

    public String getFullName() {
        return MessageFormat.format("{0} {1}", this.name, this.surname);
    }
    public String getFullNameShort() {
        return MessageFormat.format("{0} {1}.", this.name, this.surname.toCharArray()[0]);
    }
    public String getOrgUnitsList() {
        if (employeeOrgUnits.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < employeeOrgUnits.size() - 1; ++i)
            sb.append(employeeOrgUnits.get(i)).append(", ");
        sb.append(employeeOrgUnits.get(i));
        return sb.toString();
    }
}
