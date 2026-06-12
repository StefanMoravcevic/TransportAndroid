package com.programdoo.transport.data.models.dtos.employeesNotifications;

public class NotificationRequest {
    public int employeeId;
    public Boolean isRead;

    public NotificationRequest(int employeeId, Boolean isRead) {
        this.employeeId = employeeId;
        this.isRead = isRead;
    }
}