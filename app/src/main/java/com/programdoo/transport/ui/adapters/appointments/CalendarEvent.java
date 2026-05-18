package com.programdoo.transport.ui.adapters.appointments;

import android.content.Context;

import androidx.annotation.NonNull;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;

import lombok.Getter;


public class CalendarEvent {
    @Getter
    private AppointmentDto source;
    @Getter
    private long id;
    @Getter
    private String title;
    @Getter
    private String location;
    @Getter
    private Calendar start;
    @Getter
    private Calendar end;
    @Getter
    int backgroundColor;
    @Getter
    int borderColor;

    public CalendarEvent(
            @NonNull Context context,
            AppointmentDto source) {
        this.source = source;
        convert(context);
    }

    private void convert(@NonNull Context context) {
        this.id = source.getId();

//        this.title = MessageFormat.format("{0} - {1}", source.getTrainerName(), source.getTraineeName());
        this.title = source.getEventTitle();
//        this.location = source.getOrgUnitName() + "\n";

        this.start = Calendar.getInstance();
        this.start.clear();
        this.start.set(Calendar.YEAR, source.getDate().getYear());
        this.start.set(Calendar.MONTH, source.getDate().getMonthValue() - 1);
        this.start.set(Calendar.DAY_OF_MONTH, source.getDate().getDayOfMonth());
        this.start.set(Calendar.HOUR_OF_DAY, source.getStartTime().getHour());
        this.start.set(Calendar.MINUTE, source.getStartTime().getMinute());

        this.end = (Calendar) this.start.clone();
        this.end.set(Calendar.HOUR_OF_DAY, source.getEndTime().getHour());
        this.end.set(Calendar.MINUTE, source.getEndTime().getMinute());

        LocalDateTime startDT = source.getDate().with(source.getStartTime());
        LocalDateTime now = LocalDateTime.now();

        boolean isExpired = startDT.isBefore(now);
        boolean isTrial = source.isInTrialPeriod();
        boolean isInvalid = !source.isValid();
        boolean isFinished = source.isFinished();
        long daysToExpiration = Integer.MAX_VALUE;
        if (source.getMembershipExpiryDate() != null) {
            daysToExpiration = Duration.between(startDT, source.getMembershipExpiryDate()).toDays();
//            location += StringUtil.toString((int) daysToExpiration) + " " + context.getString(R.string.label_days_left_lc) + ", ";
        }
//        location += StringUtil.toString(source.getSessionsLeftAfter()) + " " + context.getString(R.string.label_sessions_left_lc);
        boolean warning = source.getSessionsLeftAfter() == 0 || daysToExpiration == 0;

        /* bojenje probnog termina */
        if (isTrial && isFinished) {
            backgroundColor = R.color.trialDarker;
            borderColor = R.color.trial;
        }
        else if (isTrial && !isExpired) {
            backgroundColor = R.color.trial;
            borderColor = R.color.trial;
        }
        else if (isTrial && isExpired) {
            backgroundColor = R.color.trialLighter;
            borderColor = R.color.trial;
        }
        /* bojenje nevalidnog termina */
        else if (isInvalid && isFinished) {
            backgroundColor = R.color.criticalDarker;
            borderColor = R.color.critical;
        }
        else if (isInvalid && !isExpired) {
            backgroundColor = R.color.critical;
            borderColor = R.color.critical;
        }
        else if (isInvalid && isExpired) {
            backgroundColor = R.color.criticalLighter;
            borderColor = R.color.critical;
        }
        /* bojenje validnog termina sa upozorenjem za istek */
        else if (warning && isFinished) {
            backgroundColor = R.color.warningDarker;
            borderColor = R.color.warning;
        }
        else if (warning && !isExpired) {
            backgroundColor = R.color.warning;
            borderColor = R.color.warning;
        }
        else if (warning && isExpired) {
            backgroundColor = R.color.warningLighter;
            borderColor = R.color.warning;
        }
        /* bojenje validnog termina */
        else if (isFinished) {
            backgroundColor = R.color.primaryDarker;
            borderColor = R.color.primaryDarker;
        }
        else if (!isExpired) {
            backgroundColor = R.color.primary;
            borderColor = R.color.primary;
        }
        else if (isExpired) {
            backgroundColor = R.color.primaryLightest;
            borderColor = R.color.primary;
        }
    }
}
