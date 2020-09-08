package com.cube.kiosk.modules.patient.service.impl;

import com.cube.kiosk.https.HttpsUtils;
import com.cube.kiosk.modules.common.ResponseData;
import com.cube.kiosk.modules.common.ResponseDatabase;
import com.cube.kiosk.modules.common.ResultData;
import com.cube.kiosk.modules.common.model.ResultLinstener;
import com.cube.kiosk.modules.patient.model.PatientInfo;
import com.cube.kiosk.modules.patient.service.PatientService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author LMZ
 */
@Service
public class PatientServiceImpl implements PatientService {

    @Value("${neofaith.url}")
    private String url;


    /**
     * 获取病人信息
     *
     * @param id        卡号
     * @param linstener
     * @return
     */
    @Override
    public void get(String id, ResultLinstener linstener) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("cardId",id);
        ResponseData<PatientInfo> responseData = ResponseDatabase.newResponseData();
        try {
            String result = HttpsUtils.doPost(url+"his/getPatientnameInfo", map);
            //反序列化Patient
            Gson gson = new Gson();
            ResultData<Object> resultData = gson.fromJson(result, new TypeToken<ResultData<Object>>(){}.getType());
            if(resultData.getCode()==1){
                responseData.setCode(500);
                responseData.setData(null);
                responseData.setMessage(resultData.getResponseData().toString());
                linstener.error(responseData);
                return;
            }
            PatientInfo patientInfo = (PatientInfo) resultData.getResponseData();
            map.put("identitycard", id);
            map.put("patientName", patientInfo.getName());
            result = HttpsUtils.doPost(url+"his/getBalance", map);
            resultData = gson.fromJson(result, new TypeToken<ResultData<Object>>(){}.getType());
            if(resultData.getCode()==1){
                responseData.setCode(500);
                responseData.setData(null);
                responseData.setMessage(resultData.getResponseData().toString());
                linstener.error(responseData);
                return;
            }
            PatientInfo blance = (PatientInfo) resultData.getResponseData();
            patientInfo.setBalance(blance.getBalance());
            patientInfo.setAmount(blance.getAmount());
            linstener.success(patientInfo);
        } catch (Exception e) {
            linstener.exception(e.getMessage());
        }
    }
}
