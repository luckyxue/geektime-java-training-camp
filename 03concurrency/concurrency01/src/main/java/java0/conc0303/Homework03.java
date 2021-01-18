package java0.conc0303;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 一个简单的代码参考：
 */
public class Homework03 {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行下面方法

        // 第一种方式: Callable
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        };
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> future = executor.submit(callable);
        executor.shutdown();
        try {
            // get本身会阻塞等待线程执行完毕
            System.out.println("异步计算结果为：" + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // 第二种方式：FutureTask
        FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });
        Thread thread02 = new Thread(task);
        thread02.start();
        try {
            // get本身会阻塞等待线程执行完毕
            System.out.println("异步计算结果为：" + task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        // 然后退出main线程
        System.out.println("退出main线程");
    }

    private static int sum() {
        return fibo2(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }

    private static int fibo2(int a) {
        int f1 = 1;
        int f2 = 1;
        int res = 0;
        if (a < 2) {
            return 1;
        }
        while (--a >= 1) {
            res = f1 + f2;
            f1 = f2;
            f2 = res;
        }
        return res;
    }
}
