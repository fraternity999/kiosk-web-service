package com.cube.kiosk.modules.hardware;

import com.cube.kiosk.modules.common.ResponseData;
import com.cube.kiosk.modules.common.ResponseDatabase;
import com.cube.kiosk.modules.common.model.PatientInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 移动卡
 * @author 李晋
 */
@Component
@Aspect
@Order(3)
public class MoveCardAop {

    @Around(value = "@annotation(com.cube.kiosk.modules.hardware.MoveCard)")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint){
        Object object = null;

        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject("http://localhost:50047/api/MoveCardToRf",String.class);
            if(result.indexOf("SUCCESS")>0){
                object = proceedingJoinPoint.proceed();
            }else{
                ResponseData responseData = ResponseDatabase.newResponseData();
                responseData.setCode(500);
                responseData.setData(null);
                responseData.setType("MoveCard");
                responseData.setMessage("移动卡失败");
                object = responseData;
            }
        } catch (Throwable throwable) {

        } finally {

            return object;

        }
    }
}
