package com.programdoo.transport.data.models.dtos.masterData;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

import java.io.Serializable;

@Data
public class MasterDataDto implements Serializable
{
    @SerializedName("value")
    Integer Value;

    @SerializedName("description")
    String Description;

    @SerializedName("searchData")
    String SearchData;

}
