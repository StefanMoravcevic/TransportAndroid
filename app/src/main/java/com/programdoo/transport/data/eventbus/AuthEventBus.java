package com.programdoo.transport.data.eventbus;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

@Singleton
public class AuthEventBus {
    private final PublishSubject<AuthEvent> events = PublishSubject.create();

    @Inject
    AuthEventBus() {}

    public Observable<AuthEvent> events() {
        return events.hide();
    }

    public void unauthorized() {
        events.onNext(AuthEvent.UNAUTHORIZED);
    }

    public enum AuthEvent {
        UNAUTHORIZED
    }
}
