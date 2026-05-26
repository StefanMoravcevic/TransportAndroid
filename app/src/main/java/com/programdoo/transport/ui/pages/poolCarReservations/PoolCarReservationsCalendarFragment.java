package com.programdoo.transport.ui.pages.poolCarReservations;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.databinding.FragmentPoolcarCalendarBinding;
import com.programdoo.transport.ui.adapters.appointments.CalendarAdapter;
import com.programdoo.transport.ui.adapters.appointments.CalendarEvent;
import com.programdoo.transport.ui.adapters.appointments.PoolCarCalendarAdapter;
import com.programdoo.transport.ui.adapters.poolcarreservations.PoolCarCalendarEvent;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.viewmodels.poolCarReservations.PoolCarReservationCalendarViewModel;
import com.programdoo.transport.utils.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PoolCarReservationsCalendarFragment extends BaseFragment {

    private FragmentPoolcarCalendarBinding binding;
    private PoolCarReservationCalendarViewModel viewModel;
    private PoolCarCalendarAdapter adapter;

    @Override
    public String TAG() {
        return "FRAG_POOL_CAR_CALENDAR";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this)
                .get(PoolCarReservationCalendarViewModel.class);

        binding = FragmentPoolcarCalendarBinding
                .inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        setupToolbar();
        setupCalendar();
        setupObservers();
        setupListeners();

        loadCurrentMonth();
        getParentFragmentManager().setFragmentResultListener(
                "pool_car_saved",
                this,
                (key, bundle) -> {

                    boolean refresh = bundle.getBoolean("refresh_calendar", false);

                    if (refresh) {
                        viewModel.refreshData(); // 🔥 REFRESH
                    }
                }
        );
    }

    private void setupToolbar() {
        ((BaseActivity) requireActivity())
                .setToolbarTitle("Pool Car Calendar");

        ((BaseActivity) requireActivity()).clearToolbarSubtitle();
        ((BaseActivity) requireActivity()).clearToolbarActions();
    }

    // ---------------- CALENDAR ----------------

    private void setupCalendar() {

        adapter = new PoolCarCalendarAdapter();

        binding.weekView.setAdapter(adapter);

        binding.weekView.setNumberOfVisibleDays(1);
        binding.weekView.setMinHour(6);
        binding.weekView.setMaxHour(23);

        adapter.setRangeListener((start, end) -> {
            loadRange(start.atStartOfDay(), end.atStartOfDay());
        });

        adapter.setClickListener(event -> {
            // open details
        });

        adapter.setLongClickListener(event -> {
            // edit
        });

    }

    // ---------------- OBSERVER ----------------

    private void setupObservers() {

        viewModel.getPoolCarReservations()
                .observe(getViewLifecycleOwner(), this::renderEvents);
    }

    // ---------------- RENDER ----------------

    private void renderEvents(List<PoolCarReservationDto> data) {

        List<PoolCarCalendarEvent> events = new ArrayList<>();

        for (PoolCarReservationDto r : data) {

            if (r.getDateFrom() == null || r.getDateTo() == null)
                continue;

            events.add(new PoolCarCalendarEvent(r));
        }

        adapter.submitList(events);
    }

    // ---------------- LISTENERS ----------------

    private void setupListeners() {

        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.refreshData();
        });

        binding.fabNewReservation.setOnClickListener(v -> openCreateReservation());
    }

    // ---------------- LOAD ----------------

    private void loadCurrentMonth() {
        loadRange(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay()
        );
    }

    private void loadRange(LocalDateTime start, LocalDateTime end) {

        SearchPoolCarReservationParams params =
                new SearchPoolCarReservationParams();

        params.dateFrom = start;
        params.dateTo = end;

        viewModel.getRepository()
                .searchPoolCarReservations(params);
    }

    private void openCreateReservation() {

        CreatePoolCarReservationFragment fragment =
                new CreatePoolCarReservationFragment();

        requireActivity().setTitle("Create Pool Car Reservation");

        Bundle args = new Bundle();

        args.putLong(
                "employeeId",
                viewModel.getSession().getEntityId()
        );

        fragment.setArguments(args);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentFrame, fragment)
                .addToBackStack(null)
                .commit();
    }

}