package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;
import com.programdoo.transport.data.models.requests.ISearchParams;
import com.programdoo.transport.data.models.requests.appointments.SearchAppointmentsParams;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.EntityToOptionMapper;
import com.programdoo.transport.utils.ExtendedFilter;
import com.programdoo.transport.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsRecyclerViewAdapter
        extends BaseRecyclerViewAdapter<AppointmentDto, AppointmentsRecyclerViewAdapter.ViewHolder>
        implements ExtendedFilter {

    public AppointmentsRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<AppointmentDto> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(AppointmentDto item) {
                return item.getId();
            }

            @Override
            public String getDescription(AppointmentDto item) {
                /* TODO */
                return "";
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_appointment, parent, false));
    }

    @Override
    public void onBindViewHolder(
            @NonNull AppointmentsRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        AppointmentDto a = displayedItems.get(position);
        holder.tvDate.setText(DateUtil.format(a.getDate()));
        holder.tvStartTime.setText(TimeUtil.format(a.getStartTime()));
        holder.tvEndTime.setText(TimeUtil.format(a.getEndTime()));
        holder.tvOrgUnitName.setText(a.getOrgUnitName());
        holder.tvTrainer.setText(a.getTrainerName());
        String statusText = "";
        if (a.isFinished()) statusText = holder.itemView.getContext().getString(R.string.label_finished);
        else if (a.isCancelled()) statusText = holder.itemView.getContext().getString(R.string.label_cancelled);
        holder.tvStatus.setText(statusText);
    }

    @Override
    public void extendedFilter(ISearchParams params) {
        SearchAppointmentsParams searchParams = (SearchAppointmentsParams) params;
        List<AppointmentDto> filtered = new ArrayList<>(this.allItems);
        this.updateDisplayed(filtered);
    }

    public class ViewHolder extends BaseViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView tvDate;
        TextView tvStartTime;
        TextView tvEndTime;
        TextView tvOrgUnitName;
        TextView tvTrainer;
        TextView tvStatus;
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
