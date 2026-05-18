package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.memberships.MembershipDto;
import com.programdoo.transport.data.models.dtos.memberships.SaveMembershipRequestModel;
import com.programdoo.transport.data.models.requests.memberships.SearchMembershipsParams;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface MembershipsService {
    @POST("memberships/search")
    Observable<ResponseModelList<MembershipDto>> searchMemberships(@Body SearchMembershipsParams searchParams);

    @POST("memberships")
    Single<ResponseModel<Integer>> saveMembership(@Body SaveMembershipRequestModel saveData);
    @GET("memberships/{id}")
    Observable<ResponseModel<MembershipDto>> getMembership(@Path("id") int id);

    @DELETE("memberships/delete/{id}/{userId}")
    Single<ResponseModelBase> deleteMembership(@Path("id") int id, @Path("userId") int userId);
}
