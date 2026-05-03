package com.kiruthika.chatapp.messaging_service.api;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TranslationApi {

    public String translate(String text,String source,String target){
        RestTemplate rest=new RestTemplate();
        String url=String.format("https://lingva.ml/api/v1/%s/%s/%s",source,target,text);
        Map response=rest.getForObject(url, Map.class);
        return (String) response.get("translation");

    }

}
