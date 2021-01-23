package io.hancaihaoyun.spring01;


import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {

    private int id;
    private String name;

    public void init() {
        System.out.println("hello...........");
    }

    public Student create() {
        return new Student(101, "KK101");
    }
}
