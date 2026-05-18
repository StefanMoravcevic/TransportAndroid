package com.programdoo.transport.ui.viewmodels.login;

import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.programdoo.transport.R;
import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.LoggedUserModel;
import com.programdoo.transport.data.models.requests.accounts.TokenRequestModel;
import com.programdoo.transport.data.repositories.AccountsRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.utils.SingleLiveEvent;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@HiltViewModel
public class LoginViewModel extends BaseViewModel {
    private final AccountsRepository accountsRepository;
    @Getter
    private final SingleLiveEvent<Integer> intentEvent;

    @Inject
    public LoginViewModel(
            AccountsRepository accountsRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.accountsRepository = accountsRepository;
        intentEvent = new SingleLiveEvent<>();
    }

    public void login(TokenRequestModel tokenModel) {
        consumeApi(accountsRepository.generateToken(tokenModel),
                result -> {
                    if (result.isValid()) {
                        JWT jwt = new JWT(result.getPayload());
                        session.setUser(LoggedUserModel.createFromJWT(jwt));
                        intentEvent.setValue(1);
                    }
                    else {
                        toastEvent.setValue(R.string.msg_error_response);
                    }
                },
                error -> {
                    Log.d("error", error.getMessage());
                    toastEvent.setValue(R.string.msg_error_api);
                });
    }
}
