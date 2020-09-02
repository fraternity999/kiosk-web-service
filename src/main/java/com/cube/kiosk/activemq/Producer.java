package com.cube.kiosk.activemq;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Queue;
import java.io.*;
import java.net.Socket;
import java.util.UUID;

@Component
public class Producer {

    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Resource(name = "queue1")
    private Queue queue1;

    @Resource(name = "queue2")
    private Queue queue2;

    @Resource(name = "queue3")
    private Queue queue3;

    public void sendMsg1(String msg) {
        this.jmsMessagingTemplate.convertAndSend(this.queue1, msg);
    }

    public void sendMsg2(String msg) {
        this.jmsMessagingTemplate.convertAndSend(this.queue2, msg);
    }

    public void sendMsg3(String msg) {
        this.jmsMessagingTemplate.convertAndSend(this.queue3, msg);
    }


}
