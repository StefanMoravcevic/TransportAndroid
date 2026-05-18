package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.requests.companies.SearchOrgUnitsParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CompaniesService {
    @POST("companies/orgUnits/search")
    Observable<ResponseModelList<OrgUnitDto>> searchOrgUnits(@Body SearchOrgUnitsParams searchParams);
}
