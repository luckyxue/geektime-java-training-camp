package java0.homework;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * Thread join 方式
 */
public class ThreadJoinMethod {

    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        AtomicInteger value = new AtomicInteger();
        Thread thread = new Thread(() -> {
            value.set(sum(36));
        });
        thread.start();
        thread.join();
        int result = value.get(); //这是得到的返回值
        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        // 然后退出main线程
    }

    private static int sum(int num) {
        return fibo(num);
    }

    private static int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }

}
