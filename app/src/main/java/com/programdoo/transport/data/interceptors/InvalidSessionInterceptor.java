package com.programdoo.transport.data.interceptors;

import androidx.annotation.NonNull;

import com.programdoo.transport.data.eventbus.AuthEventBus;

import java.io.IOException;

import jakarta.inject.Inject;
import okhttp3.Interceptor;
import okhttp3.Response;

public class InvalidSessionInterceptor implements Interceptor {
    private final AuthEventBus authEvents;

    @Inject
    public InvalidSessionInterceptor(
            AuthEventBus authEvents) {
        this.authEvents = authEvents;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() == 401) {
            authEvents.unauthorized();
        }

        return response;
    }
}
