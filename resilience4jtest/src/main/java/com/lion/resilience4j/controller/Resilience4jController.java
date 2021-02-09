package com.lion.resilience4j.controller;

import com.lion.normal.bean.TestBean;
import com.lion.resilience4j.service.Resilience4jThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/circuitbreak/resilience4j")
public class Resilience4jController {

    @Autowired
    private Resilience4jThirdPartyService resilience4jThirdPartyService;

    @GetMapping("/{mills}")
    public TestBean test(@PathVariable String mills) {
        return resilience4jThirdPartyService.callThirdParty(mills);
    }
}
