package com.lion.normal.sevice;

import com.lion.normal.bean.TestBean;
import com.lion.normal.constance.UrlConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ThirdPartyService {

    private final RestTemplate restTemplate;

    @Autowired
    public ThirdPartyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TestBean callThirdParty(String mills) {
        return pullCallThirdParty(mills);
    }

    protected TestBean pullCallThirdParty(String mills) {
        System.out.println(Thread.currentThread().getName() + ":开始执行远程调用...");
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("mills", mills);
        return restTemplate.getForObject(UrlConstance.URL, TestBean.class, uriVariables);
    }

}
