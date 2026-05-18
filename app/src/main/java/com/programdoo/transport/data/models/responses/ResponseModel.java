package com.programdoo.transport.data.models.responses;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ResponseModel<T> extends ResponseModelBase {
    @SerializedName("payload")
    private T payload;

    public ResponseModel(T data) {
        payload = data;
    }
}
