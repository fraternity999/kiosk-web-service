package com.cube.kiosk.modules.hardware;

import com.cube.core.common.utils.IpUtil;
import com.cube.kiosk.modules.common.ResponseData;
import com.cube.kiosk.modules.common.ResponseDatabase;
import com.cube.kiosk.modules.hardware.utils.RestTemplateUtil;
import com.cube.kiosk.socket.SocketUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.Socket;

/**
 * 移动卡
 * @author 李晋
 */
@Component
@Aspect
@Order(3)
public class MoveCardAop {

    @Value("${Hardware.socketIp}")
    private String socketIp;

    @Value("${Hardware.scoketPort}")
    private int socketPort;

    private String inPutParam = "<invoke name=\"READCARDMOVECARDTORF\">\n" +
            "<arguments>\n" +
            "</arguments>\n" +
            "</invoke>";


    @Around(value = "@annotation(com.cube.kiosk.modules.hardware.MoveCard)")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint){
        Object object = null;
        String ip = IpUtil.getRemoteAddr(proceedingJoinPoint);
        try {
            Socket socket = SocketUtils.sendMessage(socketIp,socketPort,inPutParam);
            String result = SocketUtils.reciveMessage(socket);
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
