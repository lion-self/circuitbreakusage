package com.lion.sentinel.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SentinelAspectConfiguration {
    public static final String DEGRADE_RULE_TEST = "degrade_rule_test";
    public static final String FLOW_RULE_TEST = "test";

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    @PostConstruct
    private void initRules() throws Exception {
        // 熔断
        DegradeRule degradeRule1 = new DegradeRule();
        degradeRule1.setResource(DEGRADE_RULE_TEST); // 指定规则名称
        degradeRule1.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT); // 定义熔断类型
        degradeRule1.setCount(5);
        degradeRule1.setTimeWindow(5000);
        List<DegradeRule> degradeRules = new ArrayList();
        degradeRules.add(degradeRule1);
        DegradeRuleManager.loadRules(degradeRules);

        FlowRule flowRule1 = new FlowRule();
        flowRule1.setRefResource(FLOW_RULE_TEST);
        flowRule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule1.setCount(1);
        flowRule1.setLimitApp(RuleConstant.LIMIT_APP_DEFAULT);
        List<FlowRule> flowRules = new ArrayList<>();
        flowRules.add(flowRule1);
        FlowRuleManager.loadRules(flowRules);
    }
}
