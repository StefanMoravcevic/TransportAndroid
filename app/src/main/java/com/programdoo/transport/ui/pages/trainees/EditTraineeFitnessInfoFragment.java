package com.programdoo.transport.ui.pages.trainees;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.databinding.FragmentEditTraineeFitnessInfoBinding;
import com.programdoo.transport.ui.adapters.MasterDataRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.trainees.EditTraineeViewModel;

import java.util.ArrayList;

public class EditTraineeFitnessInfoFragment extends BaseFragment {
    private FragmentEditTraineeFitnessInfoBinding binding;
    private EditTraineeViewModel viewModel;
    private MasterDataRecyclerViewAdapter limitationsAdapter;
    private MasterDataRecyclerViewAdapter goalsAdapter;
    private MasterDataRecyclerViewAdapter foundUsMethodsAdapter;
    private final int stepNumber = 2;
    @Override
    public String TAG() { return Constants.FRAG_EDIT_TRAINEE_FITNESS_INFO; }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup viewGroup,
            Bundle savedInstanceState) {
        /* build-a binding. svaki layout ce da generise svoju klasu. ako je ime layout-a
         * activity_x (fragment_x), imace binding ActivityXBinding (FragmentXBinding).
         * preko binding-a moze da se pristupi svim ui komponentama iz xml-a.  */
        binding = FragmentEditTraineeFitnessInfoBinding.inflate(inflater);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(EditTraineeViewModel.class);
        limitationsAdapter = new MasterDataRecyclerViewAdapter(requireContext(), new ArrayList<>());
        goalsAdapter = new MasterDataRecyclerViewAdapter(requireContext(), new ArrayList<>());
        foundUsMethodsAdapter = new MasterDataRecyclerViewAdapter(requireContext(), new ArrayList<>());

        UiUtil.multiSelectSetup(requireContext(), limitationsAdapter, binding.mselLimitations, (v,position,item)
                -> binding.mselLimitations.toggleSelected(position));
        UiUtil.multiSelectSetup(requireContext(), goalsAdapter, binding.mselGoals, (v,position,item)
                -> binding.mselGoals.toggleSelected(position));
        UiUtil.selectSetup(requireContext(), foundUsMethodsAdapter, binding.selFoundUsMethod, (v,position,item)
                -> binding.selFoundUsMethod.toggleSelected(position));


        binding.stepTabLayout.getTabAt(stepNumber).select();
        binding.buttonCancel.setOnClickListener(this::cancel);
        binding.buttonPrevious.setOnClickListener(this::previous);
        binding.buttonSave.setOnClickListener(this::save);

        viewModel.getMasterDataRepository().GetSelectOptionsByTable("Limitations", "Name");
        viewModel.getMasterDataRepository().GetSelectOptionsByTable("Goals", "Name");
        viewModel.getMasterDataRepository().GetSelectOptionsByTable("FoundUsMethods", "Name");

        viewModel.getToastEvent().observe(getViewLifecycleOwner(), msgId
                -> UiUtil.makeToast(requireActivity(), requireContext(), getString(msgId)));
        viewModel.getNavigationEvent().observe(getViewLifecycleOwner(), b
                -> NavigationUtil.navigateBackTo(this, R.id.fragmentFrame, Constants.FRAG_TRAINEES_LIST));
        viewModel.getLimitations().observe(getViewLifecycleOwner(), limitations
                -> limitationsAdapter.setData(limitations));
        viewModel.getGoals().observe(getViewLifecycleOwner(), goals
                -> goalsAdapter.setData(goals));
        viewModel.getFoundUsMethods().observe(getViewLifecycleOwner(), methods
                -> foundUsMethodsAdapter.setData(methods));
        viewModel.getTrainee().observe(getViewLifecycleOwner(), trainee -> {
            if (viewModel.isEditMode()) {
                if (!trainee.getLimitationsIds().isEmpty() && !trainee.getLimitations().isEmpty())
                    binding.mselLimitations.addSelected(trainee.getLimitationsIds(), trainee.getLimitations());
                if (!trainee.getGoalsIds().isEmpty() && !trainee.getGoals().isEmpty())
                    binding.mselGoals.addSelected(trainee.getGoalsIds(), trainee.getGoals());
                if (trainee.getFoundUsMethodId() != null && !StringUtil.isNullOrEmpty(trainee.getFoundUsMethod()))
                    binding.selFoundUsMethod.setSelected(trainee.getFoundUsMethodId(), trainee.getFoundUsMethod());
                if (!StringUtil.isNullOrEmpty(trainee.getPreviousFitnessExperience()))
                    binding.ifPreviousFitnessExperience.setText(trainee.getPreviousFitnessExperience());
            }
        });
    }

    public void cancel(View view) {
        viewModel.clearEditData();
        NavigationUtil.navigateBackTo(this, R.id.fragmentFrame, Constants.FRAG_TRAINEES_LIST);
    }

    public void previous(View view) {
        viewModel.applyFitnessInfoFromBinding(binding);
        NavigationUtil.navigate(this, R.id.fragmentFrame, new EditTraineeAdditionalInfoFragment(), null);
    }

    public void save(View view) {
        if (!checkRequiredFields()) {
            UiUtil.makeToast(this.requireActivity(), this.requireContext(), getString(R.string.msg_requiredFields));
        }
        else {
            viewModel.applyFitnessInfoFromBinding(binding);
            viewModel.saveTrainee(viewModel.getTraineeDraft());
        }
    }

    public boolean checkRequiredFields() {
        boolean valid = true;

//        if (!binding.mselLimitations.isSelected()) {
//            valid = false;
//            binding.mselLimitations.setError(getString(R.string.msg_required));
//        }
//        if (!binding.mselGoals.isSelected()) {
//            valid = false;
//            binding.mselGoals.setError(getString(R.string.msg_required));
//        }

        return valid;
    }
}
