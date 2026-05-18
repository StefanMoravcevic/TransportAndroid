package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.memberships.MembershipCardDto;
import com.programdoo.transport.data.models.requests.memberships.SearchMembershipCardsParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MembershipCardsService {
    @POST("memberships/cards/search")
    Observable<ResponseModelList<MembershipCardDto>> searchMembershipCards(@Body SearchMembershipCardsParams searchParams);

}
