package com.cube.kiosk.modules.recharge;

import com.cube.kiosk.modules.hardware.HardWare;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recharge")
public class RechargeController {

    @GetMapping("test")
    @HardWare
    public String test(){
        return "hello";
    }

}
