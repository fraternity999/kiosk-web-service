package com.cube.kiosk.https;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 *
 * @author 李晋
 * @date 2020/08/12
 */
public class HttpsUtils {

    public static String doPost(String url, Map<String,Object> result) throws Exception {
        String charset = "utf-8";
        SSLClient sslClient = new SSLClient();
        String token = "1514c1367ae748349f31de28aef9b1ca";
        String hosId = "46c0cd66b25b4da8acbab30bd501a64c";
        String spellNo = "am";
        Map<String,Object> requestJson = Maps.newHashMap();
        result.put("token", token);
        result.put("hosId", hosId);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(result);
        requestJson.put("requestJson",jsonStr);
        String a = requestJson.toString();
        String b = a.substring(1,a.length()-1);
        String httpOrgCreateTestRtn = sslClient.doPost(url, b, charset);
        return httpOrgCreateTestRtn;
    }
}
