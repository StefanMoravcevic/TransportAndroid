package com.programdoo.transport.ui.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.memberships.MembershipCardDto;
import com.programdoo.transport.utils.EntityToOptionMapper;

import java.util.List;

import lombok.NonNull;
public class MembershipCardsRecyclerViewAdapter extends BaseRecyclerViewAdapter<MembershipCardDto, MembershipCardsRecyclerViewAdapter.ViewHolder> {

    public MembershipCardsRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<MembershipCardDto> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(MembershipCardDto item) {
                return item.getId();
            }

            @Override
            public String getDescription(MembershipCardDto item) {
                return item.getName();
            }
        };
    }
    @NonNull
    @Override
    public MembershipCardsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MembershipCardsRecyclerViewAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_membershipcard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MembershipCardsRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        MembershipCardDto membershipCard = displayedItems.get(position);

        holder.tvName.setText(membershipCard.getName());
    }
    public class ViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvName;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
