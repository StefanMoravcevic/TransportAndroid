package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.activities.ActivityDto;
import com.programdoo.transport.data.models.dtos.activities.SaveActivityRequestModel;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.dtos.trainees.SaveTraineeRequestModel;
import com.programdoo.transport.data.models.requests.activities.SearchActivitiesParams;
import com.programdoo.transport.data.models.requests.trainees.SearchTraineesParams;
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

public interface TraineesService {
    @POST("trainees/search")
    Observable<ResponseModelList<TraineeDto>> searchTrainees(@Body SearchTraineesParams searchParams);
    @GET("trainees/{id}")
    Observable<ResponseModel<TraineeDto>> getTrainee(@Path("id") int id);
    @POST("trainees/trial")
    Single<ResponseModel<Integer>> saveTrialTrainee(@Body SaveTraineeRequestModel saveData);
    @POST("trainees")
    Single<ResponseModel<Integer>> saveTrainee(@Body SaveTraineeRequestModel saveData);
    @DELETE("trainees/delete/{id}/{userId}")
    Single<ResponseModelBase> deleteTrainee(@Path("id") int id, @Path("userId") int userId);

    @POST("trainees/activities/search")
    Observable<ResponseModelList<ActivityDto>> searchActivities(@Body SearchActivitiesParams searchParams);
    @GET("trainees/activities/{id}")
    Observable<ResponseModel<ActivityDto>> getActivity(@Path("id") int id);
    @POST("trainees/activities")
    Single<ResponseModel<Integer>> saveActivity(@Body SaveActivityRequestModel saveData);
    @DELETE("trainees/activities/delete/{id}/{userId}")
    Single<ResponseModelBase> deleteActivity(@Path("id") int id, @Path("userId") int userId);


}
