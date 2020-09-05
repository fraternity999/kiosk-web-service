package com.cube.kiosk.modules.hardware;

import com.cube.core.common.utils.IpUtil;
import com.cube.kiosk.activemq.Producer;
import com.cube.kiosk.modules.common.ResponseData;
import com.cube.kiosk.modules.common.ResponseDatabase;
import com.cube.kiosk.socket.SocketUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.net.Socket;

@Component
@Aspect
@Order(1)
public class AllowCardInAop {




    @Autowired
    private Producer producer;

    private String inPutParam = "<invoke name=\" READCARDALLOWCARDIN \">" +
            "<arguments>" +
            "</arguments>" +
            "</invoke>";

    private String result = null;

    @Around(value = "@annotation(com.cube.kiosk.modules.hardware.AllowCardIn)")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint){
        Object object = null;
        try{
            String ip = IpUtil.getRemoteAddr(proceedingJoinPoint);
            Socket socket = SocketUtils.sendMessage("127.0.0.1",8899,inPutParam);
            result = SocketUtils.reciveMessage(socket);
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
        } catch (Throwable throwable) {
            ResponseData responseData = ResponseDatabase.newResponseData();
            responseData.setCode(500);
            responseData.setData(null);
            responseData.setType("AllowCardIn");
            responseData.setMessage("允许进卡失败:"+ throwable.getMessage());
            object = responseData;
        } finally {
            return object;
        }
    }

    @JmsListener(destination = "ClientSend1")
    public void receiveMsg(String text) {
        System.out.println("接收到消息 : "+text);
    }
}
