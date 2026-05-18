package com.programdoo.transport.data.models.dtos.memberships;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class MembershipCardDto extends  SaveMembershipCardRequestModel{
    @SerializedName("membershipName")
    public String membershipName;

    @SerializedName("orgUnitName")
    public String orgUnitName;
}
