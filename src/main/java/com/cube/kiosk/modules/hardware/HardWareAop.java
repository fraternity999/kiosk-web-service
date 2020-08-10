package com.cube.kiosk.modules.hardware;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Aspect
public class HardWareAop {

    @Before(value = "@annotation(com.cube.kiosk.modules.hardware.HardWare)")
    public void doBefore(JoinPoint joinPoint){
        RestTemplate restTemplate = new RestTemplate();
        String notice = restTemplate.getForObject("http://localhost:50047/api/allowcardin/1",String.class);
        System.out.println(notice);
    }
}
