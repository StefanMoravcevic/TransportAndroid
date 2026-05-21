package com.programdoo.transport.di;

import com.programdoo.transport.data.services.AccountsService;
import com.programdoo.transport.data.services.AppointmentsService;
import com.programdoo.transport.data.services.AuthService;
import com.programdoo.transport.data.services.CompaniesService;
import com.programdoo.transport.data.services.EmployeesService;
import com.programdoo.transport.data.services.MasterDataService;
import com.programdoo.transport.data.services.MembershipCardsService;
import com.programdoo.transport.data.services.MembershipsService;
import com.programdoo.transport.data.services.PromotionsService;
import com.programdoo.transport.data.services.ScannedPackagesService;
import com.programdoo.transport.data.services.TraineesService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

/**
 * u ovoj klasi se registruju servisi. kad se doda nov servis, potrebno je
 * upisati ga ovde kako bi hilt di umeo da ga pronadje.
 * dovoljno je pratiti obrazac iz ostalih funkcija.
 */
@Module
@InstallIn(SingletonComponent.class)
public class ServiceModule {
    @Provides
    @Singleton
    public AuthService providesAuthService(@Unauthenticated Retrofit retrofit) {
        return retrofit.create(AuthService.class);
    }
    @Provides
    public TraineesService providesTraineesService(@Authenticated Retrofit retrofit) {
        return retrofit.create(TraineesService.class);
    }
    @Provides
    public AccountsService providesAccountsService(@Authenticated Retrofit retrofit) {
        return retrofit.create(AccountsService.class);
    }
    @Provides
    public PromotionsService providesPromotionsService(@Authenticated Retrofit retrofit) {
        return retrofit.create(PromotionsService.class);
    }
    @Provides
    public EmployeesService providesEmployeesService(@Authenticated Retrofit retrofit) {
        return retrofit.create(EmployeesService.class);
    }
    @Provides
    public CompaniesService providesCompaniesService(@Authenticated Retrofit retrofit) {
        return retrofit.create(CompaniesService.class);
    }
    @Provides
    AppointmentsService providesAppointmentsService(@Authenticated Retrofit retrofit) {
        return retrofit.create(AppointmentsService.class);
    }
    @Provides
    MembershipsService providesMembershipsService(@Authenticated Retrofit retrofit) {
        return retrofit.create(MembershipsService.class);
    }
    @Provides
    MembershipCardsService providesMembershipCardsService(@Authenticated Retrofit retrofit) {
        return retrofit.create(MembershipCardsService.class);
    }
    @Provides
    MasterDataService providesMasterDataService(@Authenticated Retrofit retrofit) {
        return retrofit.create(MasterDataService.class);
    }

    @Provides
    ScannedPackagesService providesScannedPackagesService(@Authenticated Retrofit retrofit) {
        return retrofit.create(ScannedPackagesService.class);
    }
}
