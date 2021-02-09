package com.lion.resilience4j.service;

import com.lion.normal.bean.TestBean;
import com.lion.resilience4j.constant.BreakerName;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Resilience4jThirdPartyServiceTest {
    @Autowired
    private Resilience4jThirdPartyService resilience4jThirdPartyService;

    @Before
    public void before() {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(BreakerName.BACKEND_A);
        circuitBreaker.reset();
    }

    @Test
    public void testCallThirdParty_error() {
        int quickFailTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            TestBean testBean = resilience4jThirdPartyService.callThirdParty("哎呀");
            if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                quickFailTimes++;
            }
        }
        Assert.assertEquals(5, quickFailTimes);
    }

    @Test
    public void testCallThirdParty_slowCall() {
        long startTime = System.currentTimeMillis();
        int quickFailTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            TestBean testBean = resilience4jThirdPartyService.callThirdParty("10000");
            if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                quickFailTimes++;
            }
        }
        Assert.assertEquals(5, quickFailTimes);
        System.out.println("请求耗时：" + (System.currentTimeMillis() - startTime));
    }

    @Test
    public void testCallThirdParty_concurrent() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        int times = 5000;
        AtomicInteger quickFailCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        AtomicInteger successCount = new AtomicInteger();
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(times);
        for(int i = 0; i < times; i ++) {
            executorService.submit(() -> {
                try {
                    TestBean testBean = resilience4jThirdPartyService.callThirdParty("1000");
                    if (testBean.getStatus() == TestBean.SUCCESS_CODE) {
                        successCount.getAndIncrement();
                    }
                    if (testBean.getStatus() == TestBean.FAIL_CODE) {
                        failCount.getAndIncrement();
                    }
                    if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                        quickFailCount.getAndIncrement();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
            System.out.println("successCount: " + successCount);
            System.out.println("quickFailCount：" + quickFailCount);
            System.out.println("failCount：" + failCount);
            Assert.assertEquals(5000, successCount.getAndIncrement() + failCount.getAndIncrement() + quickFailCount.get());
            System.out.println("经过熔断器包装调用接口，短时间大量请求，远程服务执行超时进行快速失败，请求耗时：" + (System.currentTimeMillis() - startTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}