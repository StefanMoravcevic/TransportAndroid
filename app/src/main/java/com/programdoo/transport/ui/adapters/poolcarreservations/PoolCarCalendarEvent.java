package com.programdoo.transport.ui.adapters.poolcarreservations;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;

import java.time.LocalDateTime;
import java.util.Calendar;

public class PoolCarCalendarEvent {

    private PoolCarReservationDto source;

    private long id;
    private String title;

    private Calendar start;
    private Calendar end;

    private String employeeName;
    private String vehicleName;

    int backgroundColor;
    int borderColor;

    public PoolCarCalendarEvent(PoolCarReservationDto source) {
        this.source = source;
        convert();
    }

    private void convert() {

        this.id = source.getId();

        this.employeeName = source.getEmployee();
        this.vehicleName = source.getVehicle();

        this.title = employeeName + " - " + vehicleName;

        this.start = toCalendar(source.getDateFrom());
        this.end = toCalendar(source.getDateTo());

        this.backgroundColor = R.color.primary;
        this.borderColor = R.color.primary;
    }

    private Calendar toCalendar(LocalDateTime dt) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, dt.getYear());
        cal.set(Calendar.MONTH, dt.getMonthValue() - 1);
        cal.set(Calendar.DAY_OF_MONTH, dt.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, dt.getHour());
        cal.set(Calendar.MINUTE, dt.getMinute());
        return cal;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public Calendar getStart() { return start; }
    public Calendar getEnd() { return end; }
    public String getEmployeeName() { return employeeName; }
    public String getVehicleName() { return vehicleName; }
    public int getBackgroundColor() { return backgroundColor; }
    public int getBorderColor() { return borderColor; }

    public PoolCarReservationDto getSource() { return source; }
}