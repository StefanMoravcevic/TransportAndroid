package com.programdoo.transport.ui.viewmodels.travelOrders;


import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.dtos.travelOrders.TravelOrderDto;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.requests.travelOrders.SearchTravelOrdersParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.PoolCarReservationRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.TravelOrdersRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class TravelOrdersListViewModel extends BaseViewModel {
    private final TravelOrdersRepository travelOrdersRepository;
    @Inject
    public TravelOrdersListViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvent,
            TravelOrdersRepository travelOrdersRepository
    ) {
        super(preferences, session, authEvent);
        this.travelOrdersRepository = travelOrdersRepository;

    }

    public void searchTravelOrders(SearchTravelOrdersParams params) {
        travelOrdersRepository.searchTravelOrders(params);
    }

    public Observable<ResponseModelList<TravelOrderDto>> getTravelOrders() {
        return travelOrdersRepository.getTravelOrders();
    }
}
