package com.programdoo.transport.ui.viewmodels.receipts;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.dtos.receipts.ReceiptModel;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.PoolCarReservationRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.ReceiptsRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.VehiclesRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;

@HiltViewModel
public class ReceiptsViewModel extends BaseViewModel {

    private final ReceiptsRepository receiptsRepository;

    @Getter
    private final BehaviorSubject<Boolean> saveResultSubject =
            BehaviorSubject.create();


    @Inject
    public ReceiptsViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvent,
            ReceiptsRepository receiptsRepository
    ) {
        super(preferences, session, authEvent);
        this.receiptsRepository = receiptsRepository;

    }
    public void parseSufAndSave(ReceiptModel model) {

        handleCompletable(
                receiptsRepository.parseSuf(model),
                () -> {
                    toastEvent.setValue(1);
                },
                throwable -> {
                    toastEvent.setValue(2);
                    android.util.Log.e("ParseSuf", "Save failed", throwable);
                }
        );
    }
}
