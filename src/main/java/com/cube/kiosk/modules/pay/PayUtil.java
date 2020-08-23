package com.cube.kiosk.modules.pay;

import com.google.common.collect.Maps;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class PayUtil {

    public static String post(String url,String param){
        RestTemplate restTemplate = new RestTemplate();


        HttpHeaders headers = new HttpHeaders();
//            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//            headers.setContentType(type);
//        headers.add("Accept", "*/*");
//        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
//        postParameters.add("", param);
//        headers.add("Content-Type", "application/x-www-form-urlencoded");
//        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        Map<String,Object> map = Maps.newHashMap();
        map.put("requestJson",param);
        String result = restTemplate.postForObject(url,map,String.class);
        return result;
    }
}
