package com.lion.resilience4j.service;

import com.lion.normal.bean.TestBean;
import com.lion.resilience4j.constant.BreakerName;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Resilience4jThirdPartyServiceSameDiffTest {
    @Autowired
    private Resilience4jThirdPartyService resilience4jThirdPartyService;

    @Before
    public void before() {
        CircuitBreaker circuitBreakerA = CircuitBreaker.ofDefaults(BreakerName.BACKEND_A);
        circuitBreakerA.reset();
        CircuitBreaker circuitBreakerB = CircuitBreaker.ofDefaults(BreakerName.BACKEND_B);
        circuitBreakerB.reset();
    }

    @Test
    public void same_diff_Instance() {
        callBackendA1();
        callBackendA2();
        callBackendA3();
        callBackendB();
        callBackendA4();
    }

    /**
     * 入参 mill
     * 出参 TestBean
     * name BackendA
     */
    private void callBackendA1() {
        int quickFailTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            TestBean testBean = resilience4jThirdPartyService.callThirdParty("10000");
            if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                quickFailTimes++;
            }
        }
        Assert.assertEquals(5, quickFailTimes);
    }

    /**
     * 入参 无
     * 出参 TestBean
     * name BackendA
     */
    private void callBackendA2() {
        int quickFailTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            TestBean testBean = resilience4jThirdPartyService.callThirdParty2();
            if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                quickFailTimes++;
            }
        }
        Assert.assertEquals(10, quickFailTimes);
    }

    /**
     * 入参 String, String, String
     * 出参 TestBean
     * name BackendA
     */
    private void callBackendA3() {
        int quickFailTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            TestBean testBean = resilience4jThirdPartyService.callThirdParty3("a", "b", "c");
            if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                quickFailTimes++;
            }
        }
        Assert.assertEquals(10, quickFailTimes);
    }

    /**
     * 入参 无
     * 出参 String
     * name BackendA
     */
    private void callBackendA4() {
        int successTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            int a = resilience4jThirdPartyService.callThirdParty4(1);
            successTimes = successTimes + a;
        }
        Assert.assertEquals(10, successTimes);
    }

    /**
     * 入参 无
     * 出参 TestBean
     * name BackendB
     */
    private void callBackendB() {
        int quickFailTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            TestBean testBean = resilience4jThirdPartyService.callThirdPartyB1();
            if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                quickFailTimes++;
            }
        }
        Assert.assertEquals(0, quickFailTimes);
    }
}
