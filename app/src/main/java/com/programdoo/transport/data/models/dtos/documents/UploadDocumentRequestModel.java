package com.programdoo.transport.data.models.dtos.documents;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class UploadDocumentRequestModel implements Serializable {
    @SerializedName("documentTypeId")
    private int documentTypeId;
    @SerializedName("ReferenceId")
    private int referenceId;
    @SerializedName("fileName")
    private String fileName;
    @SerializedName("userId")
    private int userId;
    @SerializedName("relativeFilePath")
    private String relativeFilePath;
    @SerializedName("sourceId")
    private int sourceId = 2;
}
