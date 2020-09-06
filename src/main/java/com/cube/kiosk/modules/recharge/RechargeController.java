package com.cube.kiosk.modules.recharge;

import com.cube.core.system.annotation.SysLog;
import com.cube.kiosk.modules.common.ResponseBank;
import com.cube.kiosk.modules.common.ResponseData;
import com.cube.kiosk.modules.common.ResponseDatabase;
import com.cube.kiosk.modules.common.ResultData;
import com.cube.kiosk.modules.common.model.PatientInfo;
import com.cube.kiosk.modules.hardware.AllowCardIn;
import com.cube.kiosk.modules.hardware.CheckCard;
import com.cube.kiosk.modules.hardware.MoveCard;
import com.cube.kiosk.modules.hardware.ReadCard;
import com.cube.kiosk.modules.recharge.linstener.RechargeLinstener;
import com.cube.kiosk.modules.recharge.service.RechargeService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @AllowCardIn
    @CheckCard
    @MoveCard
    @ReadCard
    @RequestMapping("index")
    @SysLog("门诊预存")
    public ResponseData<PatientInfo> index(String cardId, HttpServletRequest request){
        String result = null;
        ResponseData<PatientInfo> responseData = ResponseDatabase.newResponseData();
        try {
            //查询患者信息
            result = rechargeService.getPatientnameInfo(cardId);
            Gson gson = new Gson();
            ResultData<Object> patientInfoResultData = gson.fromJson(result, new TypeToken<ResultData<Object>>(){}.getType());
            if(patientInfoResultData.getCode()==1){
                responseData.setCode(500);
                responseData.setData(null);
                responseData.setMessage(patientInfoResultData.getResponseData().toString());
                return responseData;
            }
            PatientInfo patientInfo = (PatientInfo) patientInfoResultData.getResponseData();
            //查询卡余额 Patientname:患者形象;  就诊卡号
            rechargeService.getBalance(patientInfo.getPatientname(), null, new RechargeLinstener() {
                @Override
                public void success(Object object) {
                    patientInfo.setBalance((Double)object);
                    responseData.setCode(200);
                    responseData.setData((PatientInfo) patientInfoResultData.getResponseData());
                    responseData.setMessage("成功");
                }

                @Override
                public void error(Object object) {
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage(object.toString());
                }

                @Override
                public void exception(Object object) {
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage(object.toString());
                }
            });
        } catch (Exception e) {
            responseData.setCode(500);
            responseData.setData(null);
            responseData.setMessage(e.getMessage());
        } finally {
            return responseData;
        }


    }

    /**
     * 住院患者信息查询
     * @param inHosid:住院号
     * @param request
     * @return
     */
    @RequestMapping("patientInfoIndex")
    public ResponseData<PatientInfo> patientInfoIndex(String inHosid, HttpServletRequest request){
        ResponseData<PatientInfo> responseData = ResponseDatabase.newResponseData();
        try {
            //查询患者信息
            rechargeService.getPatientInfo(inHosid, new RechargeLinstener() {
                @Override
                public void success(Object object) {
                    Gson gson = new Gson();
                    ResultData<PatientInfo> patientInfoResultData = gson.fromJson(object.toString(), new TypeToken<ResultData<PatientInfo>>(){}.getType());
                    rechargeService.paymenPrepaidBalance(inHosid, new RechargeLinstener() {
                        @Override
                        public void success(Object object) {
                            patientInfoResultData.getResponseData().setBalance((Double)object);
                            responseData.setCode(200);
                            responseData.setData(patientInfoResultData.getResponseData());
                            responseData.setMessage("成功");
                        }

                        @Override
                        public void error(Object object) {
                            responseData.setCode(500);
                            responseData.setData(null);
                            responseData.setMessage(object.toString());
                        }

                        @Override
                        public void exception(Object object) {
                            responseData.setCode(500);
                            responseData.setData(null);
                            responseData.setMessage(object.toString());
                        }
                    });
                }

                @Override
                public void error(Object object) {
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage(object.toString());
                }

                @Override
                public void exception(Object object) {
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage(object.toString());
                }
            });
        } catch (Exception e) {
            responseData.setCode(500);
            responseData.setData(null);
            responseData.setMessage(e.getMessage());
        } finally {
            return responseData;
        }
    }

    @GetMapping("cardNo")
    @CheckCard
    @MoveCard
    @ReadCard
    public String getCardInfo(String cardNo){
        return cardNo;
    }

    /**
     * 在线建卡
     * @param cardId
     * @param request
     * @return
     */
    @ReadCard
    @RequestMapping("cardIssuers")
    public ResponseData<PatientInfo> cardIssuers(String cardId, HttpServletRequest request){
        String result = null;
        ResponseData<PatientInfo> responseData = ResponseDatabase.newResponseData();
        PatientInfo patientInfo = new PatientInfo();
        try {
            //
            rechargeService.cardIssuers(patientInfo, new RechargeLinstener() {
                @Override
                public void success(Object object) {
                    responseData.setCode(200);
                    responseData.setMessage("成功");
                }

                @Override
                public void error(Object object) {
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage("在线建卡失败");
                }

                @Override
                public void exception(Object object) {
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage("在线建卡失败");
                }
            });
        } catch (Exception e) {
            responseData.setCode(500);
            responseData.setData(null);
            responseData.setMessage(e.getMessage());
        } finally {
            return responseData;
        }
    }

    /**
     *主扫下单
     * @param money
     * @return
     */
    @GetMapping("qrCode")
    public String getQrCode(Double money){
        String result = "";
        try {
            result = rechargeService.getQrCode(money);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    /**
     *消费订单查询
     * @param tradeNo
     * @return
     */
    @GetMapping("qrCon0rder")
    public String querConsumer0rder(String tradeNo){
        String result = "";
        PatientInfo patientInfo = new PatientInfo();
        try {
            rechargeService.getConsumer0rder(tradeNo, new RechargeLinstener() {
                @Override
                public void success(Object object) {
                    ResponseBank responseBank = new ResponseBank();
                    rechargeService.payMedicalCard(patientInfo, responseBank, new RechargeLinstener() {
                        @Override
                        public void success(Object object) {

                        }

                        @Override
                        public void error(Object object) {

                        }

                        @Override
                        public void exception(Object object) {

                        }
                    });
                }

                @Override
                public void error(Object object) {

                }

                @Override
                public void exception(Object object) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }

}
