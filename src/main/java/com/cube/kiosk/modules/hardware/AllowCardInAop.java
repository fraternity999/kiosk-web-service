package com.cube.kiosk.modules.hardware;

import com.cube.kiosk.modules.common.ResponseData;
import com.cube.kiosk.modules.common.ResponseDatabase;
import com.cube.kiosk.modules.common.model.PatientInfo;
import com.cube.kiosk.modules.hardware.utils.IpUtil;
import com.cube.kiosk.modules.hardware.utils.RestTemplateUtil;
import com.google.common.collect.Maps;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Order(1)
public class AllowCardInAop {

    private String inPutParam = "<invoke name=\" READCARDALLOWCARDIN \">" +
            "<arguments>" +
            "[<string id=”ONLY_MAG”>TRUE</string>]" +
            "</arguments>" +
            "</invoke>";

    @Around(value = "@annotation(com.cube.kiosk.modules.hardware.AllowCardIn)")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint){
        Object object = null;
        String ip = IpUtil.getRemoteAddr(proceedingJoinPoint);
        if("127.0.0.1".equalsIgnoreCase(ip)){
            ip = "localhost";
        }
        try {
            String result = RestTemplateUtil.post(ip,inPutParam);
            if(result.indexOf("SUCCESS")>0){
                object = proceedingJoinPoint.proceed();
            }else{
                ResponseData responseData = ResponseDatabase.newResponseData();
                responseData.setCode(500);
                responseData.setData(null);
                responseData.setType("AllowCardIn");
                responseData.setMessage("允许进卡失败");
                object = responseData;
            }
        } catch (Exception throwable) {
            System.out.println(throwable.getMessage());
        } finally {

            return object;

        }
    }
}
