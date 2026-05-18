package com.programdoo.transport.ui.pages.memberships;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.databinding.FragmentMembershipInfoBinding;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.ui.viewmodels.memberships.MembershipInfoViewModel;

import javax.annotation.Nullable;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MembershipInfoFragment extends BaseFragment {

    private FragmentMembershipInfoBinding binding;
    private MembershipInfoViewModel viewModel;

    @Override
    public String TAG() {
        return Constants.FRAG_MEMBERSHIP_INFO;
    }
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MembershipInfoViewModel.class);
        binding = FragmentMembershipInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.ARG_MEMBERSHIP_ID)) {
                int membershipId = getArguments().getInt(Constants.ARG_MEMBERSHIP_ID);
                viewModel.getMembershipsRepository().getMembership(membershipId);
            }
        }

//        if (getActivity() instanceof TraineesActivity) {
//            ((TraineesActivity) getActivity()).setToolbarTitle(getString(R.string.label_membership_info));
//        } else if (getActivity() instanceof MembershipActivity) {
//            ((MembershipActivity) getActivity()).setToolbarTitle(getString(R.string.label_membership_info));
//        }
        ((BaseActivity) requireActivity()).setToolbarTitle(getString(R.string.label_membership_info));

        viewModel.getMembership().observe(getViewLifecycleOwner(), data -> {
            ((BaseActivity) requireActivity()).setToolbarSubtitle(data.getTraineeName());
            binding.tvFullName.setText(data.getTraineeName());
            binding.tvOrgUnit.setText(data.getOrgUnitName());
            binding.idMembershipInfo.setFirstText(data.getMembershipName());
            binding.idMembershipInfo.setSecondText(data.getMembershipType());
            binding.idValidFrom.setFirstText(DateUtil.format(data.getValidFromDate()));
            binding.idValidFrom.setSecondText(DateUtil.format(data.getValidToDate()));
            binding.idNumberOfAppointments.setText(String.valueOf(data.getTotalSessions()));
        });
    }
}
