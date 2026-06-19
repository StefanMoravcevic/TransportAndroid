package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.dtos.receipts.ReceiptModel;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.PoolCarReservationsService;
import com.programdoo.transport.data.services.ReceiptsService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Completable;

@ActivityRetainedScoped
public class ReceiptsRepository {

    private final ReceiptsService service;

    @Inject
    public ReceiptsRepository(
            ReceiptsService service) {
        this.service = service;
    }

    public Completable parseSuf(ReceiptModel model) {
        return service.parseSuf(model)
                .flatMapCompletable(result -> {
                    if (result.isValid()) return Completable.complete();
                    else return Completable.error(new RuntimeException(result.getMessage()));
                });
    }
}
