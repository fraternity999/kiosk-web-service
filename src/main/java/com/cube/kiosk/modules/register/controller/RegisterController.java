package com.cube.kiosk.modules.register.controller;

import com.cube.core.system.annotation.SysLog;
import com.cube.kiosk.modules.hardware.AllowCardIn;
import com.cube.kiosk.modules.hardware.CheckCard;
import com.cube.kiosk.modules.hardware.MoveCard;
import com.cube.kiosk.modules.hardware.ReadCard;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YYF
 */
@RestController
@RequestMapping("api/register")
public class RegisterController {


    @RequestMapping("index")
    @SysLog("办卡")
    public String index(){
        return null;
    }

}
