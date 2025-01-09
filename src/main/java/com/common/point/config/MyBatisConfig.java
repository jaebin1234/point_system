package com.common.point.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.common.point.dao.mapper") // 매퍼 패키지 경로
public class MyBatisConfig {
}