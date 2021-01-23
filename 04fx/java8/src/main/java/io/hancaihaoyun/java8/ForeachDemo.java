package io.hancaihaoyun.java8;

import java.util.Arrays;
import java.util.List;

/**
 * 这个一个非常典型的ForeEach更新变量的例子
 */
public class ForeachDemo {

    private int x = 1;

    public static void main(String[] args) {

        ForeachDemo demo = new ForeachDemo();

        demo.test();

        System.out.println(demo.x);
    }

    private void test() {
        List list = Arrays.asList(1, 2);
        int y = 1;
        list.forEach(e -> {
            x = 2;
            // y must be finally
            //y=2;  // can't be compiled
        });
    }

}
