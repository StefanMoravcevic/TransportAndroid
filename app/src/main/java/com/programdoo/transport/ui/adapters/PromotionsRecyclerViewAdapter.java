package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.promotions.PromotionDto;
import com.programdoo.transport.utils.EntityToOptionMapper;

import java.util.List;

import lombok.NonNull;

public class PromotionsRecyclerViewAdapter
        extends BaseRecyclerViewAdapter<PromotionDto, PromotionsRecyclerViewAdapter.ViewHolder> {
    public PromotionsRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<PromotionDto> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(PromotionDto item) {
                return item.getId();
            }
            @Override
            public String getDescription(PromotionDto item) {
                return item.getName();
            }
        };
    }


    @NonNull
    @Override
    public PromotionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PromotionsRecyclerViewAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_promotion, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionsRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        PromotionDto promotion = displayedItems.get(position);

        holder.tvName.setText(promotion.getName());
        holder.tvFrom.setText(promotion.getDateFromFmt());
        holder.tvTo.setText(promotion.getDateToFmt());
    }

    /**
     * ViewHolder definise kako ce da se prikazu podaci u adapteru. za njega se vezuje xml layout napravljen
     * u designeru (pogledaj <b>onCreateViewHolder</b> iznad).<br>
     * potrebno je registrovati ui komponente koje se koriste, cime se popunjavaju i click listener-e. trebalo bi
     * da bude sablonski kod, menja se samo koje komponente se registruju.
     */
    public class ViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvName;
        TextView tvFrom;
        TextView tvTo;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvFrom = itemView.findViewById(R.id.tvFrom);
            this.tvTo = itemView.findViewById(R.id.tvTo);
        }
    }
}
