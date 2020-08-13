package com.cube.kiosk.modules.recharge.service.impl;

import com.cube.kiosk.https.HttpsUtils;
import com.cube.kiosk.modules.recharge.service.RechargeService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 李晋
 */
@Service
public class RechargeServiceImpl implements RechargeService {

    @Override
    public String getQrCode(Double money) {
        String result = "";
        try {
            result = HttpsUtils.doPost("a", Maps.newHashMap());
        } catch (Exception e) {

        } finally {

           return result;

        }
    }

    /**
     * 查询患者基本信息
     *
     * @param cardId
     * @return
     */
    @Override
    public String getPatientInfo(String cardId) {
        String result = "";
        try {
            Map<String,Object> map = Maps.newHashMap();
            map.put("cardId",cardId);
            result = HttpsUtils.doPost("getPatientnameInfo", map);
        } catch (Exception e) {

        } finally {

            return result;

        }
    }


}
