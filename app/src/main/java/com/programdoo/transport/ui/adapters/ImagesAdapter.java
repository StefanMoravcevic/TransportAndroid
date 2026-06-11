package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.documents.DocumentDto;
import com.programdoo.transport.data.settings.Settings;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.VH> {

    private final Context context;
    private List<DocumentDto> items = new ArrayList<>();

    public ImagesAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DocumentDto> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);

        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        DocumentDto item = items.get(position);

        String url = Settings.ApiUrl_Local +
                "documents/download/" +
                item.getId();

        Glide.with(context)
                .load(url)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        ImageView image;

        public VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgDocument);
        }
    }
}