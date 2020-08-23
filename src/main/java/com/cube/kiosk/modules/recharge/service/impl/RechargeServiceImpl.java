package com.cube.kiosk.modules.recharge.service.impl;

import com.cube.kiosk.https.HttpsUtils;
import com.cube.kiosk.modules.recharge.service.RechargeService;
import com.google.common.collect.Maps;
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


}
