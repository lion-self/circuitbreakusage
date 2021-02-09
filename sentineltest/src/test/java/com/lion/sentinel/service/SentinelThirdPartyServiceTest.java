package com.lion.sentinel.service;

import com.lion.normal.bean.TestBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SentinelThirdPartyServiceTest {

    @Autowired
    private SentinelThirdPartyService sentinelThirdPartyService;

    @Test
    public void testCallThirdParty_slowCall() {
        long startTime = System.currentTimeMillis();
        int quickFailTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            TestBean testBean = sentinelThirdPartyService.callThirdParty("10000");
            if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                quickFailTimes++;
            }
        }
        Assert.assertEquals(5, quickFailTimes);
        System.out.println("请求耗时：" + (System.currentTimeMillis() - startTime));
    }

    @Test
    public void testCallThirdParty_error() {
        long startTime = System.currentTimeMillis();
        int quickFailTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            TestBean testBean = sentinelThirdPartyService.callThirdParty("哎呀");
            if (testBean.getStatus() == TestBean.QUICK_FAIL_CODE) {
                quickFailTimes++;
            }
        }
        Assert.assertEquals(5, quickFailTimes);
        System.out.println("请求耗时：" + (System.currentTimeMillis() - startTime));
    }
}