package java0.homework;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 通过的管程等待-通知机制，来获取值
 * synchronized方式
 */
public class SynchronizedMethod {

    private volatile Integer value = null;

    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        final SynchronizedMethod method = new SynchronizedMethod();
        Thread thread = new Thread(() -> {
            method.sum(36);
        });
        thread.start();

        int result = method.getValue(); //这是得到的返回值
        // 确保拿到result 并输出
        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        // 然后退出main线程
    }

    synchronized public void sum(int num) {
        value = fibo(num);
        notifyAll();
    }

    private int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }

    synchronized public int getValue() throws InterruptedException {
        while (value == null) {
            wait();
        }
        return value;
    }
}
