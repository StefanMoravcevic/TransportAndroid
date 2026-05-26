package com.programdoo.transport.data.models.dtos.employees;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class EmployeeDocumentAlertDto
{
    @SerializedName("type")
    private String type;
    @SerializedName("documentType")
    private String documentType;
    @SerializedName("validTo")
    private String validTo;
    @SerializedName("validToFormatted")
    private String validToFormatted;
    @SerializedName("daysLeft")
    private int daysLeft;
    @SerializedName("status")
    private String status;
}
