package com.lion.hystrix.controller;

import com.lion.normal.bean.TestBean;
import com.lion.hystrix.service.HystrixThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/circuitbreak/hystrix")
public class HystrixController {

    @Autowired
    private HystrixThirdPartyService hystrixThirdPartyService;

    @GetMapping("/{mills}")
    public TestBean test(@PathVariable String mills) {
        return hystrixThirdPartyService.callThirdParty(mills);
    }
}
