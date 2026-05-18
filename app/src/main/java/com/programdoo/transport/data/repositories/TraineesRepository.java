package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.models.dtos.activities.ActivityDto;
import com.programdoo.transport.data.models.dtos.activities.SaveActivityRequestModel;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.requests.activities.SearchActivitiesParams;
import com.programdoo.transport.data.rxoperators.RepositoryOperators;
import com.programdoo.transport.data.services.TraineesService;
import com.programdoo.transport.data.models.dtos.trainees.SaveTraineeRequestModel;
import com.programdoo.transport.data.models.requests.trainees.SearchTraineesParams;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;

@ActivityRetainedScoped
public class TraineesRepository {
    private final TraineesService service;

    private final BehaviorSubject<SearchTraineesParams> searchTraineesRequests
            = BehaviorSubject.create();
    private final PublishSubject<Object> refreshTraineesTrigger = PublishSubject.create();
    private final BehaviorSubject<Integer> getTraineeRequests
            = BehaviorSubject.create();
    private final BehaviorSubject<SearchActivitiesParams> searchActivitiesRequests
            = BehaviorSubject.create();
    private final PublishSubject<Object> refreshActivitiesTrigger = PublishSubject.create();
    private final BehaviorSubject<Integer> getActivityRequests
            = BehaviorSubject.create();


    @Getter
    private final Observable<ResponseModelList<TraineeDto>> trainees;
    @Getter
    private final Observable<ResponseModel<TraineeDto>> trainee;
    @Getter
    private final Observable<ResponseModelList<ActivityDto>> activities;
    @Getter
    private final Observable<ResponseModel<ActivityDto>> activity;
    @Getter
    private final Observable<Object> refreshTraineesCompleted;
    @Getter
    private final Observable<Object> refreshActivitiesCompleted;


    @Inject
    public TraineesRepository(
            TraineesService traineesService) {
        this.service = traineesService;

        this.trainees = RepositoryOperators.createRefreshableDataStream(
                searchTraineesRequests, refreshTraineesTrigger, service::searchTrainees);
        this.trainee = RepositoryOperators.createDataStream(
                getTraineeRequests, service::getTrainee);
        this.activities = RepositoryOperators.createRefreshableDataStream(
                searchActivitiesRequests, refreshActivitiesTrigger, service::searchActivities);
        this.activity = RepositoryOperators.createDataStream(
                getActivityRequests, service::getActivity);

        this.refreshTraineesCompleted = RepositoryOperators.createRefreshCompletedStream(
                refreshTraineesTrigger, trainees);
        this.refreshActivitiesCompleted = RepositoryOperators.createRefreshCompletedStream(
                refreshActivitiesTrigger, activities);
    }

    // commands
    public Single<Integer> saveTrainee(SaveTraineeRequestModel saveData) {
        return service.saveTrainee(saveData)
                .subscribeOn(Schedulers.io())
                .flatMap(response -> {
                    if (response.isValid())
                        return Single.just(response.getPayload());
                    else
                        return Single.error(new RuntimeException(response.getMessage()));
                })
                .doOnSuccess(id -> refreshTrainees());
    }
    public Single<Integer> saveTrialTrainee(SaveTraineeRequestModel saveData) {
        return service.saveTrialTrainee(saveData)
                .subscribeOn(Schedulers.io())
                .flatMap(response -> {
                    if (response.isValid())
                        return Single.just(response.getPayload());
                    else
                        return Single.error(new RuntimeException(response.getMessage()));
                })
                .doOnSuccess(id -> refreshTrainees());
    }
    public Completable deleteTrainee(int id, int userId) {
        return service.deleteTrainee(id, userId)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(response -> {
                    if (response.isValid())
                        return Completable.complete();
                    else
                        return Completable.error(new RuntimeException(response.getMessage()));
                })
                .doOnComplete(this::refreshTrainees);
    }
    public Completable saveActivity(SaveActivityRequestModel saveData) {
        return service.saveActivity(saveData)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(response -> {
                    if (response.isValid())
                        return Completable.complete();
                    else
                        return Completable.error(new RuntimeException(response.getMessage()));
                })
                .doOnComplete(this::refreshActivities);
    }
    public Completable deleteActivity(int id, int userId) {
        return service.deleteActivity(id, userId)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(response -> {
                    if (response.isValid())
                        return Completable.complete();
                    else
                        return Completable.error(new RuntimeException(response.getMessage()));
                })
                .doOnComplete(this::refreshActivities);
    }

    // intents
    public void searchTrainees(SearchTraineesParams params) {
        searchTraineesRequests.onNext(params);
    }
    public void getTrainee(int traineeId) {
        getTraineeRequests.onNext(traineeId);
    }
    public void searchActivities(SearchActivitiesParams params) {
        searchActivitiesRequests.onNext(params);
    }
    public void getActivity(int activityId) {
        getActivityRequests.onNext(activityId);
    }

    // refreshes
    public void refreshTrainees() {
        refreshTraineesTrigger.onNext(new Object());
    }

    public void refreshActivities() {
        refreshActivitiesTrigger.onNext(new Object());
    }
}
