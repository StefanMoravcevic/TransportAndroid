package com.programdoo.transport.data.models.dtos.vehicleEngagements;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SaveVehicleEngagementRequestModel implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("isDeleted")
    private boolean isDeleted;

    @SerializedName("deletedBy")
    private Integer deletedBy;

    @SerializedName("deletedDate")
    private String deletedDate;

    @SerializedName("indebtednessDate")
    private LocalDateTime indebtednessDate;

    @SerializedName("indebtednessDateForFormat")
    private String indebtednessDateForFormat;

    @SerializedName("employeeId")
    private Integer employeeId;

    @SerializedName("vehicleId")
    private Integer vehicleId;

    @SerializedName("milleage")
    private Integer milleage;

    @SerializedName("engagementType")
    private boolean engagementType;

    @SerializedName("divorceDate")
    private LocalDateTime divorceDate;

    @SerializedName("divorceDateForFormat")
    private String divorceDateForFormat;

    @SerializedName("pouredFuel")
    private boolean pouredFuel;

    @SerializedName("divorceMilleage")
    private Integer divorceMilleage;

    @SerializedName("indebtednessMilleage")
    private Integer indebtednessMilleage;

    @SerializedName("orgUnitId")
    private Integer orgUnitId;

    @SerializedName("divorceNote")
    private String divorceNote;

    @SerializedName("indebtednessNote")
    private String indebtednessNote;

    @SerializedName("fuelQuantity")
    private Double fuelQuantity;

    @SerializedName("engagementId")
    private Integer engagementId;

    @SerializedName("unassignedBy")
    private Integer unassignedBy;

    @SerializedName("unassignedByEmployee")
    private Integer unassignedByEmployee;
}
