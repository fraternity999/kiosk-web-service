package com.cube.kiosk.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

@Configuration
public class ActiveMqConfig {

    @Bean
    public Queue queue1() {
        return new ActiveMQQueue("ClientReceive1");
    }

    @Bean
    public Queue queue2() {
        return new ActiveMQQueue("ClientReceive2");
    }

    @Bean
    public Queue queue3() {
        return new ActiveMQQueue("ClientReceive3");
    }
}
