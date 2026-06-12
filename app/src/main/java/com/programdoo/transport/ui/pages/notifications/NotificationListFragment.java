package com.programdoo.transport.ui.pages.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.employeesNotifications.EmployeeNotificationDto;
import com.programdoo.transport.databinding.FragmentNotificationsBinding;
import com.programdoo.transport.ui.adapters.NotificationsAdapter;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.viewmodels.employees.ExpiringDocumentsViewModel;
import com.programdoo.transport.utils.Constants;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotificationListFragment extends BaseFragment {

    private FragmentNotificationsBinding binding;
    private ExpiringDocumentsViewModel viewModel;
    private NotificationsAdapter adapter;

    @Override
    public String TAG() {
        return Constants.FRAG_NOTIFICATIONS;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        setupToolbar();

        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(200);
        animator.setAddDuration(200);
        binding.recyclerView.setItemAnimator(animator);

        viewModel = new ViewModelProvider(this).get(ExpiringDocumentsViewModel.class);

        adapter = new NotificationsAdapter();

        int employeeId = viewModel.getLoggedEmployeeId();

        adapter.setListener(notificationId -> {
            viewModel.markNotificationAsRead(notificationId, employeeId);
        });
        adapter.setHistoryMode(false);

        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        binding.recyclerView.setAdapter(adapter);

        viewModel.loadExpiringDocuments(employeeId);
        viewModel.loadExpiringNotificationDocuments(employeeId);

        binding.btnMarkAllRead.setOnClickListener(v -> {
            viewModel.markAllNotificationsAsRead(employeeId);
        });

        binding.btnNotificationHistories.setOnClickListener(v -> {

            NotificationHistoryFragment fragment = new NotificationHistoryFragment();

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentFrame, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        viewModel.getExpiringDocumentsNotifications()
                .observe(getViewLifecycleOwner(), list -> {

                    adapter.setData(list);

                    int count = 0;

                    if (list != null) {
                        for (EmployeeNotificationDto dto : list) {
                            if (!dto.isRead()) {
                                count++;
                            }
                        }
                    }

                    viewModel.setUnreadCount(count);
                });
    }

    private void setupToolbar() {
        ((BaseActivity) requireActivity())
                .setToolbarTitle(getString(R.string.label_notifications));

        ((BaseActivity) requireActivity()).clearToolbarSubtitle();
        ((BaseActivity) requireActivity()).clearToolbarActions();
    }
}