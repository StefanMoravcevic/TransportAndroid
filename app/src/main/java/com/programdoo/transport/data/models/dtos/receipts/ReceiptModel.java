package com.programdoo.transport.data.models.dtos.receipts;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReceiptModel implements Serializable {

    @SerializedName("tin")
    private String tin;
    @SerializedName("shop")
    private String shop;
    @SerializedName("address")
    private String address;
    @SerializedName("city")
    private String city;
    @SerializedName("municipality")
    private String municipality;
    @SerializedName("customerId")
    private String customerId;
    @SerializedName("totalAmount")
    private String totalAmount;
    @SerializedName("invoiceNumber")
    private String invoiceNumber;
    @SerializedName("date")
    private String date;
    @SerializedName("token")
    private String token;
    @SerializedName("employeeId")
    private Integer employeeId;
}