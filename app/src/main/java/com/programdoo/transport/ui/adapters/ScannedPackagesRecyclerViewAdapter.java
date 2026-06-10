package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.scannedpackages.ScannedPackageDto;
import com.programdoo.transport.data.models.requests.ISearchParams;
import com.programdoo.transport.data.models.requests.scannedPackages.SearchScannedPackagesParams;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.EntityToOptionMapper;
import com.programdoo.transport.utils.ExtendedFilter;
import com.programdoo.transport.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class ScannedPackagesRecyclerViewAdapter
        extends BaseRecyclerViewAdapter<ScannedPackageDto, ScannedPackagesRecyclerViewAdapter.ViewHolder>
        implements ExtendedFilter {

    private final OnCameraClickListener cameraClickListener;
    public ScannedPackagesRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<ScannedPackageDto> items, OnCameraClickListener cameraClickListener) {
        super(context, items);
        this.cameraClickListener = cameraClickListener;
        mapper = new EntityToOptionMapper<>() {
            @Override
            public int getId(ScannedPackageDto item) {
                return item.getId();
            }

            @Override
            public String getDescription(ScannedPackageDto item) {
                /* TODO */
                return "";
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_package, parent, false));
    }
    public interface OnCameraClickListener {
        void onCameraClick(int position);
    }
    @Override
    public void onBindViewHolder(
            @NonNull ScannedPackagesRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ScannedPackageDto a = displayedItems.get(position);
        holder.tvPackageNo.setText(a.getPackageNo());
        holder.btnCamera.setOnClickListener(v -> {
            if (cameraClickListener != null) {
                cameraClickListener.onCameraClick(position);
            }
        });
    }

    @Override
    public void extendedFilter(ISearchParams params) {
        SearchScannedPackagesParams searchParams = (SearchScannedPackagesParams) params;
        List<ScannedPackageDto> filtered = new ArrayList<>(this.allItems);
        this.updateDisplayed(filtered);
    }

    public class ViewHolder extends BaseViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView tvPackageNo;
        ImageButton btnCamera;
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            //itemView.btnCamera(this);

            this.tvPackageNo = itemView.findViewById(R.id.tvPackageNo);
            this.btnCamera = itemView.findViewById(R.id.btnCamera);
        }
    }

}
