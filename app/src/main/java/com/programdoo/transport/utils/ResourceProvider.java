package com.programdoo.transport.utils;

import android.content.Context;

import androidx.annotation.StringRes;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class ResourceProvider {
    @ApplicationContext
    private Context ctx;

    @Inject
    public ResourceProvider(
            @ApplicationContext Context ctx) {
        this.ctx = ctx;
    }

    public String getString(@StringRes int resId) {
        return ctx.getString(resId);
    }
}
