package com.programdoo.transport.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;
import com.programdoo.transport.ui.adapters.appointments.CalendarEvent;

import java.util.ArrayList;
import java.util.List;

public class CalendarUtil {
    public static List<CalendarEvent> convert(
            @NonNull Context context,
            List<AppointmentDto> appointments) {
        List<CalendarEvent> result = new ArrayList<>();

        if (appointments == null || appointments.isEmpty())
            return result;

        for (AppointmentDto appt: appointments) {
            result.add(new CalendarEvent(context, appt));
        }

        return result;
    }
}
