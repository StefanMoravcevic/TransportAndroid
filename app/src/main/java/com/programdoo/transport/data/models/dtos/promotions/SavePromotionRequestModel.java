package com.programdoo.transport.data.models.dtos.promotions;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SavePromotionRequestModel implements Serializable {
    @SerializedName("id")
    protected int id;
    @SerializedName("name")
    protected String name;
    @SerializedName("dateFrom")
    protected LocalDateTime dateFrom;
    @SerializedName("dateTo")
    protected LocalDateTime dateTo;
    @SerializedName("comment")
    protected String comment;

    public SavePromotionRequestModel() {

    }
}
