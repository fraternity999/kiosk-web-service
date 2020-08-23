package com.cube.kiosk.modules.hardware.utils;

import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {

    public static String getRemoteAddr(JoinPoint joinPoint){
        String ip = "";
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if(args[i]!=null){
                String result = args[i].getClass().getName();
                if("org.apache.catalina.connector.RequestFacade".equalsIgnoreCase(result)){
                    if(args[i] instanceof HttpServletRequest){
                        HttpServletRequest httpServletRequest = (HttpServletRequest) args[i];
                        ip =httpServletRequest.getRemoteAddr();
                    }
                }
            }

        }
        return ip;
    }
}
