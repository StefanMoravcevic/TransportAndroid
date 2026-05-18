package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.requests.ISearchParams;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.utils.EntityToOptionMapper;
import com.programdoo.transport.utils.ExtendedFilter;

import java.util.ArrayList;
import java.util.List;

public class EmployeesRecyclerViewAdapter
        extends BaseRecyclerViewAdapter<EmployeeDto, EmployeesRecyclerViewAdapter.ViewHolder>
        implements ExtendedFilter {
    public EmployeesRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<EmployeeDto> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(EmployeeDto item) {
                return item.getId();
            }

            @Override
            public String getDescription(EmployeeDto item) {
                return item.getFullName();
            }
        };
    }

    @NonNull @Override
    public EmployeesRecyclerViewAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_employee, parent, false));
    }

    @Override
    public void onBindViewHolder(
            @NonNull EmployeesRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        EmployeeDto employee = displayedItems.get(position);

        holder.tvFullName.setText(employee.getFullName());
        holder.tvFullName.setSelected(true);
        holder.tvPhoneNumber.setText(employee.getCellPhoneNumber());
        holder.tvPhoneNumber.setSelected(true);
        holder.tvOrgUnitName.setText(employee.getOrgUnitsList());
        holder.tvOrgUnitName.setSelected(true);
    }

    @Override
    public void extendedFilter(ISearchParams params) {
        SearchEmployeesParams searchParams = (SearchEmployeesParams) params;
        List<EmployeeDto> filtered = new ArrayList<>(this.allItems);
        if (searchParams.orgUnitId != null)
            filtered.removeIf(e -> (!e.getEmployeesOrgUnitsIds().contains(searchParams.orgUnitId)));

        this.updateDisplayed(filtered);
    }

    public class ViewHolder extends BaseViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
        TextView tvFullName;
        TextView tvPhoneNumber;
        TextView tvOrgUnitName;
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.tvFullName = itemView.findViewById(R.id.tvFullName);
            this.tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            this.tvOrgUnitName = itemView.findViewById(R.id.tvOrgUnitName);
        }
    }
}
