package com.programdoo.transport.ui.viewmodels.travelOrders;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.masterData.MasterDataDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.SaveTravelOrderRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.TravelOrderDto;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.data.models.requests.travelOrders.SearchTravelOrdersParams;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.DocumentsRepository;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.MasterDataRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.TravelOrdersRepository;
import com.programdoo.transport.ui.adapters.MasterDataRecyclerViewAdapter;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;

@HiltViewModel
public class CreateTravelOrderViewModel extends BaseViewModel {
    private final TravelOrdersRepository travelOrdersRepository;
    private final DocumentsRepository documentsRepository;
    private final MasterDataRepository masterDataRepository;
    private final BehaviorSubject<Boolean> saveResultSubject =
            BehaviorSubject.create();

    private final EmployeesRepository employeesRepository;

    @Getter
    private final MutableLiveData<String> newDocumentNumber = new MutableLiveData<>();

    @Getter
    private final MutableLiveData<Boolean> numberLoading = new MutableLiveData<>();

    @Getter
    private final LiveData<List<MasterDataDto>> transportationVehicles;

    @Getter
    private final LiveData<List<MasterDataDto>> states;
    @Getter
    private final LiveData<List<MasterDataDto>> travelOrderStatuses;

    private final BehaviorSubject<List<EmployeeDto>> employeesSubject =
            BehaviorSubject.create();

    @Getter
    private final io.reactivex.rxjava3.core.Observable<List<EmployeeDto>> employees =
            employeesSubject.hide();
    @Inject
    public CreateTravelOrderViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvent,
            TravelOrdersRepository travelOrdersRepository,
            EmployeesRepository employeesRepository,
            MasterDataRepository masterDataRepository,
            DocumentsRepository documentsRepository
    ) {

        super(preferences, session, authEvent);
        this.travelOrdersRepository = travelOrdersRepository;
        this.documentsRepository = documentsRepository;
        this.employeesRepository = employeesRepository;
        this.masterDataRepository = masterDataRepository;
        this.transportationVehicles = LiveDataReactiveStreams.fromPublisher(
                masterDataRepository.GetSelectOptionsByTable("TransportationVehicles","Name")
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.states = LiveDataReactiveStreams.fromPublisher(
                masterDataRepository.GetSelectOptionsByTable("States", "Name")
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.travelOrderStatuses = LiveDataReactiveStreams.fromPublisher(
                masterDataRepository.GetSelectOptionsByTable("TravelOrderStatuses", "Name")
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        loadEmployees();
    }

    public void saveTravelOrder(SaveTravelOrderRequestModel model) {

        handleCompletable(
                travelOrdersRepository.saveTravelOrder(model),
                () -> {
                    toastEvent.setValue(1);
                    saveResultSubject.onNext(true);
                },
                throwable -> {
                    toastEvent.setValue(2);
                    android.util.Log.e("SAVE_TRAVEL_ORDER", "Save failed", throwable);
                }
        );
    }

    public void loadEmployees() {

        employeesRepository.searchEmployees(
                new SearchEmployeesParams()
        );

        disposables.add(
                employeesRepository.getEmployees()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {

                            if (response != null) {
                                employeesSubject.onNext(response.getPayload());
                            }
                        }, Throwable::printStackTrace)
        );
    }
    public Observable<String> getNewDocumentNumber(int documentSerieTypeId, int orgUnitId) {
        return documentsRepository.getNewNumber(documentSerieTypeId, orgUnitId)
                .map(response -> {
                    if (response == null || response.getPayload() == null) {
                        throw new RuntimeException("Invalid response");
                    }
                    return response.getPayload();
                });
    }



    public Observable<Boolean> getSaveResult() {
        return saveResultSubject.hide();
    }
}
