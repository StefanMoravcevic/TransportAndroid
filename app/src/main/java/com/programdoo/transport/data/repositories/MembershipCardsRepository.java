package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.models.dtos.memberships.MembershipCardDto;
import com.programdoo.transport.data.models.requests.memberships.SearchMembershipCardsParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.MembershipCardsService;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class MembershipCardsRepository {

    private final MembershipCardsService service;

    private final BehaviorSubject<SearchMembershipCardsParams> searchCardsRequests
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<MembershipCardDto>> cards;

    @Inject
    public MembershipCardsRepository(
            MembershipCardsService membershipCardsService) {
        this.service = membershipCardsService;

        cards = RepositoryOperators.createDataStream(
                searchCardsRequests, service::searchMembershipCards);
    }

    public void searchMembershipCards(SearchMembershipCardsParams searchParams) {
        searchCardsRequests.onNext(searchParams);
    }
}
