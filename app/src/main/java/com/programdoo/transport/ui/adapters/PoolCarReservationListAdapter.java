package com.programdoo.transport.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.databinding.ItemPoolCarReservationBinding;
import com.programdoo.transport.ui.pages.poolCarReservations.ReservationListItem;

import java.util.ArrayList;
import java.util.List;

public class PoolCarReservationListAdapter extends RecyclerView.Adapter<PoolCarReservationListAdapter.ViewHolder> {

    private final List<ReservationListItem> items = new ArrayList<>();

    public void submitList(List<ReservationListItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPoolCarReservationBinding binding =
                ItemPoolCarReservationBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemPoolCarReservationBinding binding;

        public ViewHolder(ItemPoolCarReservationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ReservationListItem item) {

            binding.tvDriver.setText(item.getDriver());
            binding.tvVehicle.setText(item.getVehicle());
            binding.tvFrom.setText(item.getDateFrom());
            binding.tvTo.setText(item.getDateTo());
        }
    }
}