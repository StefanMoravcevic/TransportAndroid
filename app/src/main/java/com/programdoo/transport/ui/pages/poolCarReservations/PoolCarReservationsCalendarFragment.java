package com.programdoo.transport.ui.pages.poolCarReservations;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.programdoo.transport.utils.UiUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PoolCarReservationsCalendarFragment extends BaseFragment {

    private FragmentPoolcarCalendarBinding binding;
    private PoolCarReservationCalendarViewModel viewModel;
    private GestureDetector gestureDetector;
    private PoolCarCalendarAdapter adapter;

    private LocalDate selectedDate = LocalDate.now();

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
        binding.tvDateFrom.setText(
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
        selectedDate = LocalDate.now();
        loadSelectedDay();
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
                        viewModel.refreshData();
                    }
                }
        );

        UiUtil.datePickerSetupCallback(
                this,
                binding.tvDateFrom,
                () -> {

                    String value = binding.tvDateFrom.getText().toString();

                    selectedDate = LocalDate.parse(
                            value,
                            java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    );

                    loadSelectedDay();
                }
        );
    }

    private void setupToolbar() {
        ((BaseActivity) requireActivity())
                .setToolbarTitle(getString(R.string.label_PoolCar));

        ((BaseActivity) requireActivity()).clearToolbarSubtitle();
        ((BaseActivity) requireActivity()).clearToolbarActions();
    }

    private void loadSelectedDay() {

        LocalDateTime start = selectedDate.atStartOfDay();
        LocalDateTime end = selectedDate.plusDays(1).atStartOfDay();

        SearchPoolCarReservationParams params =
                new SearchPoolCarReservationParams();

        params.dateFrom = start;
        params.dateTo = end;

        viewModel.getRepository()
                .searchPoolCarReservations(params);

        moveCalendarToSelectedDate();
    }

    private void moveCalendarToSelectedDate() {

        Calendar cal = Calendar.getInstance();
        cal.set(
                selectedDate.getYear(),
                selectedDate.getMonthValue() - 1,
                selectedDate.getDayOfMonth()
        );

        binding.weekView.goToDate(cal);
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


       // binding.weekView.setOnTouchListener((v, event) -> {

            //gestureDetector.onTouchEvent(event);

           // return false; // BITNO da WeekView i dalje radi scroll
        //});
    }

    /*private void handleClickAtPosition(MotionEvent event) {

        int startHour = 6;

        int hourHeightPx = binding.weekView.getHourHeight();

        int[] location = new int[2];
        binding.weekView.getLocationOnScreen(location);

        // 🔥 stabilan Y bez scrollY hacka
        float y = event.getRawY() - location[1];

        int hourOffset = (int) (y / hourHeightPx);

        int hour = startHour + hourOffset;

        int minute = (int) (((y % hourHeightPx) / (float) hourHeightPx) * 60);

        // clamp
        if (hour < 6) hour = 6;
        if (hour > 23) hour = 23;

        Log.d("CAL_DEBUG", "y=" + y);
        Log.d("CAL_DEBUG", "hourOffset=" + hourOffset);
        Log.d("CAL_DEBUG", "hour=" + hour);
        Log.d("CAL_DEBUG", "minute=" + minute);

        LocalDateTime from = selectedDate.atTime(hour, 0);
        LocalDateTime to = selectedDate.atTime(hour + 1, 0);

        openCreateReservationFromPress(from, to);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }*/

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

        binding.fabNewReservation.setOnClickListener(v ->
                openCreateReservation(LocalDate.now())
        );
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

    private void openCreateReservation(LocalDate date) {

        CreatePoolCarReservationFragment fragment =
                new CreatePoolCarReservationFragment();

        Bundle args = new Bundle();

        args.putLong(
                "employeeId",
                viewModel.getSession().getEntityId()
        );

        args.putString(
                "selectedDate",
                date.toString()
        );

        fragment.setArguments(args);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentFrame, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openCreateReservationFromPress(LocalDateTime from, LocalDateTime to) {

        CreatePoolCarReservationFragment fragment =
                new CreatePoolCarReservationFragment();

        Bundle args = new Bundle();

        args.putLong("employeeId", viewModel.getSession().getEntityId());
        args.putString("from", from.toString());
        args.putString("to", to.toString());

        fragment.setArguments(args);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentFrame, fragment)
                .addToBackStack(null)
                .commit();
    }


}