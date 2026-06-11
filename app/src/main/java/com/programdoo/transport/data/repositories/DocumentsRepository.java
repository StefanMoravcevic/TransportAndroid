package com.programdoo.transport.data.repositories;

import com.programdoo.transport.data.models.dtos.documents.DocumentDto;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.services.DocumentsService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import io.reactivex.rxjava3.core.Observable;

@ActivityRetainedScoped
public class DocumentsRepository {

    private final DocumentsService service;

    @Inject
    public DocumentsRepository(DocumentsService service) {
        this.service = service;
    }

    public Observable<ResponseModel<Integer>> uploadDocument(
            MultipartBody.Part file,
            RequestBody data
    ) {
        return service.uploadDocument(file, data);
    }
    public Observable<ResponseModel<String>> getNewNumber(
            int documentSerieTypeId,
            int orgUnitId
    ) {
        return service.getNewNumber(documentSerieTypeId, orgUnitId);
    }

    public Observable<List<DocumentDto>> getDocuments(int documentType, int referenceId) {

        return service.getDocuments(documentType, referenceId)
                .map(response -> {

                    if (response.isValid() && response.getPayload() != null) {
                        return response.getPayload();
                    }

                    return new ArrayList<DocumentDto>();
                });
    }

}