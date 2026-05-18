package com.programdoo.transport.ui.viewmodels.memberships;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.programdoo.transport.R;
import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.masterData.MasterDataDto;
import com.programdoo.transport.data.models.dtos.memberships.MembershipCardDto;
import com.programdoo.transport.data.models.dtos.memberships.MembershipDto;
import com.programdoo.transport.data.models.dtos.memberships.SaveMembershipRequestModel;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.MasterDataRepository;
import com.programdoo.transport.data.repositories.MembershipCardsRepository;
import com.programdoo.transport.data.repositories.MembershipsRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.TraineesRepository;
import com.programdoo.transport.databinding.FragmentEditMembershipBinding;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;
import com.programdoo.transport.ui.viewmodels.EditViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;
import lombok.Setter;
@HiltViewModel
public class EditMembershipViewModel extends BaseViewModel
    implements EditViewModel {

    @Getter @Setter
    private SaveMembershipRequestModel membership;

    @Getter @Setter
    public int traineeId;
    @Getter
    private final MembershipsRepository membershipsRepository;
    @Getter
    private final TraineesRepository traineesRepository;
    @Getter
    private final MasterDataRepository masterDataRepository;
    @Getter
    private final MembershipCardsRepository membershipCardsRepository;
    @Getter
    private final LiveData<List<MembershipCardDto>> membershipCards;

    @Getter
    private final LiveData<List<MasterDataDto>> paymentMethods;
    @Getter
    private final LiveData<List<TraineeDto>> trainees;
    @Getter
    private final LiveData<List<MasterDataDto>> currencies;

    @Getter @Setter
    private boolean isEditMode;

    @Inject
    public EditMembershipViewModel(
            MembershipsRepository membershipsRepository,
            MembershipCardsRepository membershipCardsRepository,
            MasterDataRepository masterDataRepository,
            TraineesRepository traineesRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.membershipsRepository = membershipsRepository;
        this.membershipCardsRepository = membershipCardsRepository;
        this.traineesRepository = traineesRepository;
        this.masterDataRepository = masterDataRepository;
        this.membership = new MembershipDto();

        this.membershipCards = LiveDataReactiveStreams.fromPublisher(
                membershipCardsRepository.getCards()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.paymentMethods = LiveDataReactiveStreams.fromPublisher(
                masterDataRepository.GetSelectOptionsByTable("PaymentMethods", "Name")
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.currencies = LiveDataReactiveStreams.fromPublisher(
                masterDataRepository.GetSelectOptionsByTable("Currencies", "Name")
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.trainees = LiveDataReactiveStreams.fromPublisher(
                traineesRepository.getTrainees()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
    }

    public void saveMembership(SaveMembershipRequestModel saveData) {
        handleCompletable(membershipsRepository.saveMembership(saveData),
                () -> {
                        toastEvent.setValue(R.string.msg_saved);
                        navigationEvent.setValue(1);
                },
                error -> {
                    toastEvent.setValue(R.string.msg_error_api);
                    Log.d("error", error.getMessage());
                });
    }

    public void applyInfoFromBinding(FragmentEditMembershipBinding binding) {

        String paymentDateStr = binding.paymentDate.getText();
        if (!StringUtil.isNullOrEmpty(paymentDateStr)) {
            membership.setPaymentDate(DateUtil.parse(paymentDateStr));
        }

        String validFromStr = binding.validFrom.getText();
        if (!StringUtil.isNullOrEmpty(validFromStr)) {
            membership.setValidFromDate(DateUtil.parse(validFromStr));
        }

        String validToStr = binding.validTo.getText();
        if (!StringUtil.isNullOrEmpty(validToStr)) {
            membership.setValidToDate(DateUtil.parse(validToStr));
        }

        String purchaseDateStr = binding.purchaseDate.getText();
        if (!StringUtil.isNullOrEmpty(purchaseDateStr)) {
            membership.setPurchaseDate(DateUtil.parse(purchaseDateStr));
        }

        String numberOfAppointmentsStr = binding.numberOfAppointments.getText();
        if (!StringUtil.isNullOrEmpty(numberOfAppointmentsStr)) {
                membership.setTotalSessions(Integer.parseInt(numberOfAppointmentsStr));
        }

        String priceStr = binding.price.getText();
        if (!StringUtil.isNullOrEmpty(priceStr)) {
            membership.setPrice(Double.parseDouble(priceStr));
        }

        String comment = binding.comment.getText();
        if (!StringUtil.isNullOrEmpty(comment)) {
            membership.setComment(comment);
        }
    }

    @Override
    public void clearEditData() {}
}
