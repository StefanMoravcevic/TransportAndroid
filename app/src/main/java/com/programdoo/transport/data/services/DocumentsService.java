package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.documents.DocumentDto;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DocumentsService {
    @Multipart
    @POST("documents/upload")
    Observable<ResponseModel<Integer>> uploadDocument(
            @Part MultipartBody.Part file,
            @Part("data") RequestBody data
    );

    @GET("documents/list/{documentType}/{referenceId}")
    Observable<ResponseModelList<DocumentDto>> getDocuments(
            @Path("documentType") int documentType,
            @Path("referenceId") int referenceId
    );
    @GET("documentSeries/getNewNumber/{documentSerieTypeId}/{orgUnitId}")
    Observable<ResponseModel<String>> getNewNumber(
            @Path("documentSerieTypeId") int documentSerieTypeId,
            @Path("orgUnitId") int orgUnitId
    );
}
