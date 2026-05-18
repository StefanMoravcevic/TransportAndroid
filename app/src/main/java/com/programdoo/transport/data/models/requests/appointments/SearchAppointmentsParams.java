package com.programdoo.transport.data.models.requests.appointments;

import com.programdoo.transport.data.models.requests.ISearchParams;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class SearchAppointmentsParams implements ISearchParams {
    public Integer id;
    public Integer recurrencePatternId;
    public Integer trainerId;
    public Integer traineeId;
    public Integer orgUnitId;
    public LocalDateTime date;
    public LocalTime startTime;
    public LocalTime endTime;
    public LocalDateTime dateFrom;
    public LocalDateTime dateTo;
    public Integer limit;
    public Boolean finished;
    public Boolean cancelled;
}
