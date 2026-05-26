package com.programdoo.transport.data.models.dtos.vehicles;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SaveVehicleRequestModel implements Serializable {
    @SerializedName("id")
    public int id;

    @SerializedName("internalNumber")
    public String internalNumber;

    @SerializedName("vehicleModelId")
    public int vehicleModelId;

    @SerializedName("ownerId")
    public Integer ownerId;

    @SerializedName("companyId")
    public Integer companyId;

    @SerializedName("orgUnitId")
    public Integer orgUnitId;

    @SerializedName("ownershipTypeId")
    public int ownershipTypeId;

    @SerializedName("chassisNumber")
    public String chassisNumber;

    @SerializedName("vehicleYear")
    public Integer vehicleYear;

    @SerializedName("firstRegistrationYear")
    public LocalDateTime firstRegistrationYear;

    @SerializedName("acquisitionDate")
    public LocalDateTime acquisitionDate;

    @SerializedName("saleDate")
    public LocalDateTime saleDate;

    @SerializedName("color")
    public String color;

    @SerializedName("licensePlate")
    public String licensePlate;

    @SerializedName("platesExpirationDate")
    public LocalDateTime platesExpirationDate;

    @SerializedName("leasingEndDate")
    public LocalDateTime leasingEndDate;

    @SerializedName("registrationStateId")
    public Integer registrationStateId;

    @SerializedName("vehicleNote")
    public String vehicleNote;

    @SerializedName("loadCapacity")
    public BigDecimal loadCapacity;

    @SerializedName("registeredGrossWeight")
    public Integer registeredGrossWeight;

    @SerializedName("boxSize")
    public String boxSize;

    @SerializedName("internalWidth")
    public Integer internalWidth;

    @SerializedName("internalDoorHeight")
    public Integer internalDoorHeight;

    @SerializedName("height")
    public Integer height;

    @SerializedName("width")
    public Integer width;

    @SerializedName("hasLoadingRamp")
    public boolean hasLoadingRamp;

    @SerializedName("hasSleepingCabin")
    public boolean hasSleepingCabin;

    @SerializedName("active")
    public boolean active;

    @SerializedName("engineTypeId")
    public Integer engineTypeId;

    @SerializedName("engineDescription")
    public String engineDescription;

    @SerializedName("oilType")
    public String oilType;

    @SerializedName("manualTransmission")
    public boolean manualTransmission;

    @SerializedName("idlingHours")
    public Integer idlingHours;

    @SerializedName("hasAuxiliaryPowerUnit")
    public boolean hasAuxiliaryPowerUnit;

    @SerializedName("deletePhoto")
    public boolean deletePhoto;

    @SerializedName("fuelTypeId")
    public Integer fuelTypeId;

    @SerializedName("tyreSizeSteeringAxel")
    public String tyreSizeSteeringAxel;

    @SerializedName("tyreSizeDriveAxel")
    public String tyreSizeDriveAxel;

    @SerializedName("tyreSizeTrailerAxel")
    public String tyreSizeTrailerAxel;

    @SerializedName("mileageAtTimeOfPurchase")
    public Integer mileageAtTimeOfPurchase;

    @SerializedName("mileageAtTimeOfPurchaseText")
    public String mileageAtTimeOfPurchaseText;

    @SerializedName("soldAtPrice")
    public double soldAtPrice;

    @SerializedName("hasGps")
    public boolean hasGps;

    @SerializedName("warrantyMilleage")
    public Integer warrantyMilleage;

    @SerializedName("warrantyDate")
    public LocalDateTime warrantyDate;

    @SerializedName("enginePower")
    public Integer enginePower;

    @SerializedName("engineVolume")
    public double engineVolume;

    @SerializedName("photo")
    public String photo;

    @SerializedName("isPoolCar")
    public boolean isPoolCar;

    @SerializedName("fmsunitId")
    public Integer fmsunitId;

    @SerializedName("smallServicePlanTypeId")
    public Integer smallServicePlanTypeId;

    @SerializedName("bigServicePlanTypeId")
    public Integer bigServicePlanTypeId;

    @SerializedName("sectorId")
    public Integer sectorId;

    @SerializedName("description")
    public String description;

    @SerializedName("numberOfSeats")
    public Integer numberOfSeats;

    @SerializedName("newRegistrationCertificate")
    public Boolean newRegistrationCertificate;

    @SerializedName("engineNumber")
    public String engineNumber;

    @SerializedName("pcCode")
    public String pcCode;

    @SerializedName("isSold")
    public boolean isSold;

    @SerializedName("excludeFromNotifications")
    public boolean excludeFromNotifications;

    @SerializedName("ADBlue")
    public boolean ADBlue;

    @SerializedName("isActiveADBlue")
    public boolean isActiveADBlue;

    @SerializedName("DPF")
    public boolean DPF;

    @SerializedName("isActiveDPF")
    public boolean isActiveDPF;

    @SerializedName("spareKey")
    public boolean spareKey;

    @SerializedName("purchaseDate")
    public LocalDateTime purchaseDate;

    @SerializedName("limitedSpeedDate")
    public LocalDateTime limitedSpeedDate;

    @SerializedName("hasLimitedSpeed")
    public boolean hasLimitedSpeed;

    @SerializedName("limitedSpeed")
    public Integer limitedSpeed;
}
