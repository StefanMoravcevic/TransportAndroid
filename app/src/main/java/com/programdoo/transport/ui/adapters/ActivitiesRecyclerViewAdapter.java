package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.activities.ActivityDto;
import com.programdoo.transport.utils.EntityToOptionMapper;
import com.programdoo.transport.utils.StringUtil;

import java.text.MessageFormat;
import java.util.List;

import lombok.NonNull;

public class ActivitiesRecyclerViewAdapter
        extends BaseRecyclerViewAdapter<ActivityDto, ActivitiesRecyclerViewAdapter.ViewHolder> {
    public ActivitiesRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<ActivityDto> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(ActivityDto item) {
                return item.getId();
            }
            @Override
            public String getDescription(ActivityDto item) {
                return MessageFormat.format("{0} {1}", item.getDate(), item.getTime());
            }
            @Override
            public String getSearchData(ActivityDto item) {
                String searchData = MessageFormat.format("{0} {1}", item.getActivityDescription(), item.getTrainerComment())
                        .toLowerCase().replace(" ", "");
                String description = getDescription(item).toLowerCase().replace(" ", "");
                return description + searchData;
            }
        };
    }

    @NonNull @Override
    public ActivitiesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ActivityDto activity = displayedItems.get(position);

        holder.tvTrainerComment.setText(activity.getTrainerComment());
        holder.tvTrainerComment.setSelected(true);
        holder.tvActivityDescription.setText(activity.getActivityDescription());
        holder.tvActivityDescription.setSelected(true);
        holder.tvDate.setText(activity.getDate().toLocalDate().toString() + ' ' + activity.getTime().toString());
        holder.tvRating.setText(StringUtil.toString(activity.getRating()));
        holder.tvActivityName.setText(activity.getActivityName());
        holder.tvActivityName.setSelected(true);
    }
    /**
     * ViewHolder definise kako ce da se prikazu podaci u adapteru. za njega se vezuje xml layout napravljen
     * u designeru (pogledaj <b>onCreateViewHolder</b> iznad).<br>
     * potrebno je registrovati ui komponente koje se koriste, cime se popunjavaju i click listener-e. trebalo bi
     * da bude sablonski kod, menja se samo koje komponente se registruju.
     */
    public class ViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvTrainerComment;
        TextView tvActivityDescription;
        TextView tvDate;
        TextView tvRating;
        TextView tvActivityName;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.tvTrainerComment = itemView.findViewById(R.id.tvTrainerComment);
            this.tvActivityDescription = itemView.findViewById(R.id.tvActivityDescription);
            this.tvActivityName = itemView.findViewById(R.id.tvActivityName);
            this.tvDate = itemView.findViewById(R.id.tvDate);
            this.tvRating = itemView.findViewById(R.id.tvRating);
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
