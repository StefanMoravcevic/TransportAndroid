package com.programdoo.transport.data.models.dtos.memberships;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SaveMembershipRequestModel  implements  Serializable{
    @SerializedName("id")
    public int id;
    @SerializedName("createdAt")
    public LocalDateTime  createdAt;
    @SerializedName("cardId")
    public int cardId;
    @SerializedName("traineeId")
    public int traineeId;
    @SerializedName("purchaseDate")
    public LocalDateTime purchaseDate;
    @SerializedName("validFromDate")
    public LocalDateTime validFromDate;
    @SerializedName("validToDate")
    public LocalDateTime validToDate;
    @SerializedName("paymentMethodId")
    public Integer paymentMethodId;
    @SerializedName("paymentDate")
    public LocalDateTime paymentDate;
    @SerializedName("price")
    public Double price;
    @SerializedName("currencyId")
    public Integer currencyId;
    @SerializedName("totalSessions")
    public int totalSessions;
    @SerializedName("comment")
    public String comment;

    public SaveMembershipRequestModel() {
        this.createdAt = LocalDateTime.now().withNano(0);
    }
}