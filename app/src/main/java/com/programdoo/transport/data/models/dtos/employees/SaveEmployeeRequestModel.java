package com.programdoo.transport.data.models.dtos.employees;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SaveEmployeeRequestModel {
    @SerializedName("id")
    protected int id;
    @SerializedName("code")
    protected String code;
    @SerializedName("name")
    protected String name;
    @SerializedName("surname")
    protected String surname;
    @SerializedName("middleName")
    protected String middleName;
    @SerializedName("birthDate")
    protected LocalDateTime birthDate;
    @SerializedName("personalId")
    protected String personalId;
    @SerializedName("passportId")
    protected String passportId;
    @SerializedName("insuranceNumber")
    protected String insuranceNumber;
    @SerializedName("federalNumber")
    protected String federalNumber;
    @SerializedName("housePhoneNumber")
    protected String housePhoneNumber;
    @SerializedName("cellPhoneNumber")
    protected String cellPhoneNumber;
    @SerializedName("email")
    protected String email;
    @SerializedName("active")
    protected boolean active;
    @SerializedName("ownPartnerCompany")
    protected Boolean ownPartnerCompany;
    @SerializedName("address")
    protected String address;
    @SerializedName("zipCodeId")
    protected Integer zipCodeId;
    @SerializedName("zipCode")
    protected String zipCode;
    @SerializedName("cityId")
    protected Integer cityId;
    @SerializedName("stateId")
    protected Integer stateId;
    @SerializedName("jobTypeId")
    protected Integer jobTypeId;
    @SerializedName("companyId")
    protected Integer companyId;
    @SerializedName("orgUnitId")
    protected Integer orgUnitId;
    @SerializedName("partnerId")
    protected Integer partnerId;
    @SerializedName("genderId")
    protected Integer genderId;
    @SerializedName("birthPlace")
    protected String birthPlace;
    @SerializedName("citizenship")
    protected String citizenship;
    @SerializedName("bankAccount")
    protected String bankAccount;
    @SerializedName("bankAccountAddition")
    protected String bankAccountAddition;
    @SerializedName("noticeType")
    protected String noticeType;
    @SerializedName("noticeTypeId")
    protected Integer noticeTypeId;
    @SerializedName("shoeSize")
    protected String shoeSize;
    @SerializedName("suiteSize")
    protected String suiteSize;
    @SerializedName("routingNumber")
    protected String routingNumber;
    @SerializedName("accountingCode")
    protected String accountingCode;
    @SerializedName("employeeNumber")
    protected String employeeNumber;
    @SerializedName("profilePhotoId")
    protected Integer profilePhotoId;
    @SerializedName("userCulture")
    protected String userCulture = "sr-Latn";
    @SerializedName("employeeOrgUnitsIds")
    protected List<Integer> employeesOrgUnitsIds = new ArrayList<>();
}
