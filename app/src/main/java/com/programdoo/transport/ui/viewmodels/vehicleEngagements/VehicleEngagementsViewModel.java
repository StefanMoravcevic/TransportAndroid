package com.programdoo.transport.ui.viewmodels.vehicleEngagements;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.dtos.vehicleEngagements.VehicleEngagementDto;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.requests.vehicleEngagements.SearchVehicleEngagementsParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.PoolCarReservationRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.VehicleEngagementsRepository;
import com.programdoo.transport.ui.pages.poolCarReservations.ReservationListItem;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class VehicleEngagementsViewModel extends BaseViewModel {
    private final VehicleEngagementsRepository repository;

    @Inject
    public VehicleEngagementsViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvent,
            VehicleEngagementsRepository vehicleEngagementsRepository
    ) {
        super(preferences, session, authEvent);
        this.repository = vehicleEngagementsRepository;

    }

    public void searchVehicleEngagements(SearchVehicleEngagementsParams params) {
        repository.searchVehicleEngagements(params);
    }

    public Observable<ResponseModelList<VehicleEngagementDto>> getVehicleEngagements() {
        return repository.getVehicleEngagements();
    }

    public List<ReservationListItem> mapVehicleEngagement(List<VehicleEngagementDto> list) {

        List<ReservationListItem> result = new ArrayList<>();

        for (VehicleEngagementDto dto : list) {

            result.add(new ReservationListItem(
                    dto.getEmployee(),
                    dto.getVehicle(),
                    dto.getIndebtednessDateFormatted(),
                    dto.getDivorceDateFormatted(),
                    dto.getDivorceNote()
            ));
        }

        return result;
    }

    public void refresh() {
        repository.refreshVehicleEngagements();
    }
}
