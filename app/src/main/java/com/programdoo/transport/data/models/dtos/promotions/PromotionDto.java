package com.programdoo.transport.data.models.dtos.promotions;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class PromotionDto extends SavePromotionRequestModel implements Serializable {
    @SerializedName("dateFromFmt")
    protected String dateFromFmt;
    @SerializedName("dateToFmt")
    protected String dateToFmt;

    public PromotionDto() {
        super();
    }
}
