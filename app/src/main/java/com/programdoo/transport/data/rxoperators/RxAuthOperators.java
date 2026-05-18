package com.programdoo.transport.data.rxoperators;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.responses.ResponseModelBase;

import io.reactivex.rxjava3.core.Observable;

public final class RxAuthOperators {
    public static <T extends ResponseModelBase> Observable<T> handleAuth(
            Observable<T> source,
            AuthEventBus authEvents) {
        return source.doOnNext(response -> {
            if (!response.isValid()) {
                authEvents.unauthorized();
            }
        });
    }
}
