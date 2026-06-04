package com.programdoo.transport.ui.pages.appointments;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.requests.companies.SearchOrgUnitsParams;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.data.models.requests.trainees.SearchTraineesParams;
import com.programdoo.transport.databinding.FragmentEditAppointmentBinding;
import com.programdoo.transport.ui.adapters.EmployeesRecyclerViewAdapter;
import com.programdoo.transport.ui.adapters.OrgUnitsRecyclerViewAdapter;
import com.programdoo.transport.ui.adapters.ToolbarAction;
import com.programdoo.transport.ui.adapters.TraineesRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.pages.trainees.EditTraineeBasicInfoFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.utils.TimeUtil;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.appointments.EditAppointmentViewModel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditAppointmentFragment extends BaseFragment {
    private EditAppointmentViewModel viewModel;
    private FragmentEditAppointmentBinding binding;
    private TraineesRecyclerViewAdapter traineesAdapter;
    private EmployeesRecyclerViewAdapter employeesAdapter;
    private OrgUnitsRecyclerViewAdapter orgUnitsAdapter;

    @Override
    public String TAG() {
        return Constants.FRAG_EDIT_APPOINTMENT;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            @Nullable Bundle savedStateInstance) {
        viewModel = new ViewModelProvider(this).get(EditAppointmentViewModel.class);
        binding = FragmentEditAppointmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedStateInstance) {
        ((BaseActivity) requireActivity()).clearToolbarSubtitle();

        traineesAdapter = new TraineesRecyclerViewAdapter(requireContext(), new ArrayList<>());
        traineesAdapter.setIconTint(requireContext(), R.color.tertiaryLighter, R.color.primaryLighter);
        employeesAdapter = new EmployeesRecyclerViewAdapter(requireContext(), new ArrayList<>());
        orgUnitsAdapter = new OrgUnitsRecyclerViewAdapter(requireContext(), new ArrayList<>());

        UiUtil.selectSetup(
                requireContext(),
                traineesAdapter,
                binding.selTrainee,
                (v, position, trainee) -> binding.selTrainee.toggleSelected(position));
        UiUtil.selectSetup(
                requireContext(),
                employeesAdapter,
                binding.selTrainer,
                (v,position,employee) -> {
                    binding.selTrainer.toggleSelected(position);
                    if (viewModel.getTrialTraineeId() == null) {
                        SearchTraineesParams params = new SearchTraineesParams();
                        if (viewModel.getCurrentOrgUnit() != null)
                            params.orgUnitId = viewModel.getCurrentOrgUnit();
                        params.trainerId = employee.getId();
                        traineesAdapter.extendedFilter(params);
                    }
                });
        UiUtil.selectSetup(
                requireContext(),
                orgUnitsAdapter,
                binding.selOrgUnit,
                (v,position,orgUnit) -> {
                    binding.selOrgUnit.toggleSelected(position);
                    viewModel.setCurrentOrgUnit(orgUnit.getId());

                    SearchEmployeesParams searchEmployeesParams = new SearchEmployeesParams();
                    searchEmployeesParams.orgUnitId = orgUnit.getId();
                    employeesAdapter.extendedFilter(searchEmployeesParams);

                    if (viewModel.getTrialTraineeId() == null) {
                        SearchTraineesParams searchTraineesParams = new SearchTraineesParams();
                        searchTraineesParams.orgUnitId = orgUnit.getId();
                        traineesAdapter.extendedFilter(searchTraineesParams);
                    }
                });
        binding.buttonRecurrence.setPaintFlags(
                binding.buttonRecurrence.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);

        binding.sFinished.setOnCheckedChangeListener((v, checked) -> {
            if (checked && binding.sCancelled.isChecked())
                binding.sCancelled.setChecked(false);
        });
        binding.sCancelled.setOnCheckedChangeListener((v, checked) -> {
            if (checked && binding.sFinished.isChecked())
                binding.sFinished.setChecked(false);
        });

        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.ARG_APPOINTMENT_ID)) {
                int apptId = getArguments().getInt(Constants.ARG_APPOINTMENT_ID);
                viewModel.getAppointmentsRepository().getAppointment(apptId);
            }
            if (getArguments().containsKey(Constants.ARG_EDIT_MODE)) {
                boolean isEditMode = getArguments().getBoolean(Constants.ARG_EDIT_MODE);
                viewModel.setEditMode(isEditMode);
            }
        }

        if (viewModel.isEditMode()) {
            binding.buttonRecurrence.setVisibility(View.GONE);

            ((BaseActivity) requireActivity()).setToolbarActions(List.of(
                    new ToolbarAction(
                            R.id.action_delete,
                            R.drawable.icon_trash,
                            R.string.label_delete,
                            MenuItem.SHOW_AS_ACTION_ALWAYS,
                            R.color.error,
                            () -> UiUtil.makeToast(requireActivity(), getContext(), getString(R.string.msg_long_press_delete)),
                            () -> viewModel.deleteAppointment(
                                    viewModel.getAppointment().getValue().getId(),
                                    viewModel.getSession().getUserId())
                    )
            ));
        }
        else {
            binding.buttonRecurrence.setVisibility(View.VISIBLE);
        }

        /* mora nakon inicijalizacije podataka */
        UiUtil.datePickerSetup(this, binding.ifDate);
        UiUtil.timePickerSetup(this, binding.ifStartTime);
        UiUtil.timePickerSetup(this, binding.ifEndTime);

        viewModel.getToastEvent().observe(getViewLifecycleOwner(), msgId
                -> UiUtil.makeToast(requireActivity(), requireContext(), getString(msgId)));

        viewModel.getNavigationEvent().observe(getViewLifecycleOwner(), i
                -> NavigationUtil.navigateBackTo(this, R.id.fragmentFrame, Constants.FRAG_APPOINTMENTS_CALENDAR));

        binding.buttonSave.setOnClickListener(v -> save());
        binding.buttonCancel.setOnClickListener(v -> cancel());
        binding.buttonTrialTrainee.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.ARG_APPOINTMENT_SHORTCUT, true);
            NavigationUtil.navigate(this, R.id.fragmentFrame, new EditTraineeBasicInfoFragment(), bundle);
        });
        binding.buttonRecurrence.setOnClickListener(v
                -> NavigationUtil.navigate(this, R.id.fragmentFrame, new EditRecurrencePatternFragment(), null));

        /* TODO: dialog za ponavljanje */
        /* TODO: dialog za trening */

        viewModel.getCompaniesRepository().searchOrgUnits(new SearchOrgUnitsParams());
        viewModel.getTraineesRepository().searchTrainees(new SearchTraineesParams());
        viewModel.getEmployeesRepository().searchEmployees(new SearchEmployeesParams());

        viewModel.getOrgUnits().observe(getViewLifecycleOwner(), orgUnits
                -> orgUnitsAdapter.setData(orgUnits));
        viewModel.getTrainees().observe(getViewLifecycleOwner(), trainees
                -> traineesAdapter.setData(trainees));
        viewModel.getEmployees().observe(getViewLifecycleOwner(), employees
                -> employeesAdapter.setData(employees));

        viewModel.getAppointment().observe(getViewLifecycleOwner(), data -> {
            if (viewModel.isEditMode()) {
                String subtitle = MessageFormat.format("{0}, {1}", data.getTraineeName(), DateUtil.format(data.getDate()));
                ((BaseActivity) requireActivity()).setToolbarSubtitle(subtitle);
                viewModel.setAppointmentDraft(data);
                binding.selTrainee.setSelected(data.getTraineeId(), data.getTraineeName());
                binding.selTrainer.setSelected(data.getTrainerId(), data.getTrainerName());
                binding.selOrgUnit.setSelected(data.getOrgUnitId(), data.getOrgUnitName());
                binding.ifDate.setText(DateUtil.format(data.getDate()));
                binding.ifStartTime.setText(TimeUtil.format(data.getStartTime()));
                binding.ifEndTime.setText(TimeUtil.format(data.getEndTime()));
                binding.sFinished.setChecked(data.isFinished());
                binding.sCancelled.setChecked(data.isCancelled());
                if (!StringUtil.isNullOrEmpty(data.getSessionDescription())) {
                    binding.ifDescription.setText(data.getSessionDescription());
                }
            }
        });
        viewModel.getRecurrencePattern().observe(getViewLifecycleOwner(), data -> {
            if (viewModel.isEditMode()) {
                viewModel.setByRecurrence(true);
                binding.buttonRecurrence.setVisibility(View.GONE);
                binding.recurrenceDateSpan.setFirstText(DateUtil.format(data.getDateFrom()));
                binding.recurrenceDateSpan.setSecondText(DateUtil.format(data.getDateTo()));
                binding.recurrenceWeekdays.setText(data.convertDaysToString());
                binding.recurrenceInfo.setVisibility(View.VISIBLE);
            }
        });
        getParentFragmentManager().setFragmentResultListener(
                Constants.MSG_TRAINEE_CREATED,
                this,
                (key, bundle) -> {
                    int newTraineeId = bundle.getInt(Constants.ARG_TRAINEE_ID);
                    viewModel.setTrialTraineeId(newTraineeId);
                    binding.selTrainee.addPendingSelection(newTraineeId);
                    binding.selTrainee.setClickable(false);
                    binding.selTrainee.setFocusable(false);
                    binding.buttonRecurrence.setVisibility(View.GONE);
                });
    }

    private void cancel() {
        NavigationUtil.navigateBackTo(this, R.id.fragmentFrame, Constants.FRAG_APPOINTMENTS_CALENDAR);
    }

    private void save() {
        if (checkRequiredFields()) {
            viewModel.applyAppointmentFromBinding(binding);
            viewModel.saveAction();
        }
    }

    private boolean checkRequiredFields() {
        boolean valid = true;
        if (!binding.selTrainee.isSelected()) {
            binding.selTrainee.setError(getString(R.string.msg_required));
            valid = false;
        }
        if (!binding.selTrainer.isSelected()) {
            binding.selTrainer.setError(getString(R.string.msg_required));
            valid = false;
        }
        if (!binding.selOrgUnit.isSelected()) {
            binding.selTrainer.setError(getString(R.string.msg_required));
            valid = false;
        }
        if (StringUtil.isNullOrEmpty(binding.ifDate.getText())) {
            binding.ifDate.setError(getString(R.string.msg_required));
            valid = false;
        }
        if (StringUtil.isNullOrEmpty(binding.ifStartTime.getText())) {
            binding.ifStartTime.setError(getString(R.string.msg_required));
            valid = false;
        }
        if (StringUtil.isNullOrEmpty(binding.ifEndTime.getText())) {
            binding.ifEndTime.setError(getString(R.string.msg_required));
            valid = false;
        }
        return valid;
    }
}
