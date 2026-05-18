package com.programdoo.transport.data.interceptors;

import android.content.Context;

import java.io.IOException;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.settings.Settings;
import com.programdoo.transport.utils.Constants;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import lombok.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthHeaderInterceptor implements Interceptor {
    private Context ctx;
    private PreferencesRepository preferences;

    @Inject
    public AuthHeaderInterceptor(
            @ApplicationContext Context ctx,
            PreferencesRepository preferences) {
        this.ctx = ctx;
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request req = chain.request();
        Request.Builder builder = req.newBuilder()
                .header("Content-Type", Settings.MIME_JSON_UTF8)
                .method(req.method(), req.body());
        setRequestHeader(builder);
        return chain.proceed(builder.build());
    }

    private void setRequestHeader(Request.Builder builder) {
        String token = preferences.getString(Constants.KEY_ACCESS_TOKEN);

        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
    }
}
