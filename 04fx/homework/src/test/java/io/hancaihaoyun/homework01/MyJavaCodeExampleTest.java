package io.hancaihaoyun.homework01;

import io.hancaihaoyun.homework01.javacode.MyJavaCodeConfig;
import io.hancaihaoyun.homework01.javacode.MyJavaCodeExample;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// Java编码注入测试不需要添加注解
public class MyJavaCodeExampleTest {

    @Test
    public void test() {
        AnnotationConfigApplicationContext configApplicationContext =
                new AnnotationConfigApplicationContext(MyJavaCodeConfig.class);
        MyJavaCodeExample example = (MyJavaCodeExample) configApplicationContext.getBean("myJavaCodeExample");
        example.info();
    }
}
