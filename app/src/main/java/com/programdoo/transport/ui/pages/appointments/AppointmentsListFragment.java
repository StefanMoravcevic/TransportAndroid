package com.programdoo.transport.ui.pages.appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.requests.appointments.SearchAppointmentsParams;
import com.programdoo.transport.data.models.requests.companies.SearchOrgUnitsParams;
import com.programdoo.transport.databinding.FragmentAppointmentsListBinding;
import com.programdoo.transport.ui.adapters.AppointmentsRecyclerViewAdapter;
import com.programdoo.transport.ui.decorators.ListItemDecoration;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.appointments.AppointmentsCalendarViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AppointmentsListFragment extends BaseFragment {
    private AppointmentsCalendarViewModel viewModel;
    private FragmentAppointmentsListBinding binding;
    private AppointmentsRecyclerViewAdapter adapter;

    @Override
    public String TAG() {
        return Constants.FRAG_APPOINTMENTS_LIST;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AppointmentsCalendarViewModel.class);
        /* build-a binding. svaki layout ce da generise binding klasu. ako je ime layout-a
         * activity_x (fragment_x), generisace binding ActivityXBinding (FragmentXBinding).
         * preko binding-a moze da se pristupi svim ui komponentama iz xml-a. */
        binding = FragmentAppointmentsListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new AppointmentsRecyclerViewAdapter(requireContext(), new ArrayList<>());
        adapter.setUnselectedIconTint(requireContext(), R.color.primaryLighter);
        binding.rvAppointments.setAdapter(adapter);
        binding.rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAppointments.smoothScrollToPosition(0);

        binding.rvAppointments.addItemDecoration(new ListItemDecoration(getContext(), getContext().getColor(R.color.primaryLighter), 1));

        ((BaseActivity) requireActivity()).setToolbarSubtitle(viewModel.getSession().getUser().getFullName());

        binding.swipeLayout.setOnRefreshListener(() -> viewModel.refreshData());
        binding.swipeLayout.setColorSchemeResources(R.color.primary, R.color.secondary);
        viewModel.getRefreshAppointmentsCompleted().observe(getViewLifecycleOwner(),
                o -> binding.swipeLayout.setRefreshing(false));

        adapter.setOnClickListener((v,position,appointment) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.ARG_APPOINTMENT_ID, appointment.getId());
            NavigationUtil.navigate(this, R.id.fragmentFrame, new AppointmentInfoFragment(), bundle);
        });
        viewModel.getAppointments().observe(getViewLifecycleOwner(), data
                -> adapter.setData(data));
        viewModel.getToastEvent().observe(getViewLifecycleOwner(), msgId
                -> UiUtil.makeToast(requireActivity(), requireContext(), getString(msgId)));

        setupChips();
    }

    private void setupChips() {
        viewModel.getOrgUnits().observe(getViewLifecycleOwner(), orgUnits -> {
            binding.cAllOrgUnits.setOnClickListener(v -> {
                viewModel.setSelectedOrgUnit(null);
                SearchAppointmentsParams params = new SearchAppointmentsParams();
                params.traineeId = viewModel.getSession().getEntityId();
                params.finished = viewModel.getFinished();
                params.cancelled = viewModel.getCancelled();
                viewModel.getAppointmentsRepository().searchAppointments(params);
            });

            binding.cAllOrgUnits.setChecked(true);

            if (orgUnits != null && !orgUnits.isEmpty()) {
                binding.cgOrgUnitFilters.removeAllViews();
                binding.cgOrgUnitFilters.addView(binding.cAllOrgUnits);

                for (OrgUnitDto unit: orgUnits) {
                    Chip chip = new Chip(getContext());
                    chip.setText(unit.getName());
                    UiUtil.chipSetup(getContext(), chip);

                    chip.setOnClickListener(v -> {
                        viewModel.setSelectedOrgUnit(unit.getId());
                        SearchAppointmentsParams params = new SearchAppointmentsParams();
                        params.orgUnitId = unit.getId();
                        params.traineeId = viewModel.getSession().getEntityId();
                        params.finished = viewModel.getFinished();
                        params.cancelled = viewModel.getCancelled();
                        viewModel.getAppointmentsRepository().searchAppointments(params);
                    });

                    binding.cgOrgUnitFilters.addView(chip);
                }

            }
        });

        SearchOrgUnitsParams orgParams = new SearchOrgUnitsParams();
        orgParams.traineeId = viewModel.getSession().getEntityId();
        viewModel.getCompaniesRepository().searchOrgUnits(orgParams);

        binding.cAllAppointments.setOnClickListener(v -> {
            viewModel.setFinished(null);
            viewModel.setCancelled(null);
            SearchAppointmentsParams params = new SearchAppointmentsParams();
            params.traineeId = viewModel.getSession().getEntityId();
            params.orgUnitId = viewModel.getSelectedOrgUnit();
            viewModel.getAppointmentsRepository().searchAppointments(params);
        });
        binding.cFinished.setOnClickListener(v -> {
            viewModel.setFinished(true);
            SearchAppointmentsParams params = new SearchAppointmentsParams();
            params.traineeId = viewModel.getSession().getEntityId();
            params.orgUnitId = viewModel.getSelectedOrgUnit();
            params.finished = viewModel.getFinished();
            viewModel.getAppointmentsRepository().searchAppointments(params);
        });
        binding.cCancelled.setOnClickListener(v -> {
            viewModel.setCancelled(true);
            SearchAppointmentsParams params = new SearchAppointmentsParams();
            params.traineeId = viewModel.getSession().getEntityId();
            params.orgUnitId = viewModel.getSelectedOrgUnit();
            params.cancelled = viewModel.getCancelled();
            viewModel.getAppointmentsRepository().searchAppointments(params);
        });
        binding.cScheduled.setOnClickListener(v -> {
            viewModel.setCancelled(false);
            viewModel.setFinished(false);
            SearchAppointmentsParams params = new SearchAppointmentsParams();
            params.traineeId = viewModel.getSession().getEntityId();
            params.orgUnitId = viewModel.getSelectedOrgUnit();
            params.cancelled = viewModel.getCancelled();
            params.finished = viewModel.getFinished();
            viewModel.getAppointmentsRepository().searchAppointments(params);
        });

        binding.cScheduled.setChecked(true);
        binding.cScheduled.performClick();
    }
}
