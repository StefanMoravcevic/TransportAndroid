package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.memberships.MembershipDto;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.EntityToOptionMapper;
import com.programdoo.transport.utils.StringUtil;

import java.util.List;

public class MembershipsRecyclerViewAdapter
        extends BaseRecyclerViewAdapter<MembershipDto, MembershipsRecyclerViewAdapter.ViewHolder> {

    public MembershipsRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<MembershipDto> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(MembershipDto item) {
                return item.getId();
            }

            @Override
            public String getDescription(MembershipDto item) {
                return item.getMembershipName();
            }

            @Override
            public String getSearchData(MembershipDto item) {
                return getDescription(item).toLowerCase().replace(" ", "");
            }
        };
    }

    public void setData(List<MembershipDto> data) {
        allItems.clear();
        allItems.addAll(data);
        displayedItems.clear();
        displayedItems.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_membership, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        MembershipDto membership = displayedItems.get(position);
        holder.trainee.setText(membership.getTraineeName());
//        holder.tvMembershipName.setText(MessageFormat.format("Članarina: {0}", membership.getMembershipName()));
//        holder.tvMembershipType.setText(MessageFormat.format("Tip: {0}", membership.getMembershipType()));
        holder.validFrom.setText(DateUtil.format(membership.getValidFromDate()));
        holder.validTo.setText(DateUtil.format(membership.getValidToDate()));
        holder.totalTrainings.setText(StringUtil.normalizeForUi("?", StringUtil.toString(membership.getTotalSessions())));
        holder.sessionsLeft.setText(StringUtil.normalizeForUi("?", StringUtil.toString(membership.getAvailableSessions())));
    }

    public class ViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
//        TextView tvMembershipName;
//        TextView tvMembershipType;
        TextView validFrom;
        TextView validTo;
        TextView trainee;
        TextView totalTrainings;
        TextView sessionsLeft;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

//            tvMembershipName = itemView.findViewById(R.id.tvMembershipName);
//            tvMembershipType = itemView.findViewById(R.id.tvMembershipType);
            validFrom = itemView.findViewById(R.id.tvValidFrom);
            validTo = itemView.findViewById(R.id.tvValidTo);
            trainee = itemView.findViewById(R.id.tvTraineeName);
            totalTrainings = itemView.findViewById(R.id.totalTrainings);
            sessionsLeft = itemView.findViewById(R.id.sessionsLeft);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getBindingAdapterPosition(), displayedItems.get(getLayoutPosition()));
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(view, getBindingAdapterPosition(), displayedItems.get(getLayoutPosition()));
            }
            return true;
        }
    }
}