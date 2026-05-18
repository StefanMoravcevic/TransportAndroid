package com.programdoo.transport.data.models.dtos.appointments;

import lombok.Data;

@Data
public class SaveAppointmentsByPatternRequestModel {
    private SaveAppointmentRecurrencePatternRequestModel pattern;
    private SaveAppointmentRequestModel source;
}
