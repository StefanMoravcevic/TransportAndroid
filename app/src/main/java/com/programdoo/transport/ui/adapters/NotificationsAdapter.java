package com.programdoo.transport.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.employeesNotifications.EmployeeNotificationDto;
import com.programdoo.transport.ui.pages.notifications.NotificationListItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NOTIFICATION = 1;

    private boolean isHistoryMode = false;

    public void setHistoryMode(boolean historyMode) {
        this.isHistoryMode = historyMode;
    }

    // -------------------------
    // LIST DATA
    // -------------------------
    private final List<NotificationListItem> items = new ArrayList<>();

    // -------------------------
    // LISTENER
    // -------------------------
    public interface NotificationListener {
        void onMarkAsRead(int notificationId);
    }

    private NotificationListener listener;

    public void setListener(NotificationListener listener) {
        this.listener = listener;
    }

    // -------------------------
    // DATA SET
    // -------------------------
    public void setData(List<EmployeeNotificationDto> data) {

        items.clear();

        if (data == null || data.isEmpty()) {
            notifyDataSetChanged();
            return;
        }

        List<EmployeeNotificationDto> vehicleItems = new ArrayList<>();
        List<EmployeeNotificationDto> driverItems = new ArrayList<>();

        for (EmployeeNotificationDto dto : data) {

            if (dto.getType() == null)
                continue;

            if (dto.getType().equalsIgnoreCase("Vozilo")) {
                vehicleItems.add(dto);
            } else if (dto.getType().equalsIgnoreCase("Vozač")) {
                driverItems.add(dto);
            }
        }

        if (!vehicleItems.isEmpty()) {

            items.add(NotificationListItem.header("VAŠE ZADUŽENO VOZILO"));

            for (EmployeeNotificationDto dto : vehicleItems) {
                items.add(NotificationListItem.notification(dto));
            }
        }

        if (!driverItems.isEmpty()) {

            items.add(NotificationListItem.header("VOZAČ"));

            for (EmployeeNotificationDto dto : driverItems) {
                items.add(NotificationListItem.notification(dto));
            }
        }

        notifyDataSetChanged();
    }

    // -------------------------
    // VIEW TYPES
    // -------------------------
    @Override
    public int getItemViewType(int position) {
        return items.get(position).viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_HEADER) {

            View v = inflater.inflate(R.layout.item_notification_header, parent, false);
            return new HeaderVH(v);
        }

        View v = inflater.inflate(R.layout.item_notification, parent, false);
        return new NotificationVH(v);
    }

    // -------------------------
    // BIND
    // -------------------------
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NotificationListItem item = items.get(position);

        if (holder instanceof HeaderVH) {

            ((HeaderVH) holder).tvHeader.setText(item.header);
            return;
        }

        NotificationVH vh = (NotificationVH) holder;

        EmployeeNotificationDto dto = item.notification;

        vh.tvType.setText(dto.getTitle());
        vh.tvStatus.setText(dto.getStatus());

        vh.tvDate.setText("Važi do: " + dto.getValidToFormatted());

        // STATUS DOT
        if ("Istekao".equals(dto.getStatus())
                || "Istekla".equals(dto.getStatus())) {

            vh.viewStatus.setBackgroundResource(R.drawable.bg_dot_red);

        } else {
            vh.viewStatus.setBackgroundResource(R.drawable.bg_dot_orange);
        }
        if (isHistoryMode) {
            vh.ivMarkRead.setVisibility(View.GONE);
        } else {
            vh.ivMarkRead.setVisibility(View.VISIBLE);
        }
        // -------------------------
        // READ UI STATE
        // -------------------------
        vh.viewStatus.setVisibility(
                dto.isRead() ? View.INVISIBLE : View.VISIBLE
        );

        // -------------------------
        // CLICK = MARK AS READ
        // -------------------------
        vh.ivMarkRead.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            NotificationListItem clickedItem = items.get(pos);
            EmployeeNotificationDto clickedDto = clickedItem.notification;

            if (clickedDto.isRead()) return;

            clickedDto.setRead(true);
            notifyItemChanged(pos);

            if (listener != null) {
                listener.onMarkAsRead(clickedDto.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // -------------------------
    // VIEW HOLDERS
    // -------------------------
    static class HeaderVH extends RecyclerView.ViewHolder {

        TextView tvHeader;

        public HeaderVH(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);
        }
    }

    static class NotificationVH extends RecyclerView.ViewHolder {
        ImageView ivMarkRead;
        TextView tvType;
        TextView tvStatus;
        TextView tvDate;
        View viewStatus;

        public NotificationVH(@NonNull View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.tvType);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
            viewStatus = itemView.findViewById(R.id.viewStatus);
            ivMarkRead = itemView.findViewById(R.id.ivMarkRead);

        }
    }
}