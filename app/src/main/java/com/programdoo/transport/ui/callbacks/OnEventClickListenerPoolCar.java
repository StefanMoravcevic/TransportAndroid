package com.programdoo.transport.ui.callbacks;

import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;

public interface OnEventClickListenerPoolCar {
    void onEventClick(PoolCarReservationDto appointment);
}
