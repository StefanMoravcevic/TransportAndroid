package com.programdoo.transport.ui.pages.poolCarReservations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.requests.vehicleEngagements.SearchVehicleEngagementsParams;
import com.programdoo.transport.databinding.FragmentMembershipsListBinding;
import com.programdoo.transport.databinding.FragmentPoolCarReservationListBinding;
import com.programdoo.transport.ui.adapters.appointments.PoolCarCalendarAdapter;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.viewmodels.memberships.MembershipsViewModel;
import com.programdoo.transport.ui.viewmodels.poolCarReservations.PoolCarReservationListViewModel;
import com.programdoo.transport.ui.viewmodels.vehicleEngagements.VehicleEngagementsViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.val;

@AndroidEntryPoint
public class PoolCarReservationsListFragment extends BaseFragment {

    private FragmentPoolCarReservationListBinding binding;

    private boolean isPoolCarSelected = true;

    private com.programdoo.transport.ui.adapters.PoolCarReservationListAdapter adapter;

    private PoolCarReservationListViewModel viewModel;
    private VehicleEngagementsViewModel viewModelVehicleEngagements;

    private CompositeDisposable disposables = new CompositeDisposable();
    @Override
    public String TAG() {
        return "FRAG_POOL_CAR_LIST";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(PoolCarReservationListViewModel.class);
        viewModelVehicleEngagements = new ViewModelProvider(this).get(VehicleEngagementsViewModel.class);
        binding = FragmentPoolCarReservationListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        observeData();
        setupChips();
        binding.rvPoolCarReservations.addItemDecoration(
                new DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                )
        );

    }

    private void setupChips() {

        binding.cgAssignmentFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {

            if (checkedIds.contains(R.id.cAllAssignments)) {

                isPoolCarSelected = true;
                loadPoolCar();

            } else if (checkedIds.contains(R.id.cActiveAssignments)) {

                isPoolCarSelected = false;
                loadVehicleEngagements();
            }
        });
    }

    private void loadPoolCar() {

        int employeeId = viewModel.getSession().getEntityId();

        SearchPoolCarReservationParams params = new SearchPoolCarReservationParams();
        params.setEmployeeId(employeeId);

        viewModel.searchPoolCarReservations(params);
    }

    private void setupRecyclerView() {
        adapter = new com.programdoo.transport.ui.adapters.PoolCarReservationListAdapter();

        binding.rvPoolCarReservations.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        binding.rvPoolCarReservations.setAdapter(adapter);
    }

    private void loadData() {

        int employeeId = viewModel.getSession().getEntityId();

        SearchPoolCarReservationParams params = new SearchPoolCarReservationParams();
        params.setEmployeeId(employeeId);

        viewModel.searchPoolCarReservations(params);
    }

    private void loadVehicleEngagements() {

        int employeeId = viewModelVehicleEngagements.getSession().getEntityId();

        SearchVehicleEngagementsParams params = new SearchVehicleEngagementsParams();
        params.setEmployeeId(employeeId);

        viewModelVehicleEngagements.searchVehicleEngagements(params);
    }


    @SuppressLint("AutoDispose")
    private void observeData() {

        disposables.add(
                viewModel.getPoolCarReservations()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {

                            adapter.submitList(
                                    viewModel.mapPoolCar(response.getPayload())
                            );

                        }, throwable -> {


                        })
        );

        disposables.add(
                viewModelVehicleEngagements.getVehicleEngagements()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {

                            if (isPoolCarSelected) return;

                            adapter.submitList(
                                    viewModelVehicleEngagements.mapVehicleEngagement(response.getPayload())
                            );

                        })
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
        binding = null;
    }
}
