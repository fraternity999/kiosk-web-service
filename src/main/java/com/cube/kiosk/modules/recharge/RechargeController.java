package com.cube.kiosk.modules.recharge;

import com.cube.kiosk.modules.common.ResponseData;
import com.cube.kiosk.modules.common.ResponseDatabase;
import com.cube.kiosk.modules.common.ResultData;
import com.cube.kiosk.modules.common.model.PatientInfo;
import com.cube.kiosk.modules.hardware.AllowCardIn;
import com.cube.kiosk.modules.hardware.CheckCard;
import com.cube.kiosk.modules.hardware.MoveCard;
import com.cube.kiosk.modules.hardware.ReadCard;
import com.cube.kiosk.modules.recharge.service.RechargeService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

//    @AllowCardIn
//    @CheckCard
//    @MoveCard
    @ReadCard
    @RequestMapping("index")
    public ResponseData<PatientInfo> index(String cardId, HttpServletRequest request){
        String result = null;
        ResponseData<PatientInfo> responseData = ResponseDatabase.newResponseData();
        try {
            result = rechargeService.getPatientInfo(cardId);
            Gson gson = new Gson();
            ResultData<PatientInfo> patientInfoResultData = gson.fromJson(result, new TypeToken<ResultData<PatientInfo>>(){}.getType());
            if(patientInfoResultData.getCode()==0){
                responseData.setCode(200);
                responseData.setData(patientInfoResultData.getResponseData());
                responseData.setMessage("成功");
            }else {
                responseData.setCode(500);
                responseData.setData(null);
                responseData.setMessage("查询患者信息失败");
            }
        } catch (Exception e) {
            responseData.setCode(500);
            responseData.setData(null);
            responseData.setMessage(e.getMessage());
        } finally {
            return responseData;
        }


    }

    @GetMapping("cardNo")
    @CheckCard
    @MoveCard
    @ReadCard
    public String getCardInfo(String cardNo){
        return cardNo;
    }

    @GetMapping("qrCode")
    public String getQrCode(Double money){
        String result = "";
        try {
            result = rechargeService.getQrCode(money);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }



}
