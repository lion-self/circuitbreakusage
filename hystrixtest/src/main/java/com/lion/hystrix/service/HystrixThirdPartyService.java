package com.lion.hystrix.service;

import com.lion.normal.bean.TestBean;
import com.lion.normal.constance.UrlConstance;
import com.lion.normal.sevice.ThirdPartyService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class HystrixThirdPartyService extends ThirdPartyService {

    public HystrixThirdPartyService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @HystrixCommand(fallbackMethod = "fallbackForWithHystrix",
            commandProperties={
                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"), // 设置超时时间
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="5"), // 设置失败请求阈值
                    @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="10000"), // 设置滑动窗口时长
                    @HystrixProperty(name="fallback.isolation.semaphore.maxConcurrentRequests", value = "20"),
                    @HystrixProperty(name="metrics.healthSnapshot.intervalInMilliseconds", value = "1")
            })
    public TestBean callThirdParty(String mills) {
        return pullCallThirdParty(mills);
    }

    public TestBean fallbackForWithHystrix(String mills, Throwable e) {
        TestBean testBean = new TestBean();
        testBean.setStatus(TestBean.QUICK_FAIL_CODE);
        testBean.setMessage("fail");
        return testBean;
    }
}
