package com.cube.kiosk;

import com.cube.core.entity.UserDO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cube"})
@EntityScan(basePackages = {"com.cube"})
public class KioskApplication {

    public static void main(String[] args) {
        UserDO userDO = new UserDO();
        SpringApplication.run(KioskApplication.class, args);
    }

}
