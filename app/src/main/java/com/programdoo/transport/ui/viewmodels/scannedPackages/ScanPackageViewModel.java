package com.programdoo.transport.ui.viewmodels.scannedPackages;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.scannedpackages.SaveScannedPackagesRequestModel;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.ScannedPackagesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Completable;
import jakarta.inject.Inject;

@HiltViewModel
public class ScanPackageViewModel extends BaseViewModel {

    private final ScannedPackagesRepository repository;

    @Inject
    public ScanPackageViewModel(
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

    public int getLoggedUserId() {
        return session.getUserId();
    }
}