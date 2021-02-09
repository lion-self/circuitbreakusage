package com.lion.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lion.normal.bean.TestBean;
import com.lion.sentinel.config.SentinelAspectConfiguration;
import com.lion.sentinel.service.SentinelThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/circuitbreak/sentinel/")
public class SentinelController {

    @Autowired
    private SentinelThirdPartyService sentinelThirdPartyService;

    @GetMapping("/{mills}")
    @SentinelResource(value = SentinelAspectConfiguration.FLOW_RULE_TEST,
            blockHandler = "failMethod", fallback = "fallBack")
    public TestBean test(@PathVariable String mills) {
        return sentinelThirdPartyService.callThirdParty(mills);
    }

    public TestBean failMethod(String mills, BlockException e) {
        TestBean testBean = new TestBean();
        testBean.setStatus(TestBean.QUICK_FAIL_CODE);
        testBean.setMessage("系统繁忙，请稍后重试");
        return testBean;
    }

    public TestBean fallBack(String mills, Exception e) {
        TestBean testBean = new TestBean();
        testBean.setStatus(TestBean.FAIL_CODE);
        testBean.setMessage("系统繁忙，请稍后重试");
        return testBean;
    }
}
