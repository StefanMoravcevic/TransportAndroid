package com.programdoo.transport.data.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ResponseModelList<T> extends ResponseModelBase {
    @SerializedName("payload")
    private List<T> payload;
    @SerializedName("totalRowCount")
    public int totalRowCount;

    public ResponseModelList(List<T> data) {
        payload = data;
    }
}