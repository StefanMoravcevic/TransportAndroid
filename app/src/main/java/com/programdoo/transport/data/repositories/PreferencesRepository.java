package com.programdoo.transport.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.programdoo.transport.utils.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class PreferencesRepository {
    private final SharedPreferences sp;

    @Inject
    public PreferencesRepository(
            @ApplicationContext Context context) {
        sp = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
    }

    public void setString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }
    public void setInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    public String getString(String key) {
        return sp.getString(key, null);
    }
    public int getInt(String key) {
        return sp.getInt(key, 0);
    }
    public void saveTokens(String access, String refresh) {
        sp.edit()
                .putString(Constants.KEY_ACCESS_TOKEN, access)
                .putString(Constants.KEY_REFRESH_TOKEN, refresh)
                .apply();
    }
    public void clearTokens() {
        sp.edit()
                .remove(Constants.KEY_ACCESS_TOKEN)
                .remove(Constants.KEY_REFRESH_TOKEN)
                .apply();
    }
}
