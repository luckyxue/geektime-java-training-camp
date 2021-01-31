package io.hancaihaoyun.homework01.javacode;

import org.springframework.stereotype.Component;

/**
 * Java代码方式，Bean装配
 */
@Component
public class MyJavaCodeExample {

    public MyJavaCodeExample() {
        System.out.println("Construct Example");
    }

    public void info() {
        System.out.println("Java Code wiring example");
    }
}
