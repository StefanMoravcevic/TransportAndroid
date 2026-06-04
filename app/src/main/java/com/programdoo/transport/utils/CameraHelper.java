package com.programdoo.transport.utils;

import android.content.Context;
import android.net.Uri;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.core.content.FileProvider;

import java.io.File;

public class CameraHelper {

    public interface CameraCallback {
        void onImageReady(Uri uri);
    }

    private ActivityResultLauncher<Uri> launcher;
    private Uri currentUri;

    public void init(ActivityResultCaller caller, CameraCallback callback) {

        launcher = caller.registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {

                    if (success && currentUri != null) {
                        callback.onImageReady(currentUri);
                    }
                }
        );
    }

    public void openCamera(Context context, String filePrefix) {

        currentUri = createImageUri(context, filePrefix);
        launcher.launch(currentUri);
    }

    private Uri createImageUri(Context context, String prefix) {

        File file = new File(
                context.getCacheDir(),
                prefix + "_" + System.currentTimeMillis() + ".jpg"
        );

        return FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".fileprovider",
                file
        );
    }

    public Uri getCurrentUri() {
        return currentUri;
    }
}