package com.programdoo.transport.ui.pages.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.programdoo.transport.data.models.dtos.employeesNotifications.EmployeeNotificationDto;
import com.programdoo.transport.databinding.FragmentNotificationsHistoryBinding;
import com.programdoo.transport.ui.adapters.NotificationsAdapter;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.viewmodels.employees.ExpiringDocumentsViewModel;
import com.programdoo.transport.utils.Constants;

import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotificationHistoryFragment extends BaseFragment {

    private FragmentNotificationsHistoryBinding binding;
    private ExpiringDocumentsViewModel viewModel;
    private NotificationsAdapter adapter;

    @Override
    public String TAG() {
        return Constants.FRAG_HISTORY_NOTIFICATIONS;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNotificationsHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        setupToolbar();

        viewModel = new ViewModelProvider(this).get(ExpiringDocumentsViewModel.class);

        adapter = new NotificationsAdapter();
        adapter.setHistoryMode(true);

        int employeeId = viewModel.getLoggedEmployeeId();

        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        binding.recyclerView.setAdapter(adapter);

        // LOAD HISTORY
        viewModel.loadCheckedNotifications(employeeId);

        // OBSERVE HISTORY
        viewModel.getCheckedNotifications()
                .observe(getViewLifecycleOwner(), list -> {

                    if (list == null) {
                        adapter.setData(Collections.emptyList());
                    } else {
                        adapter.setData(list);
                    }
                });
    }

    private void setupToolbar() {
        ((BaseActivity) requireActivity())
                .setToolbarTitle("Istorija obaveštenja");

        ((BaseActivity) requireActivity()).clearToolbarSubtitle();
        ((BaseActivity) requireActivity()).clearToolbarActions();
    }
}