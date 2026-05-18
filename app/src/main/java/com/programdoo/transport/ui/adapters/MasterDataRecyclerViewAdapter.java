package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.masterData.MasterDataDto;
import com.programdoo.transport.utils.EntityToOptionMapper;

import java.text.MessageFormat;
import java.util.List;

import lombok.NonNull;

public class MasterDataRecyclerViewAdapter extends BaseRecyclerViewAdapter<MasterDataDto, MasterDataRecyclerViewAdapter.ViewHolder>{

    public MasterDataRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<MasterDataDto> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(MasterDataDto item) {
                return item.getValue();
            }
            @Override
            public String getDescription(MasterDataDto item) {
                return MessageFormat.format("{0}", item.getDescription());
            }

        };
    }

    @NonNull
    @Override
    public MasterDataRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MasterDataRecyclerViewAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_masterdata, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MasterDataRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        MasterDataDto masterData = displayedItems.get(position);

        holder.tvDescription.setText(masterData.getDescription());
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvDescription;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.tvDescription = itemView.findViewById(R.id.tvDescription);

        }
    }
}
