package com.cube.kiosk.modules.recharge.service;

import com.cube.kiosk.modules.common.ResultData;
import com.cube.kiosk.modules.common.model.PatientInfo;
import com.cube.kiosk.modules.recharge.linstener.RechargeLinstener;

public interface RechargeService {

    /**
     * 获取二维码
     * @param money
     * @return
     */
    String getQrCode(Double money) throws Exception;

    /**
     * 查询消费订单
     * @param tradeNo ：支付订单号
     * @return
     * @throws Exception
     */
    void getConsumer0rder(String tradeNo, RechargeLinstener linstener);

    /**
     * 查询患者基本信息
     * @param cardId
     * @return
     */
    String getPatientInfo(String cardId) throws Exception;

    /**
     * 查询就在卡余额
     * @return
     * @throws Exception
     */
    void getBalance(String patientName, String identitycard, RechargeLinstener linstener);
}
