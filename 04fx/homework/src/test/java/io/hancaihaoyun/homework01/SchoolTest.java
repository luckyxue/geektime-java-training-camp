package io.hancaihaoyun.homework01;

import io.hancaihaoyun.homework01.school.School;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SchoolTest {

    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("BeanConfig.xml");
        School school = (School) context.getBean("school1");
        System.out.println(school.toString());
    }
}
