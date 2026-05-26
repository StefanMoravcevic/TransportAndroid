package com.programdoo.transport.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDocumentAlertDto;
import com.programdoo.transport.ui.pages.notifications.NotificationListItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NOTIFICATION = 1;

    private final List<NotificationListItem> items = new ArrayList<>();

    public void setData(List<EmployeeDocumentAlertDto> data) {

        items.clear();

        if (data == null || data.isEmpty()) {
            notifyDataSetChanged();
            return;
        }

        List<EmployeeDocumentAlertDto> vehicleItems = new ArrayList<>();
        List<EmployeeDocumentAlertDto> driverItems = new ArrayList<>();

        for (EmployeeDocumentAlertDto dto : data) {

            if (dto.getType() == null)
                continue;

            if (dto.getType().equalsIgnoreCase("Vozilo")) {
                vehicleItems.add(dto);
            }
            else if (dto.getType().equalsIgnoreCase("Vozač")) {
                driverItems.add(dto);
            }
        }

        if (!vehicleItems.isEmpty()) {

            items.add(
                    NotificationListItem.header("VAŠE ZADUŽENO VOZILO")
            );

            for (EmployeeDocumentAlertDto dto : vehicleItems) {
                items.add(
                        NotificationListItem.notification(dto)
                );
            }
        }

        if (!driverItems.isEmpty()) {

            items.add(
                    NotificationListItem.header("VOZAČ")
            );

            for (EmployeeDocumentAlertDto dto : driverItems) {
                items.add(
                        NotificationListItem.notification(dto)
                );
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_HEADER) {

            View v = inflater.inflate(
                    R.layout.item_notification_header,
                    parent,
                    false
            );

            return new HeaderVH(v);
        }

        View v = inflater.inflate(
                R.layout.item_notification,
                parent,
                false
        );

        return new NotificationVH(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position) {

        NotificationListItem item = items.get(position);

        if (holder instanceof HeaderVH) {

            HeaderVH vh = (HeaderVH) holder;
            vh.tvHeader.setText(item.header);
            return;
        }

        NotificationVH vh = (NotificationVH) holder;

        EmployeeDocumentAlertDto dto = item.notification;

        vh.tvType.setText(dto.getDocumentType());

        vh.tvStatus.setText(dto.getStatus());

        vh.tvDate.setText(
                "Važi do: " + dto.getValidToFormatted()
        );

        vh.tvDaysLeft.setText(
                dto.getDaysLeft() + " dana"
        );

        if ("Istekao".equals(dto.getStatus())
                || "Istekla".equals(dto.getStatus())) {

            vh.viewStatus.setBackgroundResource(
                    R.drawable.bg_dot_red
            );

            vh.tvDaysLeft.setBackgroundResource(
                    R.drawable.bg_badge_red
            );
        }
        else {

            vh.viewStatus.setBackgroundResource(
                    R.drawable.bg_dot_orange
            );

            vh.tvDaysLeft.setBackgroundResource(
                    R.drawable.bg_badge_orange
            );
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderVH extends RecyclerView.ViewHolder {

        TextView tvHeader;

        public HeaderVH(@NonNull View itemView) {
            super(itemView);

            tvHeader =
                    itemView.findViewById(R.id.tvHeader);
        }
    }

    static class NotificationVH extends RecyclerView.ViewHolder {

        TextView tvType;
        TextView tvStatus;
        TextView tvDate;
        TextView tvDaysLeft;
        View viewStatus;

        public NotificationVH(@NonNull View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.tvType);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDaysLeft = itemView.findViewById(R.id.tvDaysLeft);
            viewStatus = itemView.findViewById(R.id.viewStatus);
        }
    }
}