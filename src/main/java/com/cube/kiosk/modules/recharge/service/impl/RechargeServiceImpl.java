package com.cube.kiosk.modules.recharge.service.impl;

import com.cube.kiosk.https.HttpsUtils;
import com.cube.kiosk.modules.common.ResultData;
import com.cube.kiosk.modules.common.model.PatientInfo;
import com.cube.kiosk.modules.recharge.linstener.RechargeLinstener;
import com.cube.kiosk.modules.recharge.service.RechargeService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 李晋
 */
@Service
public class RechargeServiceImpl implements RechargeService {

    @Value("${neofaith.url}")
    private String url;

    @Override
    public String getQrCode(Double money) throws Exception {

        String result = HttpsUtils.doPost("http://127.0.0.1:8090/comlink-interface-abc-forward/comlink/pay", Maps.newHashMap());
        return result;
    }

    /**
     * 查询消费订单
     *
     * @param tradeNo ：支付订单号
     * @return
     * @throws Exception
     */
    @Override
    public void getConsumer0rder(String tradeNo, RechargeLinstener linstener){
        Map<String,Object> map = Maps.newHashMap();
        map.put("tradeNo", tradeNo);//就诊卡号
        try{
            String result = HttpsUtils.doPost("http://127.0.0.1:8090/comlink-interface-abc-forward/comlink/pay", map);

        }catch (Exception e){
            linstener.exception(e.getMessage());
        }


    }

    /**
     * 查询患者基本信息
     *
     * @param cardId
     * @return
     */
    @Override
    public String getPatientInfo(String cardId) throws Exception {

        Map<String,Object> map = Maps.newHashMap();
        map.put("cardId",cardId);
        String result = HttpsUtils.doPost(url+"getPatientnameInfo", map);
        return result;
    }

    /**
     * 查询就在卡余额
     *
     * @param patientName
     * @param identitycard
     * @param linstener
     * @return
     * @throws Exception
     */
    @Override
    public void getBalance(String patientName, String identitycard, RechargeLinstener linstener){
       try {
           Map<String,Object> map = Maps.newHashMap();
           map.put("identitycard", identitycard);//就诊卡号
           map.put("patientName", patientName);//患者姓名
           String result = HttpsUtils.doPost(url+"getBalance", map);
           Gson gson = new Gson();
           ResultData<PatientInfo> patientInfoResultData = gson.fromJson(result, new TypeToken<ResultData<PatientInfo>>(){}.getType());
           if(patientInfoResultData.getCode()==0){
               linstener.success(patientInfoResultData.getResponseData().getBalance());
           }else {
               linstener.error("查询余额失败");
           }
       }catch (Exception e){
           linstener.exception(e.getMessage());
       }
    }



}
