package com.programdoo.transport.ui.viewmodels.poolCarReservations;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.memberships.MembershipDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.PoolCarReservationRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;
import com.programdoo.transport.ui.viewmodels.ListViewModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;

@HiltViewModel
public class PoolCarReservationCalendarViewModel extends BaseViewModel implements ListViewModel{
    @Getter
    private final PoolCarReservationRepository repository;

    @Getter
    private final LiveData<List<PoolCarReservationDto>> poolCarReservations;

    @Inject
    public PoolCarReservationCalendarViewModel(PoolCarReservationRepository repository, PreferencesRepository preferences,
                                       SessionRepository session,
                                       AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.repository = repository;
        this.poolCarReservations = LiveDataReactiveStreams.fromPublisher(
                this.repository.getPoolCarReservations()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
    }
    public void refreshData() {
        this.repository.refreshPoolCarReservations();
    }
}