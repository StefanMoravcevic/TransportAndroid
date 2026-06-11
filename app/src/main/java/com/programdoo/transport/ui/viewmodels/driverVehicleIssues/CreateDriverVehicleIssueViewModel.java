package com.programdoo.transport.ui.viewmodels.driverVehicleIssues;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.driverVehicleIssues.SaveDriverVehicleIssueRequestModel;
import com.programdoo.transport.data.models.dtos.masterData.MasterDataDto;
import com.programdoo.transport.data.models.dtos.travelOrders.SaveTravelOrderRequestModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.DocumentsRepository;
import com.programdoo.transport.data.repositories.DriverVehicleIssuesRepository;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.MasterDataRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.TravelOrdersRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;

@HiltViewModel
public class CreateDriverVehicleIssueViewModel extends BaseViewModel {

    private final DriverVehicleIssuesRepository driverVehicleIssuesRepository;


    private final MasterDataRepository masterDataRepository;

    private final PublishSubject<Integer> saveResultSubject = PublishSubject.create();

    @Getter
    private final LiveData<List<MasterDataDto>> vehicleDefectTypes;

    @Inject
    public CreateDriverVehicleIssueViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvent,
            DriverVehicleIssuesRepository driverVehicleIssuesRepository,
            EmployeesRepository employeesRepository,
            MasterDataRepository masterDataRepository,
            DocumentsRepository documentsRepository
    ) {

        super(preferences, session, authEvent);
        this.driverVehicleIssuesRepository = driverVehicleIssuesRepository;
        this.masterDataRepository = masterDataRepository;
        this.vehicleDefectTypes = LiveDataReactiveStreams.fromPublisher(
                masterDataRepository.GetSelectOptionsByTable("VehicleDefectTypes","Name")
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
    }

    public void saveDriverVehicleIssue(SaveDriverVehicleIssueRequestModel model) {

        disposables.add(
                driverVehicleIssuesRepository.saveDriverVehicleIssue(model)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {

                                    toastEvent.setValue(1);

                                    if (response != null) {
                                        saveResultSubject.onNext(response);
                                    }

                                },
                                throwable -> toastEvent.setValue(2)
                        )
        );
    }

    public Observable<Integer> getSaveResult() {
        return saveResultSubject.hide();
    }
}
