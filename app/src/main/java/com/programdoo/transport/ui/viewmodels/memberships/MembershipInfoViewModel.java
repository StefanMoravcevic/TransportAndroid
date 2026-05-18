package com.programdoo.transport.ui.viewmodels.memberships;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.memberships.MembershipDto;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.repositories.MembershipsRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;

@HiltViewModel
public class MembershipInfoViewModel extends BaseViewModel {
    @Getter
    private final MembershipsRepository membershipsRepository;

    @Getter
    private LiveData<MembershipDto> membership;

    @Inject
    public MembershipInfoViewModel(
            MembershipsRepository membershipsRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.membershipsRepository = membershipsRepository;
        membership = LiveDataReactiveStreams.fromPublisher(
                membershipsRepository.getMembership()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModel::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
    }
}
