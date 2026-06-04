package com.programdoo.transport.ui.pages.appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.requests.appointments.SearchAppointmentsParams;
import com.programdoo.transport.data.models.requests.companies.SearchOrgUnitsParams;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.databinding.FragmentAppointmentsCalendarBinding;
import com.programdoo.transport.ui.adapters.ToolbarAction;
import com.programdoo.transport.ui.adapters.appointments.CalendarAdapter;
import com.programdoo.transport.ui.adapters.appointments.CalendarEvent;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.CalendarUtil;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.appointments.AppointmentsCalendarViewModel;

import java.text.SimpleDateFormat;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AppointmentsCalendarFragment extends BaseFragment {
    private FragmentAppointmentsCalendarBinding binding;
    private AppointmentsCalendarViewModel viewModel;
    private CalendarAdapter adapter;
    private final Map<Integer, Chip> orgChipsById = new HashMap<>();
    private final Map<Integer, Chip> empChipsById = new HashMap<>();

    @Override
    public String TAG() {
        return Constants.FRAG_APPOINTMENTS_CALENDAR;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedStateInstance) {
        viewModel = new ViewModelProvider(this).get(AppointmentsCalendarViewModel.class);
        binding = FragmentAppointmentsCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedStateInstance) {
        super.onViewCreated(view, savedStateInstance);

        ((BaseActivity) requireActivity()).clearToolbarSubtitle();
        ((BaseActivity) requireActivity()).clearToolbarActions();
        ((BaseActivity) requireActivity()).setToolbarActions(List.of(
                new ToolbarAction(
                        R.id.action_search,
                        R.drawable.icon_refresh,
                        R.string.label_search,
                        MenuItem.SHOW_AS_ACTION_ALWAYS,
                        R.color.primaryLighter,
                        item -> {
                            binding.swipeRefresh.setRefreshing(true);
                            viewModel.refreshData();
                            return true;
                        }
                )
        ));

        adapter = new CalendarAdapter();
        styleCalendar();

        binding.swipeRefresh.setOnChildScrollUpCallback((parent, child) -> true);
        viewModel.getRefreshAppointmentsCompleted().observe(getViewLifecycleOwner(), o ->
                binding.swipeRefresh.setRefreshing(false));

        adapter.setRangeListener((start, end) -> {
            viewModel.setCurrentlyShowedDate(start);

            if (start.getMonthValue() != adapter.getCurrentMonth()
                    || viewModel.isOrgUnitChanged()
                    || viewModel.isEmployeeChanged()) {
                adapter.setCurrentMonth(start.getMonthValue());
                SearchAppointmentsParams searchParams = new SearchAppointmentsParams();
                searchParams.dateFrom = start.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
                searchParams.dateTo = start.with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay();
                if (viewModel.isOrgUnitChanged()) viewModel.setOrgUnitChanged(false);
                searchParams.orgUnitId = viewModel.getSelectedOrgUnit();

//                int employeeId = viewModel.getPreferences().getInt(Constants.KEY_EMPLOYEE_ID);
//                if (employeeId != 0) searchParams.trainerId = employeeId;
                if (viewModel.isEmployeeChanged()) viewModel.setEmployeeChanged(false);
                searchParams.trainerId = viewModel.getSelectedEmployee();

                viewModel.getAppointmentsRepository().searchAppointments(searchParams);
            }
        });

        viewModel.getAppointments().observe(getViewLifecycleOwner(), data -> {
            List<CalendarEvent> events = CalendarUtil.convert(requireContext(), data);
            adapter.submitList(events);
        });

        adapter.setClickListener((appt) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.ARG_APPOINTMENT_ID, appt.getId());
            NavigationUtil.navigate(this, R.id.fragmentFrame, new AppointmentInfoFragment(),
                    bundle);
        });

        adapter.setLongClickListener(appt -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.ARG_APPOINTMENT_ID, appt.getId());
            bundle.putBoolean(Constants.ARG_EDIT_MODE, true);
            NavigationUtil.navigate(this, R.id.fragmentFrame, new EditAppointmentFragment(),
                    bundle);
        });

        binding.fabNewAppointment.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.ARG_EDIT_MODE, false);
            NavigationUtil.navigate(this, R.id.fragmentFrame, new EditAppointmentFragment(), null);
        });

        setupChips();
    }

    private void setupChips() {
        viewModel.getOrgUnits().observe(getViewLifecycleOwner(), orgUnits -> {
            if (orgUnits != null && !orgUnits.isEmpty()) {
                binding.cgOrgUnitFilters.removeAllViews();

                for (OrgUnitDto unit: orgUnits) {
                    Chip chip = new Chip(getContext());
                    chip.setText(unit.getName());
                    orgChipsById.put(unit.getId(), chip);
                    UiUtil.chipSetup(getContext(), chip);

                    chip.setOnClickListener(v -> {
                        viewModel.setSelectedOrgUnit(unit.getId());
                        viewModel.setOrgUnitChanged(true);
                        adapter.onRangeChanged(viewModel.getCurrentlyShowedDate(), viewModel.getCurrentlyShowedDate());
                        SearchEmployeesParams searchEmployeesParams = new SearchEmployeesParams();
                        searchEmployeesParams.orgUnitId = unit.getId();
                        viewModel.getEmployeesRepository().searchEmployees(searchEmployeesParams);
                    });

                    binding.cgOrgUnitFilters.addView(chip);
                }

                /* dovuci inicijalne eventove */
                if (binding.cgOrgUnitFilters.getChildCount() > 0) {
                    if (viewModel.getSelectedOrgUnit() != null)
                        orgChipsById.get(viewModel.getSelectedOrgUnit()).performClick();
                    else
                        binding.cgOrgUnitFilters.getChildAt(0).performClick();
                }
            }
        });

        viewModel.getEmployees().observe(getViewLifecycleOwner(), employees -> {
            if (employees != null && !employees.isEmpty()) {
                binding.cgEmployeeFilters.removeAllViews();

                binding.cAllEmployees.setOnClickListener(v -> {
                   viewModel.setSelectedEmployee(null);
                   viewModel.setEmployeeChanged(true);
                   adapter.onRangeChanged(viewModel.getCurrentlyShowedDate(), viewModel.getCurrentlyShowedDate());
                });
                binding.cgEmployeeFilters.addView(binding.cAllEmployees);
                binding.cAllEmployees.setChecked(true);
//                empChipsById.clear();

                for (EmployeeDto employee: employees) {
                    Chip chip = new Chip(getContext());
                    chip.setText(employee.getFullNameShort());
                    empChipsById.put(employee.getId(), chip);
                    UiUtil.chipSetup(getContext(), chip);

                    chip.setOnClickListener(v -> {
                       viewModel.setSelectedEmployee(employee.getId());
                       viewModel.setEmployeeChanged(true);
                       adapter.onRangeChanged(viewModel.getCurrentlyShowedDate(), viewModel.getCurrentlyShowedDate());
                    });

                    binding.cgEmployeeFilters.addView(chip);
                }

//                if (binding.cgEmployeeFilters.getChildCount() > 0) {
//                    if (viewModel.getSelectedEmployee() != null)
//                        empChipsById.get(viewModel.getSelectedEmployee()).performClick();
//                }
            }
        });

        SearchOrgUnitsParams searchParams = new SearchOrgUnitsParams();
        int employeeId = viewModel.getPreferences().getInt(Constants.KEY_EMPLOYEE_ID);
        if (employeeId > 0) {
            searchParams.employeeId = employeeId;
        }
        viewModel.getCompaniesRepository().searchOrgUnits(searchParams);
    }
    /**
     * mora se pozvati nakon sto se inicijalizuje CalendarAdapter
     * posto se ovde postavlja adapter za kalendar
     */
    private void styleCalendar() {
        binding.calendar.setDateFormatter(calendar -> {

            SimpleDateFormat weekdayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd. MMMM", Locale.getDefault());

            return weekdayFormat.format(calendar.getTime()).toUpperCase()
                    + "\n"
                    + dayMonthFormat.format(calendar.getTime());
        });
        binding.calendar.setTimeFormatter(hour -> StringUtil.toString(hour) + "h");

        binding.calendar.setAdapter(adapter);
        binding.calendar.setMinHour(6);
        binding.calendar.setMaxHour(23);
    }
}
