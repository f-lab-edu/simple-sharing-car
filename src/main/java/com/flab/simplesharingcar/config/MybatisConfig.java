package com.flab.simplesharingcar.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.flab.simplesharingcar.mapper"})
public class MybatisConfig {

}
