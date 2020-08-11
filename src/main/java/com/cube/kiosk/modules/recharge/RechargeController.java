package com.cube.kiosk.modules.recharge;

import com.cube.kiosk.modules.hardware.AllowCardIn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recharge")
public class RechargeController {

    @GetMapping("index")
    @AllowCardIn
    public String index(){
        return "hello";
    }

}
