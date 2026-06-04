package com.programdoo.transport.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.programdoo.transport.data.models.dtos.travelOrders.TravelOrderDto;
import com.programdoo.transport.databinding.ItemTravelOrderBinding;

import java.util.ArrayList;
import java.util.List;

public class TravelOrderRecyclerListAdapter extends RecyclerView.Adapter<TravelOrderRecyclerListAdapter.ViewHolder> {

    private final List<TravelOrderDto> items = new ArrayList<>();

    // =========================
    // CAMERA CLICK LISTENER
    // =========================
    public interface OnCameraClickListener {
        void onCameraClick(TravelOrderDto item);
    }

    private OnCameraClickListener cameraClickListener;

    public void setOnCameraClickListener(OnCameraClickListener listener) {
        this.cameraClickListener = listener;
    }

    // =========================
    // SUBMIT LIST
    // =========================
    public void submitList(List<TravelOrderDto> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    // =========================
    // VIEW HOLDER CREATE
    // =========================
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemTravelOrderBinding binding =
                ItemTravelOrderBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new ViewHolder(binding);
    }

    // =========================
    // BIND
    // =========================
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // =========================
    // VIEW HOLDER
    // =========================
    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemTravelOrderBinding binding;

        public ViewHolder(ItemTravelOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TravelOrderDto item) {

            binding.tvDriver.setText(item.getEmployee());
            binding.tvState.setText(item.getState());
            binding.tvFrom.setText(item.getDateFormatted());
            binding.tvTo.setText(item.getReturnDateFormatted());

            // =========================
            // CAMERA CLICK
            // =========================
            binding.ivDocuments.setOnClickListener(v -> {
                if (cameraClickListener != null) {
                    cameraClickListener.onCameraClick(item);
                }
            });
        }
    }
}