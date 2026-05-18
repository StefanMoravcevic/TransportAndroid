package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.requests.companies.SearchOrgUnitsParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.CompaniesService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class CompaniesRepository {
    private CompaniesService service;

    private final BehaviorSubject<SearchOrgUnitsParams> searchOrgUnitsRequests
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<OrgUnitDto>> orgUnits;

    @Inject
    public CompaniesRepository(
            CompaniesService service) {
        this.service = service;

        orgUnits = RepositoryOperators.createDataStream(
                searchOrgUnitsRequests, service::searchOrgUnits);
    }

    public void searchOrgUnits(SearchOrgUnitsParams searchParams) {
        searchOrgUnitsRequests.onNext(searchParams);
    }
}
