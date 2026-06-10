package com.programdoo.transport.ui.viewmodels.poolCarReservations;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.dtos.scannedpackages.SaveScannedPackagesRequestModel;
import com.programdoo.transport.data.models.dtos.vehicles.VehicleDto;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.data.models.requests.vehicles.SearchVehicleParams;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.PoolCarReservationRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.VehiclesRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;

@HiltViewModel
public class CreatePoolCarReservationViewModel extends BaseViewModel {

    @Getter
    private final EmployeesRepository employeesRepository;
    private final VehiclesRepository vehiclesRepository;
    private final PoolCarReservationRepository poolCarReservationRepository;

    private final BehaviorSubject<Boolean> saveResultSubject =
            BehaviorSubject.create();

    private final CompositeDisposable disposables = new CompositeDisposable();

    // STATE
    private final BehaviorSubject<List<EmployeeDto>> employeesSubject =
            BehaviorSubject.create();


    private final BehaviorSubject<List<VehicleDto>> vehiclesSubject =
            BehaviorSubject.create();

    @Getter
    private final io.reactivex.rxjava3.core.Observable<List<VehicleDto>> vehicles =
            vehiclesSubject.hide();

    @Getter
    private final io.reactivex.rxjava3.core.Observable<List<EmployeeDto>> employees =
            employeesSubject.hide();

    @Inject
    public CreatePoolCarReservationViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvent,
            PoolCarReservationRepository poolCarReservationRepository,
            EmployeesRepository employeesRepository,
            VehiclesRepository vehiclesRepository
    ) {
        super(preferences, session, authEvent);
        this.employeesRepository = employeesRepository;
        this.vehiclesRepository = vehiclesRepository;
        this.poolCarReservationRepository = poolCarReservationRepository;

        observeEmployees();
        observeVehicles();
    }

    // ---------------- LOAD ----------------

    public void loadEmployees() {
        employeesRepository.searchEmployees(new SearchEmployeesParams());
    }

    public void loadVehicles() {

        SearchVehicleParams params = new SearchVehicleParams();

        params.IsPoolCar = true;

        vehiclesRepository.searchVehicles(params);
    }

    // ---------------- OBSERVE ----------------

    private void observeEmployees() {
        disposables.add(
                employeesRepository.getEmployees()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {

                            if (response != null) {
                                employeesSubject.onNext(response.getPayload());
                            }

                        }, throwable -> {
                            throwable.printStackTrace();
                        })
        );
    } private void observeVehicles() {
        disposables.add(
                vehiclesRepository.getVehicles()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {

                            Log.d("API_VEHICLES", "FULL RESPONSE: " + response);

                            if (response != null) {
                                Log.d("API_VEHICLES", "PAYLOAD SIZE: " + response.getPayload().size());

                                vehiclesSubject.onNext(response.getPayload());
                            }

                        }, throwable -> {
                            throwable.printStackTrace();
                        })
        );
    }

    // ---------------- CLEANUP ----------------

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }

    public void savePoolCarReservation(SavePoolCarReservationRequestModel model) {

        handleCompletable(
                poolCarReservationRepository.savePoolCarReservations(model),
                () -> {
                    toastEvent.setValue(1);
                    saveResultSubject.onNext(true);
                },
                throwable -> {
                    toastEvent.setValue(2);
                    android.util.Log.e("SCAN_PACKAGE", "Save failed", throwable);
                }
        );
    }

    public Observable<Boolean> getSaveResult() {
        return saveResultSubject.hide();
    }
}