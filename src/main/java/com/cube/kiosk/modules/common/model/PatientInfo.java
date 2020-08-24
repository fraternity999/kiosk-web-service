package com.cube.kiosk.modules.common.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author LMZ
 */
@Data
public class PatientInfo {

    @SerializedName("patientname")
    private String name;//病人姓名

    private String sex;//性别

    private String age;//年龄

    private String ageUnit;//年龄单位

    private String birthday;//出生日期

    private String identitycard;

    private String registeredAddress;//户口地址

    private String sickType;//费用类别

    private String employment;//职业

    private String telephone;//手机号

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

    private String patientname;//病人姓名

    private Double balance;//卡余额

    private Double amount;//总金额

    private String hisSerialNumber;//His流水号

    private String  cardID;//就诊卡号
    private String  officeName;//住院科室
    private String  enterhosDate;//入院日期
    private String  inhosTimes;//住院次数
    private String  officebedId;//科床号
    private String  doctorName;//主治医师
}
