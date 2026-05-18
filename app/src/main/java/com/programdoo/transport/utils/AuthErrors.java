package com.programdoo.transport.utils;

import retrofit2.HttpException;

import java.io.IOException;

public final class AuthErrors {

    private AuthErrors() { /* utility class */ }

    /**
     * Returns true if this error represents a session expiration (both access and refresh tokens invalid)
     * and the app should navigate to login.
     *
     * @param error the Throwable emitted by Retrofit / RxJava
     * @return true if session expired
     */
    public static boolean isSessionExpired(Throwable error) {
        // Retrofit emits HttpException on HTTP errors
        if (error instanceof HttpException) {
            HttpException httpEx = (HttpException) error;

            // 401 Unauthorized → only consider it "session expired" if refresh failed
            // Your Authenticator should have cleared tokens when refresh fails
            if (httpEx.code() == 401) {
                return true;
            }
        }

        // Optionally handle other transport-level errors
        if (error instanceof IOException) {
            // Network error, don't treat as session expired
            return false;
        }

        // Unknown / non-HTTP errors → assume not session-related
        return false;
    }
}
