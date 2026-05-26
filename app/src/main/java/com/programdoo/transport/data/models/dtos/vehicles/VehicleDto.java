package com.programdoo.transport.data.models.dtos.vehicles;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;


@Data
public class VehicleDto extends  SaveVehicleRequestModel implements Serializable {
    @SerializedName("orgUnit")
    public String orgUnit;

    @SerializedName("owner")
    public String owner;

    @SerializedName("spareKeyText")
    public String spareKeyText;

    @SerializedName("company")
    public String company;

    @SerializedName("isChange")
    public String isChange;

    @SerializedName("vehicleTypeId")
    public Integer vehicleTypeId;

    @SerializedName("vehicleTypeIdParent")
    public Integer vehicleTypeIdParent;

    @SerializedName("vehicleType")
    public String vehicleType;

    @SerializedName("adBlueText")
    public String adBlueText;

    @SerializedName("dpfText")
    public String dpfText;

    @SerializedName("vehicleRegistration")
    public String vehicleRegistration;

    @SerializedName("formattedMilleageNumber")
    public String formattedMilleageNumber;

    @SerializedName("vehicleForHomePage")
    public String vehicleForHomePage;

    @SerializedName("limitedSpeedText")
    public String limitedSpeedText;

    @SerializedName("dispatcher")
    public String dispatcher;

    @SerializedName("driver")
    public String driver;

    @SerializedName("dispatcherId")
    public Integer dispatcherId;

    @SerializedName("driverId")
    public Integer driverId;

    @SerializedName("tags")
    public Integer tags;

    @SerializedName("tagsYesNo")
    public String tagsYesNo;

    @SerializedName("gpsYesNo")
    public String gpsYesNo;

    @SerializedName("manufacturerId")
    public Integer manufacturerId;

    @SerializedName("manufacturer")
    public String manufacturer;

    @SerializedName("model")
    public String model;

    @SerializedName("engineTypes")
    public String engineTypes;

    @SerializedName("photoForHtml")
    public String photoForHtml;

    @SerializedName("policy")
    public String policy;

    @SerializedName("policyPartner")
    public String policyPartner;

    @SerializedName("vehicleCardLukoil")
    public String vehicleCardLukoil;

    @SerializedName("vehicleCardLukoilPIN")
    public String vehicleCardLukoilPIN;

    @SerializedName("vehicleCardKnez")
    public String vehicleCardKnez;

    @SerializedName("vehicleCardKnezPIN")
    public String vehicleCardKnezPIN;

    @SerializedName("vehicleCardPetrol")
    public String vehicleCardPetrol;

    @SerializedName("vehicleCardPetrolPIN")
    public String vehicleCardPetrolPIN;

    @SerializedName("policyExpirationDate")
    public LocalDateTime policyExpirationDate;

    @SerializedName("sector")
    public String sector;

    @SerializedName("previousLicensePlate")
    public String previousLicensePlate;

    @SerializedName("previousRegistrationDateTo")
    public LocalDateTime previousRegistrationDateTo;

    @SerializedName("ppa")
    public String ppa;

    @SerializedName("ppaDate")
    public LocalDateTime ppaDate;

    @SerializedName("pp")
    public String pp;

    @SerializedName("ppDate")
    public LocalDateTime ppDate;

    @SerializedName("tachograph")
    public String tachograph;

    @SerializedName("tachographDate")
    public LocalDateTime tachographDate;

    @SerializedName("smallMaintenancePlanId")
    public Integer smallMaintenancePlanId;

    @SerializedName("bigMaintenancePlanId")
    public Integer bigMaintenancePlanId;

    @SerializedName("sparkMaintenancePlanId")
    public Integer sparkMaintenancePlanId;

    @SerializedName("milleage")
    public double milleage;

    @SerializedName("vehiclePasses")
    public Integer vehiclePasses;

    @SerializedName("ownershipType")
    public String ownershipType;

    @SerializedName("isPoolCarFilter")
    public String isPoolCarFilter;

    @SerializedName("fuelTypeDescription")
    public String fuelTypeDescription;

    @SerializedName("iftaExpirationDateFormatted")
    public String iftaExpirationDateFormatted;

    @SerializedName("platesExpirationDateFormatted")
    public String platesExpirationDateFormatted;

    @SerializedName("policyExpirationDateFormatted")
    public String policyExpirationDateFormatted;

    @SerializedName("firstRegistrationYearFormatted")
    public String firstRegistrationYearFormatted;

    @SerializedName("previousRegistrationDateToFormatted")
    public String previousRegistrationDateToFormatted;

    @SerializedName("acquisitionDateFormatted")
    public String acquisitionDateFormatted;

    @SerializedName("dateToFormatted")
    public String dateToFormatted;

    @SerializedName("mileageAtTimeOfPurchaseFormatted")
    public String mileageAtTimeOfPurchaseFormatted;

    @SerializedName("currentMileageFormatted")
    public String currentMileageFormatted;

    @SerializedName("status")
    public String status;

    @SerializedName("connectedVehicle")
    public String connectedVehicle;
}
