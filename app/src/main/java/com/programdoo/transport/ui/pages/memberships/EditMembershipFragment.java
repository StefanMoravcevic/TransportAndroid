package com.programdoo.transport.ui.pages.memberships;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.masterData.MasterDataDto;
import com.programdoo.transport.data.models.dtos.memberships.MembershipCardDto;
import com.programdoo.transport.data.models.dtos.memberships.SaveMembershipRequestModel;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.requests.memberships.SearchMembershipCardsParams;
import com.programdoo.transport.data.models.requests.trainees.SearchTraineesParams;
import com.programdoo.transport.databinding.FragmentEditMembershipBinding;
import com.programdoo.transport.ui.adapters.MasterDataRecyclerViewAdapter;
import com.programdoo.transport.ui.adapters.MembershipCardsRecyclerViewAdapter;
import com.programdoo.transport.ui.adapters.TraineesRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.memberships.EditMembershipViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditMembershipFragment extends BaseFragment {

    private EditMembershipViewModel viewModel;
    private FragmentEditMembershipBinding binding;

    private MembershipCardsRecyclerViewAdapter membershipCardAdapter;
    private TraineesRecyclerViewAdapter traineeAdapter;
    private MasterDataRecyclerViewAdapter masterDataAdapterPaymentMethod;
    private MasterDataRecyclerViewAdapter masterDataAdapterCurrency;

    @Override public String TAG() { return Constants.FRAG_EDIT_MEMBERSHIP; }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentEditMembershipBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        membershipCardAdapter = new MembershipCardsRecyclerViewAdapter(requireContext(), new ArrayList<>());
        traineeAdapter = new TraineesRecyclerViewAdapter(requireContext(), new ArrayList<>());
        masterDataAdapterPaymentMethod = new MasterDataRecyclerViewAdapter(requireContext(), new ArrayList<>());
        masterDataAdapterCurrency = new MasterDataRecyclerViewAdapter(requireContext(), new ArrayList<>());

        viewModel = new ViewModelProvider(this).get(EditMembershipViewModel.class);

        UiUtil.selectSetup(requireContext(), membershipCardAdapter, binding.selMembershipCard, (view1, position, item) -> {
            binding.selMembershipCard.toggleSelected(position);

            if (item instanceof MembershipCardDto) {
                viewModel.getMembership().setCardId(((MembershipCardDto) item).getId());
            }
        });

        UiUtil.selectSetup(getContext(), masterDataAdapterPaymentMethod, binding.selPaymentMethod, (view1, position, item) -> {
            binding.selPaymentMethod.toggleSelected(position);

            if (item instanceof MasterDataDto) {
                viewModel.getMembership().setPaymentMethodId(((MasterDataDto) item).getValue());
            }
        });

        UiUtil.selectSetup(getContext(), masterDataAdapterCurrency, binding.selCurrency, (view1, position, item) -> {
            binding.selCurrency.toggleSelected(position);

            if (item instanceof MasterDataDto) {
                viewModel.getMembership().setCurrencyId(((MasterDataDto) item).getValue());
            }
        });

        UiUtil.selectSetup(getContext(), traineeAdapter, binding.selTrainees, (view1, position, item) -> {
            binding.selTrainees.toggleSelected(position);
            if (item instanceof TraineeDto) {
                viewModel.getMembership().setTraineeId(((TraineeDto) item).getId());
            }
        });

        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.FRAG_EDIT_MEMBERSHIP)) {
                SaveMembershipRequestModel membership =
                        (SaveMembershipRequestModel) getArguments().getSerializable(Constants.FRAG_EDIT_MEMBERSHIP);
                if (membership != null) {
                    viewModel.setMembership(membership);
                    requireActivity().setTitle("Izmeni članarinu");
                    applyMembershipToBinding(membership);
                    viewModel.setMembership(membership);
                }
            } else if (getArguments().containsKey(Constants.ARG_TRAINEE_ID)) {
                int traineeId = getArguments().getInt(Constants.ARG_TRAINEE_ID);
                viewModel.setTraineeId(traineeId);
                requireActivity().setTitle("Nova članarina");
            }
        } else {
            requireActivity().setTitle("Nova članarina");
        }
        viewModel.getMembershipCards().observe(getViewLifecycleOwner(), membershipCards -> {
            membershipCardAdapter.setData(membershipCards);

            int cardId = viewModel.getMembership().getCardId();
            if (cardId != 0) {
                for (int i = 0; i < membershipCards.size(); i++) {
                    if (membershipCards.get(i).getId() == cardId) {
                        binding.selMembershipCard.toggleSelected(i);
                        break;
                    }
                }
            }
        });

        viewModel.getPaymentMethods().observe(getViewLifecycleOwner(), paymentMethods -> {
            masterDataAdapterPaymentMethod.setData(paymentMethods);

            Integer paymentMethodId = viewModel.getMembership().getPaymentMethodId();
            if (paymentMethodId != null && paymentMethodId != 0) {
                for (int i = 0; i < paymentMethods.size(); i++) {
                    if (paymentMethods.get(i).getValue() == paymentMethodId) {
                        binding.selPaymentMethod.toggleSelected(i);
                        break;
                    }
                }
            }
        });

        viewModel.getCurrencies().observe(getViewLifecycleOwner(), currencies -> {
            masterDataAdapterCurrency.setData(currencies);

            Integer currencyId = viewModel.getMembership().getCurrencyId();
            if (currencyId != null && currencyId != 0) {
                for (int i = 0; i < currencies.size(); i++) {
                    if (currencies.get(i).getValue() == currencyId) {
                        binding.selCurrency.toggleSelected(i);
                        break;
                    }
                }
            }
        });

        viewModel.getTrainees().observe(getViewLifecycleOwner(), trainees -> {
            traineeAdapter.setData(trainees);

            if (viewModel.getMembership() != null && viewModel.getMembership().getTraineeId() != 0) {
                int traineeId = viewModel.getMembership().getTraineeId();
                for (int i = 0; i < trainees.size(); i++) {
                    if (trainees.get(i).getId() == traineeId) {
                        binding.selTrainees.toggleSelected(i);
                        break;
                    }
                }
            }
            else if (getArguments() != null && getArguments().containsKey(Constants.ARG_TRAINEE_ID)) {
                int traineeId = getArguments().getInt(Constants.ARG_TRAINEE_ID);
                for (int i = 0; i < trainees.size(); i++) {
                    if (trainees.get(i).getId() == traineeId) {
                        binding.selTrainees.toggleSelected(i);
                        break;
                    }
                }
            }
        });

        binding.buttonSave.setOnClickListener(v -> saveMembership());
        binding.buttonCancel.setOnClickListener(v -> cancel());

        viewModel.getToastEvent().observe(getViewLifecycleOwner(), msg
                -> UiUtil.makeToast(requireActivity(), requireContext(), getString(msg)));

        viewModel.getMembershipCardsRepository().searchMembershipCards(new SearchMembershipCardsParams());
        viewModel.getTraineesRepository().searchTrainees(new SearchTraineesParams());
//        viewModel.getMasterDataRepository().getPaymentMethods();
//        viewModel.getCurrenciesForSelect("Currencies","Name");
    }

    private void saveMembership() {
        viewModel.applyInfoFromBinding(binding);

        if (viewModel.getMembership().getTraineeId() == 0 &&
                getArguments() != null && getArguments().containsKey(Constants.ARG_TRAINEE_ID)) {
            int traineeId = getArguments().getInt(Constants.ARG_TRAINEE_ID);
            viewModel.getMembership().setTraineeId(traineeId);
        }

        viewModel.saveMembership(viewModel.getMembership());

        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void cancel() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void applyMembershipToBinding(SaveMembershipRequestModel membership) {
        if (membership == null) return;

        if (membership.getPaymentDate() != null) {
            binding.paymentDate.setText(DateUtil.format(membership.getPaymentDate()));
        }
        if (membership.getValidFromDate() != null) {
            binding.validFrom.setText(DateUtil.format(membership.getValidFromDate()));
        }
        if (membership.getValidToDate() != null) {
            binding.validTo.setText(DateUtil.format(membership.getValidToDate()));
        }
        if (membership.getPurchaseDate() != null) {
            binding.purchaseDate.setText(DateUtil.format(membership.getPurchaseDate()));
        }

        if (membership.getTotalSessions() > 0) {
            binding.numberOfAppointments.setText(String.valueOf(membership.getTotalSessions()));
        }
        if (membership.getPrice() != null) {
            binding.price.setText(String.valueOf(membership.getPrice()));
        }
        if (membership.getComment() != null) {
            binding.comment.setText(membership.getComment());
        }




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}