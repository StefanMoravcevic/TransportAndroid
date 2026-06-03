package com.programdoo.transport.ui.pages.poolCarReservations;

import lombok.Data;

@Data
public class ReservationListItem {

    public String driver;
    public String vehicle;
    public String dateFrom;
    public String dateTo;

    public ReservationListItem(String driver, String vehicle, String dateFrom, String dateTo) {
        this.driver = driver;
        this.vehicle = vehicle;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}