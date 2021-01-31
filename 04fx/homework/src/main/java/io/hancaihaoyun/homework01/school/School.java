package io.hancaihaoyun.homework01.school;

import java.util.List;

public class School {

    private List<Klass> myClasses;

    public List<Klass> getMyClasses() {
        return myClasses;
    }

    public void setMyClasses(List<Klass> myClasses) {
        this.myClasses = myClasses;
    }

    @Override
    public String toString() {
        return "MyClass::" + myClasses.toString();
    }
}
