package com.programdoo.transport.data.models.requests.poolCarReservations;

import com.programdoo.transport.data.models.requests.ISearchParams;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SearchPoolCarReservationParams implements ISearchParams {

    public Integer id;
    public Integer vehicleId;
    public Integer employeeId;

    public LocalDateTime dateFrom;

    public LocalDateTime dateTo;

    public LocalDateTime date;

    public boolean isDivorced;
}
