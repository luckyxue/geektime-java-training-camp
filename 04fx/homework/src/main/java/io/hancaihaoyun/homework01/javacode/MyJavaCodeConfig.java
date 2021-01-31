package io.hancaihaoyun.homework01.javacode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyJavaCodeConfig {

    @Bean
    public MyJavaCodeExample myJavaCodeExample() {
        return new MyJavaCodeExample();
    }
}
