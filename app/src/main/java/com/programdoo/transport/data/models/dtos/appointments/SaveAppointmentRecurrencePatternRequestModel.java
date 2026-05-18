package com.programdoo.transport.data.models.dtos.appointments;

import com.google.gson.annotations.SerializedName;
import com.programdoo.transport.utils.DateUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.Data;

@Data
public class SaveAppointmentRecurrencePatternRequestModel implements Serializable {
    @SerializedName("id")
    protected int id;
    @SerializedName("mon")
    protected boolean mon;
    @SerializedName("tue")
    protected boolean tue;
    @SerializedName("wed")
    protected boolean wed;
    @SerializedName("thu")
    protected boolean thu;
    @SerializedName("fri")
    protected boolean fri;
    @SerializedName("sat")
    protected boolean sat;
    @SerializedName("sun")
    protected boolean sun;
    @SerializedName("dateFrom")
    protected LocalDateTime dateFrom;
    @SerializedName("dateTo")
    protected LocalDateTime dateTo;

    public String convertDaysToString() {
        String result = "";
        ArrayList<String> days = (ArrayList<String>) DateUtil.daysOfWeek();
        if (this.isMon()) result += days.get(0) + " ";
        if (this.isTue()) result += days.get(1) + " ";
        if (this.isWed()) result += days.get(2) + " ";
        if (this.isThu()) result += days.get(3) + " ";
        if (this.isFri()) result += days.get(4) + " ";
        if (this.isSat()) result += days.get(5) + " ";
        if (this.isSun()) result += days.get(6);

        return result;
    }
}
