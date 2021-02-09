# circuitbreakusage
熔断器的使用学习和比较

## circuitbreakserver
作为一个第三方服务，只提供了一个接口。根据传入数值型入参执行对应时间的休眠

## normal
没有使用熔断机制的请求

## hystrixtest
使用 Hystrix 实现熔断降级和限流

## resilience4jtest
使用 Resilience4j 实现熔断降级和限流
- 使用文档：https://github.com/lmhmhl/Resilience4j-Guides-Chinese/blob/main/index.md

## sentineltest
使用 sentinel 实现熔断降级
限流测试失败
wiki: https://github.com/alibaba/Sentinel/wiki/
