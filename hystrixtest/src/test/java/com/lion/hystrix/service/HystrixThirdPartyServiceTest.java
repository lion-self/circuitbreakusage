package com.lion.hystrix.service;

import com.lion.normal.bean.TestBean;
import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.apache.commons.lang3.concurrent.CircuitBreaker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Ignore
@SpringBootTest
@RunWith(SpringRunner.class)
public class HystrixThirdPartyServiceTest {
    @Autowired
    private HystrixThirdPartyService hystrixThirdPartyService;

    @Test
    public void testCallThirdParty_error() {
        int quickFailTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            TestBean testBean = hystrixThirdPartyService.callThirdParty("哎呀");
            if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                quickFailTimes++;
            }
        }
        Assert.assertEquals(10, quickFailTimes);
    }

    @Test
    public void testCallThirdParty_slowCall() {
        int quickFailTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            TestBean testBean = hystrixThirdPartyService.callThirdParty("10000");
            if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                quickFailTimes++;
            }
        }
        Assert.assertEquals(10, quickFailTimes);
    }

    @Test
    public void testCallThirdParty_concurrent() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        int times = 5000;
        AtomicInteger errorCount = new AtomicInteger();
        AtomicInteger hystrixFailCount = new AtomicInteger();
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(times);
        for(int i = 0; i < times; i ++) {
            executorService.submit(() -> {
                try {
                    TestBean testBean = hystrixThirdPartyService.callThirdParty("100");
                    if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                        errorCount.getAndIncrement();
                    }
                } catch(HystrixRuntimeException e) {
                    hystrixFailCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
            Assert.assertEquals(5000, errorCount.addAndGet(hystrixFailCount.get()));
            System.out.println("经过熔断器包装调用接口，短时间大量请求，远程服务执行超时进行快速失败，请求耗时：" + (System.currentTimeMillis() - startTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}