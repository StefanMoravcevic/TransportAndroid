package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.requests.ISearchParams;
import com.programdoo.transport.data.models.requests.trainees.SearchTraineesParams;
import com.programdoo.transport.utils.EntityToOptionMapper;
import com.programdoo.transport.utils.ExtendedFilter;
import com.programdoo.transport.utils.StringUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class TraineesRecyclerViewAdapter
        extends BaseRecyclerViewAdapter<TraineeDto, TraineesRecyclerViewAdapter.ViewHolder>
        implements ExtendedFilter {
    public TraineesRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<TraineeDto> items) {
        super(context, items);
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(TraineeDto item) {
                return item.getId();
            }
            @Override
            public String getDescription(TraineeDto item) {
                return MessageFormat.format("{0} {1}", item.getName(), item.getSurname());
            }
            @Override
            public String getSearchData(TraineeDto item) {
                String searchData = MessageFormat.format("{0} {1}", item.getNickname(), item.getPhoneNumber())
                        .toLowerCase().replace(" ", "");
                String description = getDescription(item).toLowerCase().replace(" ", "");
                return description + searchData;
            }
        };
    }

    public void extendedFilter(ISearchParams params) {
        SearchTraineesParams searchParams = (SearchTraineesParams) params;
        List<TraineeDto> filtered = new ArrayList<>(this.allItems);
        if (searchParams.trainerId != null)
            filtered.removeIf(
                    t -> ((t.getCurrentTrainerId() != null && t.getCurrentTrainerId().intValue() != searchParams.trainerId.intValue())
                            || (t.getTrialTrainerId() != null && t.getTrialTrainerId().intValue() != searchParams.trainerId.intValue())));
        if (searchParams.orgUnitId != null)
            filtered.removeIf(t -> (!t.getTraineeOrgUnitsIds().contains(searchParams.orgUnitId)));

        this.updateDisplayed(filtered);
    }

    @NonNull @Override
    public TraineesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trainee, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TraineesRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TraineeDto trainee = displayedItems.get(position);

        holder.tvFullName.setText(trainee.getFullName());
        holder.tvFullName.setSelected(true);
        holder.tvPhoneNumber.setText(trainee.getPhoneNumber());
        holder.tvPhoneNumber.setSelected(true);
        holder.tvTrainer.setText(StringUtil.normalizeForUi(holder.unknownLabel, trainee.getCurrentTrainerNameShort()));
        holder.tvTrainer.setSelected(true);
        holder.tvOrgUnitName.setText(StringUtil.normalizeForUi(holder.unknownLabel, trainee.getOrgUnitName()));
        holder.tvOrgUnitName.setSelected(true);
    }

    /**
     * ViewHolder definise kako ce da se prikazu podaci u adapteru. za njega se vezuje xml layout napravljen
     * u designeru (pogledaj <b>onCreateViewHolder</b> iznad).<br>
     * potrebno je registrovati ui komponente koje se koriste, cime se popunjavaju i click listener-e. trebalo bi
     * da bude sablonski kod, menja se samo koje komponente se registruju.
     */
    public class ViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvFullName;
        TextView tvPhoneNumber;
        TextView tvTrainer;
        TextView tvOrgUnitName;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.tvFullName = itemView.findViewById(R.id.tvFullName);
            this.tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            this.tvTrainer = itemView.findViewById(R.id.tvTrainer);
            this.tvOrgUnitName = itemView.findViewById(R.id.tvOrgUnitName);
        }
    }
}
