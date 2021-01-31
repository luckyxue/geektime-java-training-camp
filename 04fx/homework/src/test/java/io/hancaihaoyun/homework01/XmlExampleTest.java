package io.hancaihaoyun.homework01;

import io.hancaihaoyun.homework01.xml.XmlExample;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

// xml注入测试不需要添加注解
public class XmlExampleTest {

    @Test
    public void test() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("BeanConfig.xml");
        XmlExample example = (XmlExample) context.getBean("XmlExample");
        example.info();
    }
}
