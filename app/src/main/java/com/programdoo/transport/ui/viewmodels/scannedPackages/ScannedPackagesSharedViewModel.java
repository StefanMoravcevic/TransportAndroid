package com.programdoo.transport.ui.viewmodels.scannedPackages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.dtos.scannedpackages.SaveScannedPackagesRequestModel;
import com.programdoo.transport.data.models.dtos.scannedpackages.ScannedPackageDto;
import com.programdoo.transport.data.models.requests.poolCarReservations.SearchPoolCarReservationParams;
import com.programdoo.transport.data.models.requests.scannedPackages.SearchScannedPackagesParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.ScannedPackagesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.List;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;
import jakarta.inject.Inject;
@HiltViewModel
public class ScannedPackagesSharedViewModel extends BaseViewModel {
    // LiveData za trenutni paket
    private final MutableLiveData<Integer> selectedPackageId = new MutableLiveData<>();
    private final ScannedPackagesRepository repository;
    public final MutableLiveData<List<Integer>> packageIdsLiveData = new MutableLiveData<>();

    public LiveData<List<Integer>> getPackageIdsLiveData() {
        return packageIdsLiveData;
    }
    @Inject
    public ScannedPackagesSharedViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvent,
            ScannedPackagesRepository repository) {

        super(preferences, session, authEvent);

        this.repository = repository;
    }

    public void saveScannedPackage(SaveScannedPackagesRequestModel model) {

        handleCompletable(
                repository.saveScannedPackages(model),
                () -> toastEvent.setValue(1),
                throwable -> {
                    toastEvent.setValue(2);
                    android.util.Log.e("SCAN_PACKAGE", "Save failed", throwable);
                }
        );
    }
    public void searchScannedPackages(SearchScannedPackagesParams params) {
        repository.searchScannedPackages(params);
    }

    public int getLoggedUserId() {
        return session.getUserId();
    }
    public void setPackageId(int id) {
        selectedPackageId.setValue(id);
    }

    public LiveData<Integer> getSelectedPackageId() {
        return selectedPackageId;
    }
    public Observable<ResponseModelList<ScannedPackageDto>> getScannedPackages() {
        return repository.getScannedPackages();
    }
}
