package com.programdoo.transport.di;

import com.programdoo.transport.data.services.AccountsService;
import com.programdoo.transport.data.services.AppointmentsService;
import com.programdoo.transport.data.services.AuthService;
import com.programdoo.transport.data.services.CompaniesService;
import com.programdoo.transport.data.services.DocumentsService;
import com.programdoo.transport.data.services.DriverVehicleIssuesService;
import com.programdoo.transport.data.services.EmployeeNotificationsService;
import com.programdoo.transport.data.services.EmployeesService;
import com.programdoo.transport.data.services.LocationTrackingService;
import com.programdoo.transport.data.services.MasterDataService;
import com.programdoo.transport.data.services.MembershipCardsService;
import com.programdoo.transport.data.services.MembershipsService;
import com.programdoo.transport.data.services.PoolCarReservationsService;
import com.programdoo.transport.data.services.PromotionsService;
import com.programdoo.transport.data.services.ReceiptsService;
import com.programdoo.transport.data.services.ScannedPackagesService;
import com.programdoo.transport.data.services.TraineesService;
import com.programdoo.transport.data.services.TravelOrdersService;
import com.programdoo.transport.data.services.VehicleEngagementsService;
import com.programdoo.transport.data.services.VehiclesService;

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

    @Provides
    PoolCarReservationsService providesPoolCarReservationsService(@Authenticated Retrofit retrofit) {
        return retrofit.create(PoolCarReservationsService.class);
    }

    @Provides
    VehiclesService providesVehicleService(@Authenticated Retrofit retrofit) {
        return retrofit.create(VehiclesService.class);
    }

    @Provides
    EmployeeNotificationsService providesEmployeeNotificationsService(@Authenticated Retrofit retrofit) {
        return retrofit.create(EmployeeNotificationsService.class);
    }

    @Provides
    VehicleEngagementsService providesVehicleEngagementsService(@Authenticated Retrofit retrofit) {
        return retrofit.create(VehicleEngagementsService.class);
    }

    @Provides
    TravelOrdersService providesTravelOrdersService(@Authenticated Retrofit retrofit) {
        return retrofit.create(TravelOrdersService.class);
    }

    @Provides
    DocumentsService providesDocumentsService(@Authenticated Retrofit retrofit) {
        return retrofit.create(DocumentsService.class);
    }
    @Provides
    DriverVehicleIssuesService providesDriverVehicleIssuesService(@Authenticated Retrofit retrofit) {
        return retrofit.create(DriverVehicleIssuesService.class);
    }
    @Provides
    LocationTrackingService providesLocationTrackingService(@Authenticated Retrofit retrofit) {
        return retrofit.create(LocationTrackingService.class);
    }

    @Provides
    ReceiptsService providesReceiptsService(@Authenticated Retrofit retrofit) {
        return retrofit.create(ReceiptsService.class);
    }

}
