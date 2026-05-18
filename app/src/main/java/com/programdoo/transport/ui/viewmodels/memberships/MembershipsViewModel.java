package com.programdoo.transport.ui.viewmodels.memberships;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.programdoo.transport.R;
import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.dtos.memberships.MembershipDto;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.CompaniesRepository;
import com.programdoo.transport.data.repositories.MembershipsRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.TraineesRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;
import com.programdoo.transport.ui.viewmodels.ListViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;
import lombok.Setter;

@HiltViewModel
public class MembershipsViewModel extends BaseViewModel
    implements ListViewModel {
    @Getter
    private final MembershipsRepository membershipsRepository;
    @Getter
    private final TraineesRepository traineesRepository;
    @Getter
    private final CompaniesRepository companiesRepository;

    @Getter @Setter
    private Integer traineeId = null;

    @Getter
    private final LiveData<List<MembershipDto>> memberships;
    @Getter
    private final LiveData<TraineeDto> trainee;
    @Getter
    private final LiveData<List<OrgUnitDto>> orgUnits;
    @Getter
    private final LiveData<Object> refreshMembershipsCompleted;

    @Getter @Setter
    private Integer currentOrgUnit = null;
    @Getter @Setter
    private Boolean active;

    @Inject
    public MembershipsViewModel(
            MembershipsRepository membershipsRepository,
            TraineesRepository traineesRepository,
            CompaniesRepository companiesRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.membershipsRepository = membershipsRepository;
        this.traineesRepository = traineesRepository;
        this.companiesRepository = companiesRepository;
        this.memberships = LiveDataReactiveStreams.fromPublisher(
                this.membershipsRepository.getMemberships()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.trainee = LiveDataReactiveStreams.fromPublisher(
                this.traineesRepository.getTrainee()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModel::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.orgUnits = LiveDataReactiveStreams.fromPublisher(
                this.companiesRepository.getOrgUnits()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.refreshMembershipsCompleted = LiveDataReactiveStreams.fromPublisher(
                this.membershipsRepository.getRefreshMembershipsCompleted()
                        .toFlowable(BackpressureStrategy.BUFFER));

        if (session.isUserClient())
            setTraineeId(session.getUser().getEntityId());
    }

    public void deleteMembership(int id, int userId) {
        handleCompletable(membershipsRepository.deleteMembership(id, userId),
                () -> toastEvent.setValue(R.string.msg_delete_success),
                error -> {
                    toastEvent.setValue(R.string.msg_error_api);
                    Log.d("error", error.getMessage());
                });
    }

    @Override
    public void refreshData() {
        this.membershipsRepository.refreshMemberships();
    }
}