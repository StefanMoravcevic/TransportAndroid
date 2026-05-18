package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.utils.EntityToOptionMapper;

import java.util.List;

public class OrgUnitsRecyclerViewAdapter
    extends BaseRecyclerViewAdapter<OrgUnitDto, OrgUnitsRecyclerViewAdapter.ViewHolder> {
    public OrgUnitsRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<OrgUnitDto> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(OrgUnitDto item) {
                return item.getId();
            }

            @Override
            public String getDescription(OrgUnitDto item) {
                return item.getName();
            }
        };
    }

    @NonNull @Override
    public OrgUnitsRecyclerViewAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_orgunit, parent, false));
    }

    @Override
    public void onBindViewHolder(
            @NonNull OrgUnitsRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        OrgUnitDto orgUnit = displayedItems.get(position);

        holder.tvName.setText(orgUnit.getName());
        holder.tvName.setSelected(true);
        holder.tvAddress.setText(orgUnit.getAddress());
        holder.tvAddress.setSelected(true);
        holder.tvPhoneNumber.setText(orgUnit.getPhoneNumber());
        holder.tvPhoneNumber.setSelected(true);

    }

    public class ViewHolder extends BaseViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
        TextView tvName;
        TextView tvAddress;
        TextView tvPhoneNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvAddress = itemView.findViewById(R.id.tvAddress);
            this.tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
        }
    }
}
