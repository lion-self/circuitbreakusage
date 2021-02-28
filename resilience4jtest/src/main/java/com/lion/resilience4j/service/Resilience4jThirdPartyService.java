package com.lion.resilience4j.service;

import com.lion.normal.bean.TestBean;
import com.lion.normal.constance.UrlConstance;
import com.lion.normal.sevice.ThirdPartyService;
import com.lion.resilience4j.constant.BreakerName;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Bulkhead(name = BreakerName.BACKEND_A)
@CircuitBreaker(name = BreakerName.BACKEND_A, fallbackMethod = "fallbackForResilience4j")
public class Resilience4jThirdPartyService extends ThirdPartyService {

    public Resilience4jThirdPartyService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public TestBean callThirdParty(String mills) {
        return pullCallThirdParty(mills);
    }

    public TestBean callThirdParty2() {
        return pullCallThirdParty("10");
    }

    public TestBean callThirdParty3(String a, String b, String c) {
        System.out.println("进来了");
        return new TestBean();
    }

    @CircuitBreaker(name = BreakerName.BACKEND_B, fallbackMethod = "fallbackForResilience4j")
    public TestBean callThirdPartyB1() {
        return pullCallThirdParty("10");
    }

    @CircuitBreaker(name = BreakerName.BACKEND_A, fallbackMethod = "fallbackForResilience4j")
    public int callThirdParty4(int i) {
        return i;
    }

    public TestBean fallbackForResilience4j(Throwable e) {
        TestBean testBean = new TestBean();
        testBean.setMessage("fail");
        if (e instanceof CallNotPermittedException || e instanceof BulkheadFullException) {
            testBean.setStatus(TestBean.QUICK_FAIL_CODE);
        } else {
            testBean.setStatus(TestBean.FAIL_CODE);
        }
        return testBean;
    }

    public int fallbackForResilience4j(int i, Throwable e) {
        return 0;
    }
}
