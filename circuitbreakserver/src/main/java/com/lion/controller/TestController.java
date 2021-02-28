package com.lion.controller;

import com.lion.normal.bean.TestBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/something")
    public TestBean doSomething(@RequestParam(required = false) Long mills) {
        TestBean testMO = new TestBean();
        System.out.println(mills);
        if (mills != null && mills !=0) {
            try {
                Thread.sleep(mills);
            } catch (InterruptedException e) {
                testMO.setStatus(TestBean.FAIL_CODE);
                testMO.setMessage("interrupted");
                return testMO;
            }
        }
        testMO.setStatus(TestBean.SUCCESS_CODE);
        return testMO;
    }

    @GetMapping("/something2")
    public TestBean doSomething() {
        TestBean testMO = new TestBean();
        testMO.setStatus(TestBean.SUCCESS_CODE);
        return testMO;
    }
}
