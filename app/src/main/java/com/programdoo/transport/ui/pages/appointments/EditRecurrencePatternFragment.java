package com.programdoo.transport.ui.pages.appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.enums.Weekdays;
import com.programdoo.transport.databinding.FragmentEditRecurrencePatternBinding;
import com.programdoo.transport.ui.adapters.EnumRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.appointments.EditAppointmentViewModel;

import java.util.Arrays;

public class EditRecurrencePatternFragment extends BaseFragment {
    private FragmentEditRecurrencePatternBinding binding;
    private EditAppointmentViewModel viewModel;
    private EnumRecyclerViewAdapter weekdaysAdapter;

    @Override
    public String TAG() { return Constants.FRAG_EDIT_RECURRENCE_PATTERN; }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            @Nullable Bundle savedStateInstance) {
        viewModel = new ViewModelProvider(this).get(EditAppointmentViewModel.class);
        binding = FragmentEditRecurrencePatternBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedStateInstance) {
        weekdaysAdapter = new EnumRecyclerViewAdapter(requireContext(), Arrays.asList(Weekdays.values()));
        UiUtil.multiSelectSetup(
                getContext(),
                weekdaysAdapter,
                binding.mselWeekdays,
                (v,position,weekday)
                        -> binding.mselWeekdays.toggleSelected(position));

        UiUtil.datePickerSetup(this, binding.ifRecurFrom);
        UiUtil.datePickerSetup(this, binding.ifRecurTo);

        viewModel.getNavigationEvent().observe(getViewLifecycleOwner(), i
                -> NavigationUtil.navigateBackTo(this, R.id.fragmentFrame, Constants.FRAG_EDIT_APPOINTMENT));

        binding.buttonSave.setOnClickListener(this::save);
        binding.buttonCancel.setOnClickListener(this::cancel);
    }

    public void save(View view) {
        if (checkRequiredFields()) {
            viewModel.applyRecurrenceFromBinding(binding);
            viewModel.saveRecurrencePattern(viewModel.getRecurrenceDraft());
        }
    }
    public void cancel(View view) {
        NavigationUtil.navigateBackTo(this, R.id.fragmentFrame, Constants.FRAG_EDIT_APPOINTMENT);
    }

    private boolean checkRequiredFields() {
        boolean valid = true;
        if (!binding.mselWeekdays.isSelected()) {
            binding.mselWeekdays.setError(getString(R.string.msg_required));
            valid = false;
        }
        if (StringUtil.isNullOrEmpty(binding.ifRecurFrom.getText())) {
            binding.ifRecurFrom.setError(getString(R.string.msg_required));
            valid = false;
        }
        if (StringUtil.isNullOrEmpty(binding.ifRecurTo.getText())) {
            binding.ifRecurTo.setError(getString(R.string.msg_required));
            valid = false;
        }

        return valid;
    }
}
