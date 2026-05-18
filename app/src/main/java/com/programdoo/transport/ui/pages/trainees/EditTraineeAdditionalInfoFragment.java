package com.programdoo.transport.ui.pages.trainees;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.enums.Genders;
import com.programdoo.transport.data.models.enums.Languages;
import com.programdoo.transport.data.models.requests.companies.SearchOrgUnitsParams;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.databinding.FragmentEditTraineeAdditionalInfoBinding;
import com.programdoo.transport.ui.adapters.EmployeesRecyclerViewAdapter;
import com.programdoo.transport.ui.adapters.EnumRecyclerViewAdapter;
import com.programdoo.transport.ui.adapters.OrgUnitsRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.trainees.EditTraineeViewModel;

import java.util.ArrayList;
import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditTraineeAdditionalInfoFragment extends BaseFragment {
    private FragmentEditTraineeAdditionalInfoBinding binding;
    private EditTraineeViewModel viewModel;
    private EmployeesRecyclerViewAdapter employeesAdapter;
    private OrgUnitsRecyclerViewAdapter mainOrgUnitsAdapter;
    private OrgUnitsRecyclerViewAdapter mselOrgUnitsAdapter;
    private EnumRecyclerViewAdapter gendersAdapter;
    private EnumRecyclerViewAdapter languagesAdapter;
    private final int stepNumber = 1;
    @Override
    public String TAG() { return Constants.FRAG_EDIT_TRAINEE_ADDITIONAL_INFO; }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup viewGroup,
            Bundle savedStateInstance) {
        /* build-a binding. svaki layout ce da generise svoju klasu. ako je ime layout-a
         * activity_x (fragment_x), imace binding ActivityXBinding (FragmentXBinding).
         * preko binding-a moze da se pristupi svim ui komponentama iz xml-a.  */
        binding = FragmentEditTraineeAdditionalInfoBinding.inflate(inflater);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(EditTraineeViewModel.class);

        employeesAdapter = new EmployeesRecyclerViewAdapter(requireContext(), new ArrayList<>());
        mainOrgUnitsAdapter = new OrgUnitsRecyclerViewAdapter(requireContext(), new ArrayList<>());
        mselOrgUnitsAdapter = new OrgUnitsRecyclerViewAdapter(requireContext(), new ArrayList<>());
        gendersAdapter = new EnumRecyclerViewAdapter(requireContext(), Arrays.asList(Genders.values()));
        languagesAdapter = new EnumRecyclerViewAdapter(requireContext(), Arrays.asList(Languages.values()));

        UiUtil.selectSetup(requireContext(), employeesAdapter, binding.selCurrentTrainer, (v, position, item) -> {
            binding.selCurrentTrainer.toggleSelected(position);
            binding.mselTraineeOrgUnits.toggleSelected(position);
        });
        UiUtil.selectSetup(requireContext(), mainOrgUnitsAdapter, binding.selOrgUnit, (v, position, item) -> {
            binding.selOrgUnit.toggleSelected(position);

            SearchEmployeesParams params = new SearchEmployeesParams();
            params.orgUnitId = item.getId();
            employeesAdapter.extendedFilter(params);
        });
        UiUtil.selectSetup(requireContext(), gendersAdapter, binding.selGender, (v,position,item) -> {
           binding.selGender.toggleSelected(position);
        });
        UiUtil.selectSetup(requireContext(), languagesAdapter, binding.selLanguage, (v,position,item) -> {
           binding.selLanguage.toggleSelected(position);
        });
        UiUtil.multiSelectSetup(requireContext(), mselOrgUnitsAdapter, binding.mselTraineeOrgUnits, (v,position,item) -> {
            binding.mselTraineeOrgUnits.toggleSelected(position);
        });
        UiUtil.datePickerSetup(this, binding.ifBirthdate);

        binding.stepTabLayout.getTabAt(stepNumber).select();
        binding.buttonCancel.setOnClickListener(this::cancel);
        binding.buttonPrevious.setOnClickListener(this::previous);
        binding.buttonNext.setOnClickListener(this::next);

        viewModel.getEmployeesRepository().searchEmployees(new SearchEmployeesParams());
        viewModel.getCompaniesRepository().searchOrgUnits(new SearchOrgUnitsParams());

        viewModel.getToastEvent().observe(getViewLifecycleOwner(), msgId -> {
            UiUtil.makeToast(requireActivity(), requireContext(), getString(msgId));
        });
        viewModel.getEmployees().observe(getViewLifecycleOwner(), employees
                -> employeesAdapter.setData(employees));
        viewModel.getOrgUnits().observe(getViewLifecycleOwner(), orgUnits -> {
            mainOrgUnitsAdapter.setData(orgUnits);
            mselOrgUnitsAdapter.setData(orgUnits);
        });
        viewModel.getTrainee().observe(getViewLifecycleOwner(), trainee -> {
            if (viewModel.isEditMode()) {
                if (trainee.getBirthdate() != null)
                    binding.ifBirthdate.setText(DateUtil.format(trainee.getBirthdate()));
                binding.selGender.setSelected(Genders.fromValue(trainee.getGenderId()));
                binding.selLanguage.setSelected(Languages.fromDescription(trainee.getUserCulture()));
                if (!StringUtil.isNullOrEmpty(trainee.getEmail()))
                    binding.ifEmail.setText(trainee.getEmail());
                binding.sReceiveEmailNotifications.setChecked(trainee.isReceiveEmailNotifications());
                binding.selOrgUnit.setSelected(trainee.getOrgUnitId(), trainee.getOrgUnitName());
                binding.selCurrentTrainer.setSelected(trainee.getCurrentTrainerId(), trainee.getCurrentTrainerName());
                binding.sActive.setChecked(trainee.isActive());
                binding.mselTraineeOrgUnits.addSelected(trainee.getTraineeOrgUnitsIds(), trainee.getTraineeOrgUnits());
            }
        });
    }

    public void cancel(View view) {
        viewModel.clearEditData();
        NavigationUtil.navigateBackTo(this, R.id.fragmentFrame, Constants.FRAG_TRAINEES_LIST);
    }

    public void previous(View view) {
        viewModel.applyAdditionalInfoFromBinding(binding);
        NavigationUtil.navigate(this, R.id.fragmentFrame, new EditTraineeBasicInfoFragment(), null);
    }

    public void next(View view) {
        if (checkRequiredFields()) {
            viewModel.applyAdditionalInfoFromBinding(binding);
            NavigationUtil.navigate(this, R.id.fragmentFrame, new EditTraineeFitnessInfoFragment(), null);
        }
    }

    public boolean checkRequiredFields() {
        boolean valid = true;

        if (StringUtil.isNullOrEmpty(binding.ifBirthdate.toString())) {
            valid = false;
            binding.ifBirthdate.setError(getString(R.string.msg_required));
        }
        if (StringUtil.isNullOrEmpty(binding.ifEmail.toString())) {
            valid = false;
            binding.ifEmail.setError(getString(R.string.msg_required));
        }
        if (!binding.selCurrentTrainer.isSelected()) {
            valid = false;
            binding.selCurrentTrainer.setError(getString(R.string.msg_required));
        }
        if (!binding.selOrgUnit.isSelected()) {
            valid = false;
            binding.selOrgUnit.setError(getString(R.string.msg_required));
        }

        return valid;
    }
}
