package com.programdoo.transport.ui.adapters.appointments;

import androidx.annotation.NonNull;

import com.alamkanak.weekview.WeekViewEntity;
import com.alamkanak.weekview.jsr310.WeekViewSimpleAdapterJsr310;
import com.programdoo.transport.R;
import com.programdoo.transport.ui.adapters.poolcarreservations.PoolCarCalendarEvent;
import com.programdoo.transport.ui.callbacks.OnEventClickListener;
import com.programdoo.transport.ui.callbacks.OnEventClickListenerPoolCar;
import com.programdoo.transport.ui.callbacks.OnEventLogClickListenerPoolCar;
import com.programdoo.transport.ui.callbacks.OnEventLongClickListener;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

public class PoolCarCalendarAdapter extends WeekViewSimpleAdapterJsr310<PoolCarCalendarEvent> {
    @Getter @Setter
    private int currentMonth = -1;
    @Setter
    private OnRangeChangedListener rangeListener;
    @Setter
    private OnEventClickListenerPoolCar clickListener;
    @Setter
    private OnEventLogClickListenerPoolCar longClickListener;

    public PoolCarCalendarAdapter() {

    }

    public interface OnRangeChangedListener {
        void onRangeChanged(LocalDate start, LocalDate end);
    }

    @NonNull
    @Override
    public WeekViewEntity onCreateEntity(
            PoolCarCalendarEvent item) {
        WeekViewEntity.Style style = new WeekViewEntity.Style.Builder()
                .setBorderWidth(4)
                .setTextColor(getContext().getColor(R.color.white))
                .setBackgroundColor(getContext().getColor(R.color.errorLighter))
                .setBorderColor(getContext().getColor(R.color.primary))
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
    public void onEventClick(PoolCarCalendarEvent event) {
        this.clickListener.onEventClick(event.getSource());
    }

    @Override
    public void onEventLongClick(PoolCarCalendarEvent event) {
        this.longClickListener.onEventLongClick(event.getSource());
    }
}
