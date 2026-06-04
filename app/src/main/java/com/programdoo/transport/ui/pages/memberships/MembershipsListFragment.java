package com.programdoo.transport.ui.pages.memberships;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.dtos.memberships.MembershipDto;
import com.programdoo.transport.data.models.requests.companies.SearchOrgUnitsParams;
import com.programdoo.transport.data.models.requests.memberships.SearchMembershipsParams;
import com.programdoo.transport.databinding.FragmentMembershipsListBinding;
import com.programdoo.transport.ui.adapters.MembershipsRecyclerViewAdapter;
import com.programdoo.transport.ui.callbacks.SwipeDeleteCallback;
import com.programdoo.transport.ui.decorators.ListItemDecoration;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.memberships.MembershipsViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MembershipsListFragment extends BaseFragment {

    private FragmentMembershipsListBinding binding;
    private MembershipsRecyclerViewAdapter adapter;
    private MembershipsViewModel viewModel;

    @Override
    public String TAG() {
        return Constants.FRAG_MEMBERSHIP_LIST;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(MembershipsViewModel.class);
        binding = FragmentMembershipsListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MembershipsRecyclerViewAdapter(requireContext(), new ArrayList<>());
        adapter.setUnselectedIconTint(requireContext(), R.color.primaryLighter);

        binding.rvMemberships.setAdapter(adapter);
        binding.rvMemberships.smoothScrollToPosition(0);
        binding.rvMemberships.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.rvMemberships.addItemDecoration(new ListItemDecoration(getContext(), getContext().getColor(R.color.primaryLighter), 1));

        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.ARG_TRAINEE_ID)) {
                int traineeId = getArguments().getInt(Constants.ARG_TRAINEE_ID);
                viewModel.setTraineeId(traineeId);
                viewModel.getTraineesRepository().getTrainee(traineeId);
            }
        }


        ((BaseActivity) requireActivity()).clearToolbarSubtitle();

        binding.swipeRefresh.setOnRefreshListener(() -> viewModel.refreshData());
        binding.swipeRefresh.setColorSchemeResources(R.color.primary, R.color.secondary);
        viewModel.getRefreshMembershipsCompleted().observe(getViewLifecycleOwner(), o ->
                binding.swipeRefresh.setRefreshing(false));

        adapter.setOnClickListener((v, position, membership) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.ARG_MEMBERSHIP_ID, membership.getId());
            NavigationUtil.navigate(this, R.id.fragmentFrame, new MembershipInfoFragment(), bundle);
        });

        if (viewModel.getSession().isUserStaff()) {
            adapter.setOnLongClickListener((v, position, membership) -> {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.ARG_MEMBERSHIP_ID, membership.getId());
                NavigationUtil.navigate(this, R.id.fragmentFrame, new EditMembershipFragment(), bundle);
            });
        }

        binding.NewMembership.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            int traineeId = viewModel.getTraineeId();
            bundle.putInt(Constants.ARG_TRAINEE_ID, traineeId);

            NavigationUtil.navigate(this, R.id.fragmentFrame, new EditMembershipFragment(), bundle);
        });

        if (viewModel.getSession().isUserStaff()) {
            UiUtil.enableSwipeDelete(binding.rvMemberships, new SwipeDeleteCallback(getContext()) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getBindingAdapterPosition();
                    MembershipsRecyclerViewAdapter adapter = (MembershipsRecyclerViewAdapter) binding.rvMemberships.getAdapter();
                    MembershipDto membership = adapter.getItemAt(position);
                    viewModel.deleteMembership(membership.getId(), viewModel.getSession().getUserId());
                }
            });
        }

        SearchMembershipsParams params = new SearchMembershipsParams();
        params.traineeId = viewModel.getTraineeId();
        viewModel.getMembershipsRepository().searchMemberships(params);

        viewModel.getMemberships().observe(getViewLifecycleOwner(), memberships
                -> adapter.setData(memberships));
        viewModel.getTrainee().observe(getViewLifecycleOwner(), trainee
                -> ((BaseActivity) requireActivity()).setToolbarSubtitle(trainee.getFullName()));

        setupChips();
    }

    public void setupChips() {
        binding.cAll.setChecked(true);
        binding.cAll.setOnClickListener(v -> {
            viewModel.setActive(null);
            SearchMembershipsParams params = new SearchMembershipsParams();
            params.orgUnitId = viewModel.getCurrentOrgUnit();
            params.traineeId = viewModel.getSession().getEntityId();
            viewModel.getMembershipsRepository().searchMemberships(params);
        });
        binding.cActive.setOnClickListener(v -> {
            viewModel.setActive(true);
            SearchMembershipsParams params = new SearchMembershipsParams();
            params.active = viewModel.getActive();
            params.orgUnitId = viewModel.getCurrentOrgUnit();
            params.traineeId = viewModel.getSession().getEntityId();
            viewModel.getMembershipsRepository().searchMemberships(params);
        });
        binding.cInactive.setOnClickListener(v -> {
            viewModel.setActive(false);
            SearchMembershipsParams params = new SearchMembershipsParams();
            params.active = viewModel.getActive();
            params.orgUnitId = viewModel.getCurrentOrgUnit();
            params.traineeId = viewModel.getSession().getEntityId();
            viewModel.getMembershipsRepository().searchMemberships(params);
        });

        if (viewModel.getSession().isUserStaff()) {
            binding.cAllOrgUnits.setChecked(true);
            binding.cAllOrgUnits.setOnClickListener(v -> {
                viewModel.setCurrentOrgUnit(null);
                SearchMembershipsParams params = new SearchMembershipsParams();
                params.active = viewModel.getActive();
                params.traineeId = viewModel.getSession().getEntityId();
                viewModel.getMembershipsRepository().searchMemberships(params);
            });

            viewModel.getOrgUnits().observe(getViewLifecycleOwner(), orgUnits -> {
                if (orgUnits != null && !orgUnits.isEmpty()) {
                    binding.cgOrgUnitFilters.removeAllViews();
                    binding.cgOrgUnitFilters.addView(binding.cAllOrgUnits);

                    for (OrgUnitDto unit : orgUnits) {
                        Chip chip = new Chip(getContext());
                        chip.setText(unit.getName());
                        UiUtil.chipSetup(getContext(), chip);

                        chip.setOnClickListener(v -> {
                            viewModel.setCurrentOrgUnit(unit.getId());
                            SearchMembershipsParams params = new SearchMembershipsParams();
                            params.orgUnitId = unit.getId();
                            params.active = viewModel.getActive();
                            viewModel.getMembershipsRepository().searchMemberships(params);
                        });

                        binding.cgOrgUnitFilters.addView(chip);
                    }
                }
            });

            viewModel.getCompaniesRepository().searchOrgUnits(new SearchOrgUnitsParams());
        }
        else
            binding.cgOrgUnitFilters.removeAllViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}