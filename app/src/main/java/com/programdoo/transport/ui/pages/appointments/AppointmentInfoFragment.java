package com.programdoo.transport.ui.pages.appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.databinding.FragmentAppointmentInfoBinding;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.utils.TimeUtil;
import com.programdoo.transport.ui.viewmodels.appointments.AppointmentInfoViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AppointmentInfoFragment extends BaseFragment {
    private FragmentAppointmentInfoBinding binding;
    private AppointmentInfoViewModel viewModel;

    @Override
    public String TAG() {
        return Constants.FRAG_APPOINTMENT_INFO;
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            @Nullable Bundle savedStateInstance) {
        viewModel = new ViewModelProvider(this).get(AppointmentInfoViewModel.class);
        binding = FragmentAppointmentInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedStateInstance) {

        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.ARG_APPOINTMENT_ID)) {
                int apptId = getArguments().getInt(Constants.ARG_APPOINTMENT_ID);
                viewModel.getAppointmentsRepository().getAppointment(apptId);
            }
        }

        ((BaseActivity) requireActivity()).clearToolbarActions();

        viewModel.getAppointment().observe(getViewLifecycleOwner(), appt -> {
            binding.iiTrainee.setText(appt.getTraineeName());
            binding.iiTrainer.setText(appt.getTrainerName());
            binding.iiOrgUnit.setText(appt.getOrgUnitName());
            binding.iiDate.setText(DateUtil.format(appt.getDate()));
            binding.iidStartEnd.setFirstText(TimeUtil.format(appt.getStartTime()));
            binding.iidStartEnd.setSecondText(TimeUtil.format(appt.getEndTime()));

            if (appt.getRecurrencePatternId() == null) {
                binding.llRecurrence.setVisibility(View.GONE);
                binding.tvRecurrenceMsg.setVisibility(View.VISIBLE);
            }
            else {
                viewModel.getAppointmentsRepository().getRecurrencePattern(appt.getRecurrencePatternId());
            }

            if (StringUtil.isNullOrEmpty(appt.getSessionDescription())) {
                binding.iiDescription.setVisibility(View.GONE);
                binding.tvDescriptionMsg.setVisibility(View.VISIBLE);
            }
            else {
                binding.iiDescription.setText(appt.getSessionDescription());
            }

            /* TODO: informacije o clanarini */
            binding.iidMembershipExpirySessionsLeft.setFirstText(DateUtil.format(appt.getMembershipExpiryDate()));
            binding.iidMembershipExpirySessionsLeft.setSecondText(StringUtil.toString(appt.getSessionsLeftAfter()));
        });

        viewModel.getRecurrencePattern().observe(getViewLifecycleOwner(), data -> {
            binding.iiRecurrence.setText(data.convertDaysToString());
            binding.iidRecurFromTo.setFirstText(DateUtil.format(data.getDateFrom()));
            binding.iidRecurFromTo.setSecondText(DateUtil.format(data.getDateTo()));
        });
    }
}
