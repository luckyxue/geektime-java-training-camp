package java0.conc0303.sequence;

import java.util.concurrent.CountDownLatch;

/**
 * 多线程顺序打印0~10000
 */
public class MultiThreadSequence03 {

    private CountDownLatch countDownLatch01;

    private CountDownLatch countDownLatch02;

    public MultiThreadSequence03() {
        countDownLatch01 = new CountDownLatch(1);
        countDownLatch02 = new CountDownLatch(1);
    }

    public static void main(String[] args) {

    }

    public void first(Runnable printFirst) throws InterruptedException {
        // 当前只有first线程没有任何的阻碍，其余两个线程都处于等待阶段
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        //直到CountDownLatch01里面计数为0才执行因调用该countDownCatch01.await()而等待的线程
        countDownLatch01.countDown();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        // 只有countDownLatch01为0才能通过，否则会一直阻塞
        countDownLatch01.await();
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        //直到CountDownLatch02里面计数为0才执行因调用该countDownCatch02.await()而等待的线程
        countDownLatch02.countDown();
    }

    public void third(Runnable printThird) throws InterruptedException {
        // 只有countDownLatch02为0才能通过，否则会一直阻塞
        countDownLatch02.await();
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }
}
