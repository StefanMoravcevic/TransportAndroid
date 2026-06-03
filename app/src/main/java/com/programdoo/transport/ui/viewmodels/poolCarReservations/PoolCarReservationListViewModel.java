package com.programdoo.transport.ui.viewmodels.poolCarReservations;


import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.PoolCarReservationRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.VehiclesRepository;
import com.programdoo.transport.ui.pages.poolCarReservations.ReservationListItem;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class PoolCarReservationListViewModel extends BaseViewModel {
    private final PoolCarReservationRepository poolCarReservationRepository;
    @Inject
    public PoolCarReservationListViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvent,
            PoolCarReservationRepository poolCarReservationRepository
    ) {
        super(preferences, session, authEvent);
        this.poolCarReservationRepository = poolCarReservationRepository;

    }

    public void searchPoolCarReservations(SearchPoolCarReservationParams params) {
        poolCarReservationRepository.searchPoolCarReservations(params);
    }

    public Observable<ResponseModelList<PoolCarReservationDto>> getPoolCarReservations() {
        return poolCarReservationRepository.getPoolCarReservations();
    }
    public List<ReservationListItem> mapPoolCar(List<PoolCarReservationDto> list) {

        List<ReservationListItem> result = new ArrayList<>();

        for (PoolCarReservationDto dto : list) {

            result.add(new ReservationListItem(
                    dto.getEmployee(),
                    dto.getVehicle(),
                    dto.getDateFromFormatted(),
                    dto.getDateToFormatted()
            ));
        }

        return result;
    }

    public void refresh() {
        poolCarReservationRepository.refreshPoolCarReservations();
    }

}
