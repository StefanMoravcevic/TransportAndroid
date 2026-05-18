package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.enums.BaseEnum;
import com.programdoo.transport.utils.EntityToOptionMapper;

import java.util.List;

public class EnumRecyclerViewAdapter
        extends BaseRecyclerViewAdapter<BaseEnum, EnumRecyclerViewAdapter.ViewHolder> {
    public EnumRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<BaseEnum> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(BaseEnum item) {
                return item.getValue();
            }

            @Override
            public String getDescription(BaseEnum item) {
                return item.getDescription();
            }
        };
    }

    @NonNull
    @Override
    public EnumRecyclerViewAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_masterdata, parent, false));
    }

    @Override
    public void onBindViewHolder(
            @NonNull EnumRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        BaseEnum item = displayedItems.get(position);

        holder.tvDescription.setText(item.getDescription());
    }

    public class ViewHolder extends BaseViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView tvDescription;
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
