package com.programdoo.transport.data.models.dtos.employeesNotifications;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class EmployeeNotificationDto extends  SaveEmployeeNotificationRequestModel{

    @SerializedName("validToFormatted")
    public String validToFormatted;
}
