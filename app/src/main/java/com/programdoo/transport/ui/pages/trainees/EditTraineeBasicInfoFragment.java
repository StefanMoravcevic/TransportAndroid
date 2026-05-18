package com.programdoo.transport.ui.pages.trainees;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.requests.promotions.SearchPromotionsParams;
import com.programdoo.transport.databinding.FragmentEditTraineeBasicInfoBinding;
import com.programdoo.transport.ui.adapters.PromotionsRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.trainees.EditTraineeViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditTraineeBasicInfoFragment extends BaseFragment {
    private EditTraineeViewModel viewModel;
    private FragmentEditTraineeBasicInfoBinding binding;
    private PromotionsRecyclerViewAdapter promotionsAdapter;
    private final int stepNumber = 0;
    @Override public String TAG() { return Constants.FRAG_EDIT_TRAINEE_BASIC_INFO; }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /* build-a binding. svaki layout ce da generise svoju klasu. ako je ime layout-a
         * activity_x (fragment_x), imace binding ActivityXBinding (FragmentXBinding).
         * preko binding-a moze da se pristupi svim ui komponentama iz xml-a.  */
        binding = FragmentEditTraineeBasicInfoBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        promotionsAdapter = new PromotionsRecyclerViewAdapter(requireContext(), new ArrayList<>());

        ((BaseActivity) requireActivity()).clearToolbarActions();

        viewModel = new ViewModelProvider(requireActivity()).get(EditTraineeViewModel.class);
        /* poziva helper funkciju koja konfigurise select dialog.
         * prosledjuje joj se kontekst, adapter za recycler view, instanca select dialog-a iz
         * bindinga i lambda funkcija koja definise sta se desava prilikom klika na item iz select liste.
         * ta funkcija se postavlja kao click listener za adapter. */
        UiUtil.selectSetup(getContext(), promotionsAdapter, binding.selPromotion, (view1, position, item) ->
                binding.selPromotion.toggleSelected(position));
        binding.buttonPrimary.setOnClickListener(v -> primaryAction(view));
        binding.buttonCancel.setOnClickListener(v -> cancel(view));
        binding.sTrialPeriod.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) {
                viewModel.setTrialForm(true);
                binding.buttonPrimary.setText(getString(R.string.label_save_trial_trainee));
            }
            else {
                viewModel.setTrialForm(false);
                binding.buttonPrimary.setText(getString(R.string.label_next));
            }
        });

        /* ukoliko je neki argument poslat preko bundle-a prilikom fragment transaction-a, ovde
         * se dovlaci. getArguments() proverava da li postoje bilo kakvi podaci. constainsKey() proverava
         * za konkretan key da li postoji podatak.
         * u ovom slucaju, trazimo da li je prosledjen vezbac i ako jeste, popunjavamo inpute
         * podacima iz prosledjenog objekta. */
        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.ARG_TRAINEE_ID)) {
                int traineeId = getArguments().getInt(Constants.ARG_TRAINEE_ID);
                viewModel.getTraineesRepository().getTrainee(traineeId);
            }
            if (getArguments().containsKey(Constants.ARG_EDIT_MODE)) {
                boolean isEditMode = getArguments().getBoolean(Constants.ARG_EDIT_MODE);
                viewModel.setEditMode(isEditMode);
            }
            if (getArguments().containsKey(Constants.ARG_APPOINTMENT_SHORTCUT)) {
                boolean isAppointmentShortcut = getArguments().getBoolean(Constants.ARG_APPOINTMENT_SHORTCUT);
                viewModel.setAppointmentShortcut(isAppointmentShortcut);
                viewModel.setTrialForm(true);
                binding.sTrialPeriod.setChecked(true);
                binding.sTrialPeriod.setClickable(false);
                ((BaseActivity) requireActivity()).setToolbarSubtitle(getString(R.string.label_add_trial_trainee));
            }
        }

        if (viewModel.isEditMode())
            ((BaseActivity) requireActivity()).setToolbarTitle(getString(R.string.label_edit_trainee));
        else
            ((BaseActivity) requireActivity()).setToolbarTitle(getString(R.string.label_newTrainee));

        binding.stepTabLayout.getTabAt(stepNumber).select();

        viewModel.getPromotionsRepository().searchPromotions(new SearchPromotionsParams());

        /* postavljanje observer-a na toast event. vise o tome u SingleLiveEvent klasi */
        viewModel.getToastEvent().observe(getViewLifecycleOwner(), msgId ->
                UiUtil.makeToast(requireActivity(), getContext(), getString(msgId)));
        /* postavljanje observer-a na navigation event */
        viewModel.getNavigationEvent().observe(getViewLifecycleOwner(), b ->
                NavigationUtil.navigateBackTo(this, R.id.fragmentFrame, viewModel.getReturnDestinationTag()));
        /* postavljanje observer-a na listu promocija, koja treba da popuni select dialog za promocije
         * vise o observer-ima u TraineeListViewModel i EditTraineeViewModel klasama */
        viewModel.getPromotions().observe(getViewLifecycleOwner(), promotions ->
                promotionsAdapter.setData(promotions));
        viewModel.getTrainee().observe(getViewLifecycleOwner(), trainee -> {
            // fix da ne dovlaci stale podatak iz viewModel.getTrainee() stream-a, NE RADI
            if (viewModel.isEditMode()) {
                ((BaseActivity) requireActivity()).setToolbarSubtitle(trainee.getFullName());
                viewModel.setTraineeDraft(trainee);
                if (!StringUtil.isNullOrEmpty(trainee.getName()))
                    binding.ifFirstName.setText(trainee.getName());
                if (!StringUtil.isNullOrEmpty(trainee.getSurname()))
                    binding.ifSurname.setText(trainee.getSurname());
                if (!StringUtil.isNullOrEmpty(trainee.getPhoneNumber()))
                    binding.ifPhoneNumber.setText(trainee.getPhoneNumber());
                if (!StringUtil.isNullOrEmpty(trainee.getNickname()))
                    binding.ifNickname.setText(trainee.getNickname());
                binding.sTrialPeriod.setChecked(trainee.isInTrialPeriod());
                if (trainee.getPromotionId() != null && !StringUtil.isNullOrEmpty(trainee.getPromotionName())) {
                    binding.selPromotion.setSelected(trainee.getPromotionId(), trainee.getPromotionName());
                }
            }
        });
        viewModel.getSaveResponse().observe(getViewLifecycleOwner(), traineeId -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.ARG_TRAINEE_ID, traineeId);
            getParentFragmentManager().setFragmentResult(Constants.MSG_TRAINEE_CREATED, bundle);
        });
    }

    /**
     * prelazi na naredni ekran ili cuva probnog vezbaca (u zavisnosti od
     * vrste forme) ako su inputi okej
     */
    public void primaryAction(View view) {
        if (!checkRequiredFields()) {
            UiUtil.makeToast(this.requireActivity(), this.requireContext(), getString(R.string.msg_requiredFields));
            return;
        }
        viewModel.applyBasicInfoFromBinding(binding);

        if (viewModel.isTrialForm())
            viewModel.saveTrialTrainee(viewModel.getTraineeDraft());
        else
            NavigationUtil.navigate(this, R.id.fragmentFrame,
                    new EditTraineeAdditionalInfoFragment(), null);
    }
    /**
     * vraca se na listu vezbaca
     */
    public void cancel(View view) {
        viewModel.clearEditData();
        NavigationUtil.navigateBackTo(this, R.id.fragmentFrame, viewModel.getReturnDestinationTag());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public boolean checkRequiredFields() {
        boolean valid = true;

        if (StringUtil.isNullOrEmpty(binding.ifFirstName.getText())) {
            valid = false;
            binding.ifFirstName.setError(getString(R.string.msg_required));
        }
        if (StringUtil.isNullOrEmpty(binding.ifSurname.getText())) {
            valid = false;
            binding.ifSurname.setError(getString(R.string.msg_required));
        }
        if (StringUtil.isNullOrEmpty(binding.ifPhoneNumber.getText())) {
            valid = false;
            binding.ifPhoneNumber.setError(getString(R.string.msg_required));
        }

        return valid;
    }
}