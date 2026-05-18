package com.programdoo.transport.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.programdoo.transport.data.authenticators.TokenAuthenticator;
import com.programdoo.transport.data.interceptors.AuthHeaderInterceptor;
import com.programdoo.transport.data.interceptors.InvalidSessionInterceptor;
import com.programdoo.transport.data.models.jsonadapters.LocalTimeJsonAdapter;
import com.programdoo.transport.data.settings.Settings;
import com.programdoo.transport.data.models.jsonadapters.LocalDateTimeJsonAdapter;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * u ovoj klasi se registruju komponente za komunikaciju sa api-jem.
 * ovde bi se dodala nova funkcija samo ako se doda nova 3rd party biblioteka
 * za rad sa api-jem.
 */
@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    /**
     *
     * @param authHeaderInterceptor
     * @return build-ovan okHttp klijent koji ce retrofit da koristi za slanje poruka
     * <p>
     *     ukoliko se pravi nov interceptor, ovde se registruje putem <b>.addInterceptor</b> metode.
     * </p>
     */
    @Provides @Singleton @Authenticated
    public OkHttpClient provideAuthenticatedOkHttpClient(
            AuthHeaderInterceptor authHeaderInterceptor,
            InvalidSessionInterceptor invalidSessionInterceptor,
            TokenAuthenticator tokenAuthenticator) {
        return new OkHttpClient.Builder()
                .addInterceptor(authHeaderInterceptor)
                .addInterceptor(invalidSessionInterceptor)
                .authenticator(tokenAuthenticator)
                .build();
    }

    @Provides @Singleton @Unauthenticated
    public OkHttpClient provideUnauthenticatedOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    /**
     *
     * @param dateAdapter json adapter za LocalDateTime
     * @params timeAdapter json adapter za LocalTime
     * @return build-ovan Gson objekat koji okHttp klijent koristi za serijalizaciju i
     * deserijalizaciju
     * <p>
     *     ukoliko ima potrebe za pravljenjem serijalizatora za neke tipove, kao sto ovde postoji
     *     za Date, dodaje se ovde putem <b>.registerTypeAdapter</b>
     * </p>
     */
    @Provides @Singleton
    public Gson provideGson(
            LocalDateTimeJsonAdapter dateAdapter,
            LocalTimeJsonAdapter timeAdapter) {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, dateAdapter)
                .registerTypeAdapter(LocalTime.class, timeAdapter)
                .create();
    }

    /**
     *
     * @param client OkHttpClient, za slanje poruka
     * @param gson Gson, za serijalizaciju
     * @return build-ovan retrofit koji se koristi u NetworkModule za pravljenje servisa
     * <p>
     *     ukoliko se menja API url, menja se ovde u <b>.baseUrl</b> <br>
     *     <b>.addConverterFactory</b> dobija serijalizator i prosledjuje ga OkHttp-u <br>
     *     <b>.addCallAdapterFactory</b> dobija adapter za return type. Po default-u, OkHttp vraca
     *     Call interfejs, gde je potrebno override-ovati onResponse i onFailure metode. RxJava3
     *     vraca Observable, koji ima bolji threading i izbegava callback hell jer ima bolji chaining.
     * </p>
     */
    @Provides @Singleton @Authenticated
    public Retrofit provideAuthenticatedRetrofit(
            @Authenticated OkHttpClient client,
            Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Settings.ApiUrl_Local)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides @Singleton @Unauthenticated
    public Retrofit provideUnauthenticatedRetrofit(
            @Unauthenticated OkHttpClient client,
            Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Settings.ApiUrl_Local)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();
    }
}
