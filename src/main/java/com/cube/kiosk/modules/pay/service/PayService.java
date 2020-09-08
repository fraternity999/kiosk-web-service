package com.cube.kiosk.modules.pay.service;

import com.cube.kiosk.modules.common.model.ResultLinstener;
import com.cube.kiosk.modules.pay.model.RequestDataPay;

public interface PayService {

    void doPost(RequestDataPay requestDataPay, ResultLinstener linstener);

    void queryResult(RequestDataPay requestDataPay, ResultLinstener linstener);
}
