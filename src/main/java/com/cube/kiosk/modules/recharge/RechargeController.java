package com.cube.kiosk.modules.recharge;

import com.cube.core.system.annotation.SysLog;
import com.cube.kiosk.modules.common.*;
import com.cube.kiosk.modules.common.model.PatientInfo;
import com.cube.kiosk.modules.hardware.AllowCardIn;
import com.cube.kiosk.modules.hardware.CheckCard;
import com.cube.kiosk.modules.hardware.MoveCard;
import com.cube.kiosk.modules.hardware.ReadCard;
import com.cube.kiosk.modules.pay.entity.TransactionRecordDO;
import com.cube.kiosk.modules.recharge.linstener.RechargeLinstener;
import com.cube.kiosk.modules.recharge.service.RechargeService;
import com.cube.kiosk.modules.recharge.service.TransactionRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("api/recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private TransactionRepository transactionRepository;

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
            result = rechargeService.getPatientnameInfo("610529198909068562");
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
     * @param request
     * @return
     */
    @RequestMapping("patientInfoIndex")
    public ResponseData<PatientInfo> patientInfoIndex(HttpServletRequest request){
        ResponseData<PatientInfo> responseData = ResponseDatabase.newResponseData();
        try {
            //获取住院号
            String inHosid= request.getParameter("inHosid");
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
    public ResponseData<PatientInfo> cardIssuers(String cardId, PatientInfo patientInfo, HttpServletRequest request){
        ResponseData<PatientInfo> responseData = ResponseDatabase.newResponseData();
        patientInfo.setCardID(cardId);
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
     * @param PayType 支付方式
     * @return
     */
    @GetMapping("qrCode")
    public String getQrCode(Double money,String PayType,HttpServletRequest request){

        //1.获取下单金额、交易方式
        PatientInfo patientInfo = new PatientInfo();
        TransactionRecordDO tRDO = new TransactionRecordDO();
        tRDO.setIp("");//自助机ip地址
        tRDO.setMoney(money); //交易金额
        tRDO.setPayType(PayType);//交易方式

        String result = "";
        try {
            //2，获取患者信息
            String patiResult = rechargeService.getPatientnameInfo("610529198909068562");
            Gson gson = new Gson();
            ResultData<PatientInfo> patientInfoResultData = gson.fromJson(patiResult, new TypeToken<ResultData<PatientInfo>>(){}.getType());
            //3.获取下单二维码
            result = rechargeService.getQrCode(money);
            ResultData<ResponseBank> responseBankResultData = gson.fromJson(result, new TypeToken<ResultData<ResponseBank>>(){}.getType());
            result=responseBankResultData.getResponseData().getScanCode();//二维码信息

            //创建下单数据
            tRDO.setPatientName(patientInfoResultData.getResponseData().getPatientname());//患者姓名
            tRDO.setIdNumber(patientInfoResultData.getResponseData().getIdentitycard());//身份证号
            tRDO.setCardID(patientInfoResultData.getResponseData().getCardID());//就诊卡
            tRDO.setTradeNo(responseBankResultData.getResponseData().getTradeNo());//支付订单号
        } catch (Exception e) {
            tRDO.setTradeMsg(e.getMessage());
        }finally {
            tRDO.setTradeDate(new Date());//订单时间
            //4.记录下单数据
            transactionRepository.save(tRDO);
            return result;
        }
    }

    /**
     *消费订单查询-就诊卡充值
     * @param tradeNo
     * @return
     */
    @GetMapping("qrCon0rderCard")
    public ResponseData<PatientInfo> querConsumer0rder(String tradeNo){
        String result = "";
        ResponseData<PatientInfo> responseData = ResponseDatabase.newResponseData();
        PatientInfo patientInfo = new PatientInfo();
        ResponseBank responseBank = new ResponseBank();
        try {
            //1.根据支付订单号查询消费订单
            boolean flag = true;
            long startTime = System.currentTimeMillis();
            while (flag){
                long endTime = System.currentTimeMillis();
                //调用就诊卡充值消费订单请求接口查询结果
                rechargeService.getConsumer0rderCard(tradeNo, new RechargeLinstener() {
                    @Override
                    public void success(Object object) {
                        responseData.setCode(200);
                    }

                    @Override
                    public void error(Object object) {

                    }

                    @Override
                    public void exception(Object object) {
                        responseData.setCode(500);
                        responseData.setData(null);
                        responseData.setMessage(object.toString());
                    }
                });

                //订单查询正确结果或异常结果时终止循环
                if(responseData.getCode()>0){
                    break;
                }
                //时间超过
                if(endTime-startTime>10){
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage("超时");
                    break;
                }
            }
            //消费订单数据记录
            TransactionRecordDO tRDO = new TransactionRecordDO();
            tRDO.setId("");//支付id
            tRDO.setRespCode(responseData.getCode());//消费订单状态
            tRDO.setRespMsg(responseData.getMessage());//消费订单结果
            tRDO.setRespDate(new Date());//消费订单时间
            transactionRepository.save(tRDO);//4.记录日志

            //调用HIS就诊卡充值接口
            rechargeService.payMedicalCard(patientInfo, responseBank, new RechargeLinstener() {
                @Override
                public void success(Object object) {
                    responseData.setMessage("支付成功");
                }

                @Override
                public void error(Object object) {
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage("HIS支付，请联系管理员");
                }

                @Override
                public void exception(Object object) {
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage("HIS支付异常，请联系管理员");
                }
            });
        } catch (Exception e) {
            responseData.setCode(500);
            responseData.setData(null);
            responseData.setMessage(e.getMessage());
        }finally {
            return responseData;
        }
    }

    /**
     *消费订单查询-就诊卡充值
     * @param tradeNo
     * @return
     */
    @GetMapping("qrCon0rderPaymen")
    public ResponseData<PatientInfo> querConsumerPaymen(String tradeNo){
        String result = "";
        ResponseData<PatientInfo> responseData = ResponseDatabase.newResponseData();
        PatientInfo patientInfo = new PatientInfo();
        ResponseBank responseBank = new ResponseBank();
        try {
            //1.根据支付订单号查询消费订单
            boolean flag = true;
            long startTime = System.currentTimeMillis();
            while (flag){
                long endTime = System.currentTimeMillis();
                //调用住院预交金消费订单请求接口查询结果
                rechargeService.getConsumer0rderPaymen(tradeNo, new RechargeLinstener() {
                    @Override
                    public void success(Object object) {
                        responseData.setCode(200);

                    }

                    @Override
                    public void error(Object object) {

                    }

                    @Override
                    public void exception(Object object) {
                        responseData.setCode(500);
                        responseData.setData(null);
                        responseData.setMessage(object.toString());
                    }
                });

                //订单查询正确结果或异常结果时终止循环
                if(responseData.getCode()>0){
                    break;
                }
                //时间超过
                if(endTime-startTime>10){
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage("超时");
                    break;
                }
            }
            rechargeService.paymenPrepaid(patientInfo, responseBank, new RechargeLinstener() {
                @Override
                public void success(Object object) {
                    responseData.setMessage("支付成功");
                }

                @Override
                public void error(Object object) {
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage("HIS支付，请联系管理员");
                }

                @Override
                public void exception(Object object) {
                    responseData.setCode(500);
                    responseData.setData(null);
                    responseData.setMessage("HIS支付异常，请联系管理员");
                }
            });
        } catch (Exception e) {
            responseData.setCode(500);
            responseData.setData(null);
            responseData.setMessage(e.getMessage());
        }finally {
            return responseData;
        }
    }

    @RequestMapping("/deferredresult")
    public String deferredResult() throws Exception {
        //1、查询结果请求
        boolean flag = true;
        long startTime = System.currentTimeMillis();
        while (flag){
            //调用银行请求接口查询结果
            long endTime = System.currentTimeMillis();
            //if(查询到正确结果){falg = false，返回消息}
            //if(查询到错误结果){flag = false,返回消息}

            //if(时间超过规定entTime-startTime>10){falg = false,返回消息}
        }
        return "消息内容";
    }


}
