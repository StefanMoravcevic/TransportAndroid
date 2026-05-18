package com.programdoo.transport.ui.adapters.appointments;

import androidx.annotation.NonNull;

import com.alamkanak.weekview.WeekViewEntity;
import com.alamkanak.weekview.jsr310.WeekViewSimpleAdapterJsr310;
import com.programdoo.transport.R;
import com.programdoo.transport.ui.callbacks.OnEventClickListener;
import com.programdoo.transport.ui.callbacks.OnEventLongClickListener;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

public class CalendarAdapter extends WeekViewSimpleAdapterJsr310<CalendarEvent> {
    @Getter @Setter
    private int currentMonth = -1;
    @Setter
    private OnRangeChangedListener rangeListener;
    @Setter
    private OnEventClickListener clickListener;
    @Setter
    private OnEventLongClickListener longClickListener;

    public CalendarAdapter() {

    }

    public interface OnRangeChangedListener {
        void onRangeChanged(LocalDate start, LocalDate end);
    }

    @NonNull
    @Override
    public WeekViewEntity onCreateEntity(
            CalendarEvent item) {
        WeekViewEntity.Style style = new WeekViewEntity.Style.Builder()
                .setBackgroundColor(getContext().getColor(item.backgroundColor))
                .setBorderColor(getContext().getColor(item.borderColor))
                .setBorderWidth(4)
                .setTextColor(getContext().getColor(R.color.secondary))
                .build();

        return new WeekViewEntity.Event.Builder<>(item)
                .setId(item.getId())
                .setTitle(item.getTitle())
//                .setSubtitle(item.getLocation())
                .setStartTime(item.getStart())
                .setEndTime(item.getEnd())
                .setStyle(style)
                .build();
    }

    @Override
    public void onRangeChanged(@NonNull LocalDate start, @NonNull LocalDate end) {
        this.rangeListener.onRangeChanged(start, end);
    }

    @Override
    public void onEventClick(CalendarEvent event) {
        this.clickListener.onEventClick(event.getSource());
    }

    @Override
    public void onEventLongClick(CalendarEvent event) {
        this.longClickListener.onEventLongClick(event.getSource());
    }
}
