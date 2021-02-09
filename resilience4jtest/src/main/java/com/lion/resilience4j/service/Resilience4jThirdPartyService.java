package com.lion.resilience4j.service;

import com.lion.normal.bean.TestBean;
import com.lion.normal.constance.UrlConstance;
import com.lion.normal.sevice.ThirdPartyService;
import com.lion.resilience4j.constant.BreakerName;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class Resilience4jThirdPartyService extends ThirdPartyService {

    public Resilience4jThirdPartyService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Bulkhead(name = BreakerName.BACKEND_A)
    @CircuitBreaker(name = BreakerName.BACKEND_A, fallbackMethod = "fallbackForResilience4j")
    public TestBean callThirdParty(String mills) {
        return pullCallThirdParty(mills);
    }

    public TestBean fallbackForResilience4j(String mills, Throwable e) {
        TestBean testBean = new TestBean();
        testBean.setMessage("fail");
        if (e instanceof CallNotPermittedException || e instanceof BulkheadFullException) {
            testBean.setStatus(TestBean.QUICK_FAIL_CODE);
        } else {
            testBean.setStatus(TestBean.FAIL_CODE);
        }
        return testBean;
    }
}
