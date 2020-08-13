package com.cube.kiosk.modules.hardware;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Aspect
@Order(4)
public class ReadCardAop {

    @Around(value = "@annotation(com.cube.kiosk.modules.hardware.ReadCard)")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint){
        Object object = null;

        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject("http://localhost:50047/api/ReadCard",String.class);
            Object[] args = proceedingJoinPoint.getArgs();
            args[0] = result;
            object = proceedingJoinPoint.proceed(args);
        } catch (Throwable throwable) {

        } finally {

            return object;

        }
    }
}
