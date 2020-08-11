package com.cube.kiosk.modules.hardware;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Aspect
public class AllowCardInAop {

    @Around(value = "@annotation(com.cube.kiosk.modules.hardware.AllowCardIn)")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint){
        Object object = null;

        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject("http://localhost:50047/api/allowcardin",String.class);
            if(result.indexOf("SUCCESS")>0){
                object = proceedingJoinPoint.proceed();
            }else{
                object = "允许进卡失败";
            }
        } catch (Throwable throwable) {

        } finally {

            return object;

        }
    }
}
