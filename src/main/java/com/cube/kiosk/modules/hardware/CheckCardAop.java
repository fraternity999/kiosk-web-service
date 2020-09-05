package com.cube.kiosk.modules.hardware;

import com.cube.core.common.utils.IpUtil;
import com.cube.kiosk.modules.common.ResponseData;
import com.cube.kiosk.modules.common.ResponseDatabase;
import com.cube.kiosk.modules.hardware.utils.RestTemplateUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 检测是否有卡
 * @author 李晋
 */
@Component
@Aspect
@Order(2)
public class CheckCardAop {

    private String inPutParam = "<invoke name=\" READCARDTESTINSERTCARD \">\n" +
            "<arguments>\n" +
            "</arguments>\n" +
            "</invoke>";

    @Around(value = "@annotation(com.cube.kiosk.modules.hardware.CheckCard)")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint){
        Object object = null;
        String ip = IpUtil.getRemoteAddr(proceedingJoinPoint);
        if("127.0.0.1".equalsIgnoreCase(ip)){
            ip = "localhost";
        }
        try {
            String result = RestTemplateUtil.post(ip,inPutParam);
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
