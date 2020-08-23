package com.cube.kiosk.modules.recharge.service;

public interface RechargeService {

    /**
     * 获取二维码
     * @param money
     * @return
     */
    String getQrCode(Double money) throws Exception;

    /**
     * 查询患者基本信息
     * @param cardId
     * @return
     */
    String getPatientInfo(String cardId) throws Exception;
}
