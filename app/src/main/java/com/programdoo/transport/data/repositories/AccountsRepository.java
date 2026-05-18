package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.models.dtos.AuthTokensDto;
import com.programdoo.transport.data.services.AccountsService;
import com.programdoo.transport.data.models.requests.accounts.TokenRequestModel;
import com.programdoo.transport.data.models.responses.ResponseModel;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class AccountsRepository {
    private final AccountsService accountsService;

    @Inject
    public AccountsRepository(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    public Observable<ResponseModel<String>> generateToken(TokenRequestModel token) {
        return accountsService.generateToken(token);
    }
}
