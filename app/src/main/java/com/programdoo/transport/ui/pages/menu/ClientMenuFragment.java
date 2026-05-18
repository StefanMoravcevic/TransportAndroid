package com.programdoo.transport.ui.pages.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.databinding.FragmentClientMenuBinding;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.pages.appointments.AppointmentsActivity;
import com.programdoo.transport.ui.pages.login.LoginActivity;
import com.programdoo.transport.ui.pages.memberships.MembershipActivity;
import com.programdoo.transport.ui.pages.settings.SettingsActivity;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.MenuViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ClientMenuFragment extends BaseFragment {
    private FragmentClientMenuBinding binding;
    private MenuViewModel viewModel;
    @Override
    public String TAG() {
        return Constants.FRAG_CLIENT_MENU;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentClientMenuBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        ((BaseActivity) requireActivity()).setToolbarTitle(getString(R.string.label_menu));
        ((BaseActivity) requireActivity()).setToolbarSubtitle(viewModel.getSession().getUser().getFullName());

        binding.tvMeasurements.setOnClickListener(v
                -> UiUtil.makeToast(requireActivity(), requireContext(), getString(R.string.msg_not_implemented)));

        binding.tvProgress.setOnClickListener(v
                -> UiUtil.makeToast(requireActivity(), requireContext(), getString(R.string.msg_not_implemented)));

        binding.tvAppointments.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), AppointmentsActivity.class);
            i.putExtra(Constants.ARG_TRAINEE_ID, viewModel.getSession().getUser().getEntityId());
            startActivity(i);
        });

        binding.tvMemberships.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), MembershipActivity.class);
            startActivity(i);
        });

        binding.tvSettings.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), SettingsActivity.class);
            startActivity(i);
        });

        binding.tvLogout.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), LoginActivity.class);
            viewModel.getPreferences().clearTokens();
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }
}