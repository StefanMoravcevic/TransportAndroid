package com.programdoo.transport.data.models.dtos.employeesNotifications;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SaveEmployeeNotificationRequestModel implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("employeeId")
    private Integer employeeId;

    @SerializedName("sourceType")
    private String sourceType;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("sourceTypeId")
    private Integer sourceTypeId;

    @SerializedName("isRead")
    private boolean isRead;

    @SerializedName("createdAt")
    private LocalDateTime createdAt;

    @SerializedName("validTo")
    private LocalDateTime validTo;
}