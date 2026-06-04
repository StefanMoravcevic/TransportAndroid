package com.programdoo.transport.ui.pages.trainees;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.enums.Genders;
import com.programdoo.transport.databinding.FragmentTraineeInfoBinding;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.pages.activities.ActivitiesListFragment;
import com.programdoo.transport.ui.pages.memberships.EditMembershipFragment;
import com.programdoo.transport.ui.pages.memberships.MembershipsListFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.ui.viewmodels.trainees.TraineeInfoViewModel;

import javax.annotation.Nullable;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TraineeInfoFragment extends BaseFragment {
    private FragmentTraineeInfoBinding binding;
    private TraineeInfoViewModel viewModel;

    @Override
    public String TAG() {
        return Constants.FRAG_TRAINEE_INFO;
    }
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(TraineeInfoViewModel.class);
        binding = FragmentTraineeInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.ARG_TRAINEE_ID)) {
                int traineeId = getArguments().getInt(Constants.ARG_TRAINEE_ID);
                viewModel.getTraineesRepository().getTrainee(traineeId);
            }
        }

        ((BaseActivity) requireActivity()).clearToolbarActions();

        viewModel.getTrainee().observe(getViewLifecycleOwner(), data -> {
            ((BaseActivity) requireActivity()).setToolbarSubtitle(data.getFullName());
            binding.tvFullName.setText(data.getFullName());
            binding.tvHeight.setText(StringUtil.toString(data.getHeight()));
            binding.tvOrgUnit.setText(data.getOrgUnitName());
            binding.tvPhoneNumber.setText(data.getPhoneNumber());
            binding.tvEmail.setText(data.getEmail());
            binding.iidMembershipExpirySessionsLeft.setFirstText(DateUtil.format(data.getMembershipExpiryDate()));
            binding.iidMembershipExpirySessionsLeft.setSecondText(StringUtil.toString(data.getSessionsLeft()));
            binding.iiNextSessionDate.setText(DateUtil.format(data.getNextSessionDate()));
            binding.iidTrialTrainerDate.setFirstText(data.getTrialTrainerName());
            binding.iidTrialTrainerDate.setSecondText(DateUtil.format(data.getTrialDate()));

            if (data.getGenderId() == null || data.getGenderId() == Genders.FEMALE.getValue()) {
                binding.imgProfilePicture.setImageResource(R.drawable.image_placeholder_profile_female);
            }
            else {
                binding.imgProfilePicture.setImageResource(R.drawable.image_placeholder_profile_male);
            }
        });


        binding.bMeasurements.setOnClickListener(v -> {});
        binding.bNewMeasurement.setOnClickListener(v -> {});
        binding.bMemberships.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            int traineeId = viewModel.getTrainee().getValue().getId();
            bundle.putInt(Constants.ARG_TRAINEE_ID, traineeId);

            NavigationUtil.navigate(this, R.id.fragmentFrame, new MembershipsListFragment(), bundle);
        });
        binding.bNewMembership.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            int traineeId = viewModel.getTrainee().getValue().getId();
            bundle.putInt(Constants.ARG_TRAINEE_ID, traineeId);

            NavigationUtil.navigate(this, R.id.fragmentFrame, new EditMembershipFragment(), bundle);
        });
        binding.bActivities.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            int traineeId = viewModel.getTrainee().getValue().getId();
            String traineeName = viewModel.getTrainee().getValue().getFullName();
            bundle.putInt(Constants.ARG_TRAINEE_ID, traineeId);
            bundle.putString(Constants.ARG_TRAINEE_NAME, traineeName);

            NavigationUtil.navigate(this, R.id.fragmentFrame, new ActivitiesListFragment(), bundle);
        });
        binding.bNewActivity.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            int traineeId = viewModel.getTrainee().getValue().getId();
            String traineeName = viewModel.getTrainee().getValue().getFullName();
            bundle.putInt(Constants.ARG_TRAINEE_ID, traineeId);
            bundle.putString(Constants.ARG_TRAINEE_NAME, traineeName);

            NavigationUtil.navigate(this, R.id.fragmentFrame, new EditMembershipFragment(), bundle);

        });
    }
}
