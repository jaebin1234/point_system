package com.common.point.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.common.point") // TestController가 포함된 패키지
public class AppConfig {

}
