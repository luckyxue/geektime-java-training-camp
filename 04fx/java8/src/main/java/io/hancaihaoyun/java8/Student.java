package io.hancaihaoyun.java8;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Slf4j
public class Student implements Serializable, ApplicationContextAware {

    private int id;
    private String name;

    private ApplicationContext applicationContext;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void init() {
        System.out.println("init hello world");
        log.debug("hello work");
    }

    public Student create() {
        this.applicationContext.getBeanDefinitionNames();
        System.out.println("create hello world");
        Student s = new Student(101, "KK101");
        return s;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
