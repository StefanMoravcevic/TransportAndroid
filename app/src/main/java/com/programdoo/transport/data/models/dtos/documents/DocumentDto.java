package com.programdoo.transport.data.models.dtos.documents;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DocumentDto implements Serializable {
    public DocumentDto() {
        this.relativeFilePath = "";
        this.downloadFileName = "";
        this.documentType = "";
        this.uploadedBy = "";
        this.sourceId = 1;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("relativeFilePath")
    private String relativeFilePath;

    @SerializedName("documentTypeId")
    private int documentTypeId;

    @SerializedName("referenceId")
    private int referenceId;

    @SerializedName("uploadedDate")
    private LocalDateTime uploadedDate;

    @SerializedName("downloadFileName")
    private String downloadFileName;

    @SerializedName("uploadedByUserId")
    private int uploadedByUserId;

    @SerializedName("sourceId")
    private int sourceId;

    @SerializedName("documentType")
    private String documentType;

    @SerializedName("uploadedBy")
    private String uploadedBy;
}
