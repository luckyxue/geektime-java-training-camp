package java0.homework;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * FutureTask
 */
public class FutureTaskMethod {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        FutureTask<Integer> sum = new FutureTask<>(new Sum());
        FutureTask<Integer> get = new FutureTask<>(new Get(sum));

        Thread sumT = new Thread(sum);
        sumT.start();
        Thread getT = new Thread(get);
        getT.start();

        int result = get.get(); //这是得到的返回值
        // 确保拿到result 并输出
        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        // 然后退出main线程
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }

    static class Get implements Callable<Integer> {
        FutureTask<Integer> sum;

        public Get(FutureTask<Integer> sum) {
            this.sum = sum;
        }

        @Override
        public Integer call() throws Exception {
            return sum.get();
        }
    }

    static class Sum implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            return fibo(45);
        }

        private int fibo(int a) {
            if (a < 2) {
                return 1;
            }
            return fibo(a - 1) + fibo(a - 2);
        }
    }
}
