package com.programdoo.transport.ui.viewmodels.settings;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EditSettingsViewModel extends BaseViewModel {

    @Inject
    EditSettingsViewModel(
            PreferencesRepository preferencesRepository,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferencesRepository, session, authEvents);
    }
}
