package com.programdoo.transport.data.repositories;

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

@ActivityRetainedScoped
public class MembershipsRepository {

    private final MembershipsService service;

    private final BehaviorSubject<SearchMembershipsParams> searchMembershipsRequests
            = BehaviorSubject.create();
    private final PublishSubject<Object> refreshMembershipsTrigger = PublishSubject.create();
    private final BehaviorSubject<Integer> getMembershipRequests
            = BehaviorSubject.create();

    @Getter
    private final Observable<ResponseModelList<MembershipDto>> memberships;
    @Getter
    private final Observable<ResponseModel<MembershipDto>> membership;
    @Getter
    private final Observable<Object> refreshMembershipsCompleted;

    @Inject
    public MembershipsRepository(
            MembershipsService membershipsService) {
        this.service = membershipsService;

        memberships = RepositoryOperators.createRefreshableDataStream(
                searchMembershipsRequests, refreshMembershipsTrigger, service::searchMemberships);
        membership = RepositoryOperators.createDataStream(
                getMembershipRequests, service::getMembership);
        refreshMembershipsCompleted = RepositoryOperators.createRefreshCompletedStream(
                refreshMembershipsTrigger, memberships);
    }

    public void searchMemberships(SearchMembershipsParams searchParams) {
        searchMembershipsRequests.onNext(searchParams);
    }
    public void getMembership(int membershipId) {
        getMembershipRequests.onNext(membershipId);
    }

    public Completable saveMembership(SaveMembershipRequestModel saveData) {
        return service.saveMembership(saveData)
                .flatMapCompletable(result -> {
                    if (result.isValid()) return Completable.complete();
                    else return Completable.error(new RuntimeException(result.getMessage()));
                })
                .doOnComplete(this::refreshMemberships);
    }
    public Completable deleteMembership(int id, int userId) {
        return service.deleteMembership(id, userId)
                .flatMapCompletable(result -> {
                    if (result.isValid()) return Completable.complete();
                    else return Completable.error(new RuntimeException(result.getMessage()));
                })
                .doOnComplete(this::refreshMemberships);
    }

    public void refreshMemberships() {
        this.refreshMembershipsTrigger.onNext(new Object());
    }
}
