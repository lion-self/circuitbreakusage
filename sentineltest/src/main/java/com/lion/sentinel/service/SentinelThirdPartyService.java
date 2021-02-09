package com.lion.sentinel.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lion.normal.bean.TestBean;
import com.lion.normal.sevice.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.lion.sentinel.config.SentinelAspectConfiguration;

@Service
public class SentinelThirdPartyService extends ThirdPartyService {

    @Autowired
    public SentinelThirdPartyService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @SentinelResource(value = SentinelAspectConfiguration.DEGRADE_RULE_TEST,
            fallback = "fallbackForSentinel", // 请求异常
            blockHandler = "handleFlowQpsException") // 熔断降级
    public TestBean callThirdParty(String mills) {
        return pullCallThirdParty(mills);
    }

    public TestBean fallbackForSentinel(String mills, Throwable e) {
        TestBean testBean = new TestBean();
        testBean.setStatus(TestBean.FAIL_CODE);
        testBean.setMessage("fail");
        return testBean;
    }

    public TestBean handleFlowQpsException(String mills, BlockException e) {
        TestBean testBean = new TestBean();
        testBean.setStatus(TestBean.QUICK_FAIL_CODE);
        testBean.setMessage("fail");
        return testBean;
    }
}
