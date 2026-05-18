package com.programdoo.transport.ui.viewmodels;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MenuViewModel extends BaseViewModel {
    @Inject
    public MenuViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
    }
}
