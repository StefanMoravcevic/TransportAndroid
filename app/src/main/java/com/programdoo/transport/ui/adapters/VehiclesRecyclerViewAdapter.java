package com.programdoo.transport.ui.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.vehicles.VehicleDto;
import com.programdoo.transport.data.models.requests.ISearchParams;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.utils.EntityToOptionMapper;
import com.programdoo.transport.utils.ExtendedFilter;

import java.util.ArrayList;
import java.util.List;

public class VehiclesRecyclerViewAdapter
        extends BaseRecyclerViewAdapter<VehicleDto, VehiclesRecyclerViewAdapter.ViewHolder>
         {
    public VehiclesRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<VehicleDto> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(VehicleDto item) {
                return item.getId();
            }

            @Override
            public String getDescription(VehicleDto item) {
                return item.licensePlate;
            }
        };
    }

    @NonNull @Override
    public VehiclesRecyclerViewAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_vehicle, parent, false));
    }

    @Override
    public void onBindViewHolder(
            @NonNull VehiclesRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        VehicleDto vehicle = displayedItems.get(position);

        holder.tvFullName.setText(vehicle.getLicensePlate() + " - " + vehicle.getInternalNumber() + " - " + vehicle.getManufacturer() + " - " + vehicle.getModel());
        holder.tvFullName.setSelected(true);
    }

    public class ViewHolder extends BaseViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView tvFullName;
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.tvFullName = itemView.findViewById(R.id.tvFullName);
        }
    }
}
