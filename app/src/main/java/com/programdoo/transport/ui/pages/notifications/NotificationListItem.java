package com.programdoo.transport.ui.pages.notifications;

import com.programdoo.transport.data.models.dtos.employees.EmployeeDocumentAlertDto;

public class NotificationListItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NOTIFICATION = 1;

    public int viewType;
    public String header;
    public EmployeeDocumentAlertDto notification;

    public static NotificationListItem header(String text) {
        NotificationListItem item = new NotificationListItem();
        item.viewType = TYPE_HEADER;
        item.header = text;
        return item;
    }

    public static NotificationListItem notification(EmployeeDocumentAlertDto dto) {
        NotificationListItem item = new NotificationListItem();
        item.viewType = TYPE_NOTIFICATION;
        item.notification = dto;
        return item;
    }
}