package com.cube.kiosk.modules.common.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author LMZ
 */
@Data
public class PatientInfo {

    @SerializedName("patientname")
    private String name;

    private String sex;

    private String age;

    private String ageUnit;

    private String birthday;

    private String identitycard;

    private String registeredAddress;

    private String sickType;

    private String employment;

    private String telephone;

    private String contactAddress;

    private String postalCode;

    private String workUnit;

    private String unitTelephone;

    private String unitAddress;

    private String unitPostCode;

    private String nation;

    private String nationality;

    private String cardState;

    private String diagnoseId;

    private String cardType;
}
