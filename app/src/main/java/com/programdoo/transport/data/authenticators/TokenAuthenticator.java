package com.programdoo.transport.data.authenticators;

import com.programdoo.transport.data.models.dtos.AuthTokensDto;
import com.programdoo.transport.data.models.dtos.SaveRefreshTokenRequestModel;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.services.AuthService;
import com.programdoo.transport.utils.Constants;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Route;
import retrofit2.Response;

public class TokenAuthenticator implements Authenticator {
    private final PreferencesRepository preferences;
    private final AuthService service;

    private final Object lock = new Object();

    @Inject
    TokenAuthenticator(
            PreferencesRepository preferences,
            AuthService service) {
        this.preferences = preferences;
        this.service = service;
    }

    @Override
    public Request authenticate(Route route, okhttp3.Response response) throws IOException {
        if (responseCount(response) >= 2) {
            preferences.clearTokens();
            return null;
        }

        synchronized (lock) {
            String currentToken = preferences.getString(Constants.KEY_ACCESS_TOKEN);
            String requestToken = response.request().header("Authorization");

            if (requestToken != null && requestToken.equals("Bearer " + currentToken)) {
                String refreshToken = preferences.getString(Constants.KEY_REFRESH_TOKEN);

                if (refreshToken == null) {
                    preferences.clearTokens();
                    return null;
                }

                try {
                    SaveRefreshTokenRequestModel refreshTokenModel = new SaveRefreshTokenRequestModel();
                    refreshTokenModel.token = refreshToken;
                    Response<ResponseModel<AuthTokensDto>> refreshResponse =
                            service.loginWithRefreshToken(refreshTokenModel).execute();

                    if (!refreshResponse.isSuccessful() || refreshResponse.body() == null) {
                        preferences.clearTokens();
                        return null;
                    }

                    ResponseModel<AuthTokensDto> result = refreshResponse.body();
                    String newAccessToken = result.getPayload().getAccessToken();
                    String newRefreshToken = result.getPayload().getRefreshToken();

                    preferences.saveTokens(newAccessToken, newRefreshToken);
                }
                catch (Exception e) {
                    preferences.clearTokens();
                    return null;
                }
            }

            return response.request().newBuilder()
                    .header("Authorization",
                            "Bearer " + preferences.getString(Constants.KEY_ACCESS_TOKEN))
                    .build();
        }
    }

    private int responseCount(okhttp3.Response response) {
        int count = 1;
        while ((response = response.priorResponse()) != null) {
            count++;
        }
        return count;
    }
}
