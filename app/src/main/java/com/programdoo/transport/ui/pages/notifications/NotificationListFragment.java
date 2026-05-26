package com.programdoo.transport.ui.pages.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.programdoo.transport.databinding.FragmentNotificationsBinding;
import com.programdoo.transport.ui.adapters.NotificationsAdapter;
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

        // 1. ViewModel
        viewModel = new ViewModelProvider(this).get(ExpiringDocumentsViewModel.class);

        // 2. Adapter
        adapter = new NotificationsAdapter();

        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        binding.recyclerView.setAdapter(adapter);

        // 3. Load podataka
        int employeeId = viewModel.getLoggedEmployeeId();
        viewModel.loadExpiringDocuments(employeeId);

        // 4. Observe
        viewModel.getExpiringDocuments().observe(getViewLifecycleOwner(), list -> {
            adapter.setData(list);
        });
    }
}
