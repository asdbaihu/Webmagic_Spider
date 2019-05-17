package com.lenovo.frame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author XiuChong@lenovo.com
 * @data 2019/5/16 14:11
 * @description SpringBoot的启动类
 */
@SpringBootApplication
@EnableScheduling
public class SpringBootApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationRunner.class,args);
    }
}
