package com.programdoo.transport.ui.callbacks;

import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;

public interface OnEventClickListener {
    void onEventClick(AppointmentDto appointment);
}
