package com.lion.hystrix.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lion.normal.bean.TestBean;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class HystrixControllerTest extends TestCase {

    @Autowired
    private MockMvc mockMvc;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Test
    public void testTest1() {
        int size = 15;
        AtomicInteger quickFailTimes = new AtomicInteger();
        AtomicInteger successTimes = new AtomicInteger();
        String url = "/circuitbreak/hystrix/1000";
        MockHttpServletRequestBuilder mockBuilder = MockMvcRequestBuilders.get(url);
        CountDownLatch latch = new CountDownLatch(size);
        for (int i = 0; i<size; i++) {
            executorService.submit(()->{
                try {
                    MvcResult mvcResult = mockMvc.perform(mockBuilder)
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
                    JSONObject jsonObject = JSON.parseObject(mvcResult.getResponse().getContentAsString());
                    if (jsonObject.getInteger(TestBean.FIELD_STATUS) == TestBean.QUICK_FAIL_CODE) {
                        quickFailTimes.getAndIncrement();
                    }
                    if (jsonObject.getInteger(TestBean.FIELD_STATUS) == TestBean.SUCCESS_CODE) {
                        successTimes.getAndIncrement();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
            Assert.assertEquals(10, successTimes.get());
            Assert.assertEquals(5, quickFailTimes.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}