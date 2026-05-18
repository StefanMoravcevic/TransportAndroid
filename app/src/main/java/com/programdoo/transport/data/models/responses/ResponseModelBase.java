package com.programdoo.transport.data.models.responses;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ResponseModelBase {
    @SerializedName("valid")
    private boolean valid;
    @SerializedName("message")
    private String message;

    public ResponseModelBase() { valid = false; }
}
