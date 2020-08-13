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
 * 检测是否有卡
 * @author 李晋
 */
@Component
@Aspect
@Order(2)
public class CheckCardAop {

    @Around(value = "@annotation(com.cube.kiosk.modules.hardware.CheckCard)")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint){
        Object object = null;

        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject("http://localhost:50047/api/TestCard",String.class);
            if(result.indexOf("1")>0){
                object = proceedingJoinPoint.proceed();
            }else{
                ResponseData responseData = ResponseDatabase.newResponseData();
                responseData.setCode(500);
                responseData.setData(null);
                responseData.setType("CheckCard");
                responseData.setMessage("请插入您的就诊卡");
                object = responseData;
            }
        } catch (Throwable throwable) {

        } finally {

            return object;

        }
    }
}
