package io.hancaihaoyun.homework01.xml;

import org.springframework.stereotype.Component;

/**
 * xml方式，Bean装配
 */
@Component
public class XmlExample {
    public XmlExample() {
        System.out.println("Construct Example");
    }

    public void info() {
        System.out.println("XML wiring example");
    }
}
