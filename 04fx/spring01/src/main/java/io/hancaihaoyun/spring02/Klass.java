package io.hancaihaoyun.spring02;

import io.hancaihaoyun.spring01.Student;
import lombok.Data;

import java.util.List;

@Data
public class Klass {

    List<Student> students;

    public void dong() {
        System.out.println(this.getStudents());
    }

}
