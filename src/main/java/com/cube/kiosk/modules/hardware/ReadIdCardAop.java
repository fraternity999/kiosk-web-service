package com.cube.kiosk.modules.hardware;

import com.cube.core.common.utils.IpUtil;
import com.cube.kiosk.socket.SocketUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.Socket;

/**
 * @author YYF
 */
@Component
@Aspect
public class ReadIdCardAop {

    @Value("${Hardware.scoketPort}")
    private int socketPort;


    private String inPutParam = "<invoke name=\"SHENFENZHENG\">\n" +
            "<arguments></arguments>\n" +
            "</invoke>";

    @Around(value = "@annotation(com.cube.kiosk.modules.hardware.ReadIdCard)")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint){
        String ip = IpUtil.getRemoteAddr(proceedingJoinPoint);
        Object object = null;
        try{
            Socket socket = SocketUtils.sendMessage(ip,socketPort,inPutParam);
            String result = SocketUtils.reciveMessage(socket);
            
        }catch (Exception e){

        }finally {
            return object;
        }
    }
}
