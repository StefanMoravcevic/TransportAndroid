package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.promotions.PromotionDto;
import com.programdoo.transport.data.models.dtos.scannedpackages.SaveScannedPackagesRequestModel;
import com.programdoo.transport.data.models.dtos.scannedpackages.ScannedPackageDto;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.data.models.requests.scannedPackages.SearchScannedPackagesParams;
import com.programdoo.transport.data.services.EmployeesService;
import com.programdoo.transport.data.services.ScannedPackagesService;

import com.programdoo.transport.data.models.dtos.memberships.MembershipDto;
import com.programdoo.transport.data.models.dtos.memberships.SaveMembershipRequestModel;
import com.programdoo.transport.data.models.requests.memberships.SearchMembershipsParams;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.MembershipsService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;

import dagger.hilt.android.scopes.ActivityRetainedScoped;

@ActivityRetainedScoped
public class ScannedPackagesRepository {

    private final ScannedPackagesService service;
    private final BehaviorSubject<SearchScannedPackagesParams> searchScannedPackagesRequests
            = BehaviorSubject.create();
    @Getter
    private final Observable<ResponseModelList<ScannedPackageDto>> scannedPackages;
    @Inject
    public ScannedPackagesRepository(
        ScannedPackagesService scannedPackagesService) {
            this.service = scannedPackagesService;

        scannedPackages = RepositoryOperators.createDataStream(
                    searchScannedPackagesRequests, service::SearchScannedPackages);

        //    expiringDocuments = RepositoryOperators.createDataStream(
        //            expiringDocumentsRequests,
        //            service::getEmployeeExpiringDocuments
        //    );
    }

    public Completable saveScannedPackages(SaveScannedPackagesRequestModel saveData) {
        return service.SaveScannedPackages(saveData)
                .flatMapCompletable(result -> {
                    if (result.isValid()) return Completable.complete();
                    else return Completable.error(new RuntimeException(result.getMessage()));
                });
    }
    public void searchScannedPackages(SearchScannedPackagesParams searchParams) {
        searchScannedPackagesRequests.onNext(searchParams);
    }


}
