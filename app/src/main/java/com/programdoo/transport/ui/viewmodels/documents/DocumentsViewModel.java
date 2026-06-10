package com.programdoo.transport.ui.viewmodels.documents;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.documents.UploadDocumentRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.TravelOrderDto;
import com.programdoo.transport.data.repositories.DocumentsRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Getter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@HiltViewModel
public class DocumentsViewModel extends BaseViewModel {

    private final DocumentsRepository documentsRepository;

    private final MutableLiveData<Boolean> uploadLoading = new MutableLiveData<>();
    private final MutableLiveData<Integer> uploadResult = new MutableLiveData<>();
    private final MutableLiveData<String> uploadError = new MutableLiveData<>();


    @Inject
    public DocumentsViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvent,
            DocumentsRepository documentsRepository
    ) {
        super(preferences, session, authEvent);
        this.documentsRepository = documentsRepository;
    }

    public LiveData<Boolean> getUploadLoading() {
        return uploadLoading;
    }

    public LiveData<Integer> getUploadResult() {
        return uploadResult;
    }

    public LiveData<String> getUploadError() {
        return uploadError;
    }

    public void uploadDocument(
            Uri uri,
            TravelOrderDto item,
            int userId,
            Context context,
            int documentTypeId
    ) {

        uploadLoading.setValue(true);

        try {

            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            File file = new File(
                    context.getCacheDir(),
                    "upload_" + System.currentTimeMillis() + ".jpg"
            );

            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            RequestBody fileBody =
                    RequestBody.create(file, MediaType.parse("image/jpeg"));

            MultipartBody.Part filePart =
                    MultipartBody.Part.createFormData(
                            "file",
                            file.getName(),
                            fileBody
                    );

            UploadDocumentRequestModel model = new UploadDocumentRequestModel();
            model.setReferenceId(item.getId());
            model.setUserId(userId);
            model.setFileName(file.getName());
            model.setDocumentTypeId(documentTypeId);
            model.setSourceId(1);
            model.setRelativeFilePath("test");

            String json = new Gson().toJson(model);

            RequestBody dataBody =
                    RequestBody.create(json, MediaType.parse("text/plain"));

            disposables.add(
                    documentsRepository.uploadDocument(filePart, dataBody)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {

                                uploadLoading.setValue(false);
                                uploadResult.setValue(response.getPayload());

                            }, throwable -> {

                                uploadLoading.setValue(false);
                                uploadError.setValue(throwable.getMessage());

                            })
            );

        } catch (Exception e) {
            uploadLoading.setValue(false);
            uploadError.setValue(e.getMessage());
        }
    }

}