package com.cube.kiosk.modules.recharge;

import com.cube.kiosk.modules.common.ResponseData;
import com.cube.kiosk.modules.common.ResponseDatabase;
import com.cube.kiosk.modules.common.model.PatientInfo;
import com.cube.kiosk.modules.hardware.AllowCardIn;
import com.cube.kiosk.modules.hardware.CheckCard;
import com.cube.kiosk.modules.hardware.MoveCard;
import com.cube.kiosk.modules.hardware.ReadCard;
import com.cube.kiosk.modules.recharge.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @GetMapping("index")
    @AllowCardIn
    @CheckCard
    @MoveCard
    @ReadCard
    public ResponseData<PatientInfo> index(String cardId){
        ResponseData<PatientInfo> responseData = ResponseDatabase.newResponseData();
        responseData.setCode(0);
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setCardId(cardId);
        patientInfo.setName("哈哈");
        responseData.setData(patientInfo);
        responseData.setMessage("成功");
        return responseData;
    }

    @GetMapping("cardNo")
    @CheckCard
    @MoveCard
    @ReadCard
    public String getCardInfo(String cardNo){
        return cardNo;
    }

    @GetMapping("qrCode")
    @CheckCard
    public String getQrCode(Double money){
        return rechargeService.getQrCode(money);
    }

    @GetMapping("patientInfo")
    public String getPatientInfo(String cardId){
        return rechargeService.getPatientInfo(cardId);
    }

}
