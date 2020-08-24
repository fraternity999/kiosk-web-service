package com.cube.kiosk.modules.hardware;

import com.cube.kiosk.modules.common.ResponseData;
import com.cube.kiosk.modules.common.ResponseDatabase;
import com.cube.kiosk.modules.hardware.utils.IpUtil;
import com.cube.kiosk.modules.hardware.utils.RestTemplateUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;

@Component
@Aspect
@Order(4)
public class ReadCardAop {

    private String inPutParam = "<invoke name=\" READCARDREADRFCARD \">\n" +
            "<arguments>\n" +
            "<string id=\"SECTORNO\">0</string>\n" +
            "<string id=\"BLOCKNO\">0</string>\n" +
            "<string id=\"PASSWORD\">FFFFFFFFFFFF</string>\n" +
            "</arguments>\n" +
            "</invoke>";

    @Around(value = "@annotation(com.cube.kiosk.modules.hardware.ReadCard)")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint){
        String cardNo = "";
        Object object = null;
        String ip = IpUtil.getRemoteAddr(proceedingJoinPoint);
        if("127.0.0.1".equalsIgnoreCase(ip)){
            ip = "localhost";
        }
        try {
//            String result = RestTemplateUtil.post(ip,inPutParam);
            String result = "<return name=\" READCARDREADRFCARD \">\n" +
                    "<arguments>\n" +
                    "<string id=\"ERROR\">SUCCESS</string>\n" +
                    "<string id=\"CARDNO\">12345678</string>\n" +
                    "</arguments>\n" +
                    "</return>";
            Document doc = null;
            doc = DocumentHelper.parseText(result);
            Element root = doc.getRootElement();// 指向根节点
            Iterator it = root.elementIterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();// 一个Item节点
                if("arguments".equalsIgnoreCase(element.getName())){
                    List<Element> elementList = element.elements();
                    for (int i = 0; i < elementList.size(); i++) {
                        Element elementChild = elementList.get(i);
                        Attribute attribute = elementChild.attribute(0);
                        if(attribute!=null){
                            String attrName = attribute.getName();
                            if("id".equalsIgnoreCase(attrName)){
                                String value = attribute.getValue();
                                if("ERROR".equalsIgnoreCase(value)){
                                    String text = elementChild.getText();
                                    if(!"SUCCESS".equalsIgnoreCase(text)){
                                        break;
                                    }
                                }
                                if("CARDNO".equalsIgnoreCase(value)){
                                    String text = elementChild.getText();
                                    cardNo = text;
                                }
                            }
                        }

                    }
                }

            }
            if(StringUtils.isEmpty(cardNo)){
                ResponseData responseData = ResponseDatabase.newResponseData();
                responseData.setCode(500);
                responseData.setData(null);
                responseData.setType("ReadCard");
                responseData.setMessage("没有获取到卡号");
                object = responseData;
            }else {
                Object[] args = proceedingJoinPoint.getArgs();
                args[0] = cardNo;
                object = proceedingJoinPoint.proceed(args);
            }

        } catch (Exception throwable) {
            ResponseData responseData = ResponseDatabase.newResponseData();
            responseData.setCode(500);
            responseData.setData(null);
            responseData.setType("ReadCard");
            responseData.setMessage(throwable.getMessage());
            object = responseData;
        } finally {

            return object;

        }
    }
}
