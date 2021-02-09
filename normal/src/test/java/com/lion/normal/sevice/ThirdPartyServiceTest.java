package com.lion.normal.sevice;

import com.lion.normal.bean.TestBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ThirdPartyServiceTest {

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Test
    public void callThirdParty_error() {
        try {
            thirdPartyService.callThirdParty("哎呀");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Assert.fail();
    }

    @Test
    public void callThirdParty_slowCall() {
        long startTime = System.currentTimeMillis();
        int errorTimes = 0;
        for (int i=0; i<10; i++) {
            System.out.println(Thread.currentThread().getName());
            try {
                TestBean testBean = thirdPartyService.callThirdParty("10000");
            } catch (Exception e) {
                errorTimes++;
            }
        }
        Assert.assertEquals(10, errorTimes);
        System.out.println("请求耗时：" + (System.currentTimeMillis() - startTime));
    }

}