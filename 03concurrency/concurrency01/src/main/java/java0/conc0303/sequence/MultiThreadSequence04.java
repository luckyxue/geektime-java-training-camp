package java0.conc0303.sequence;

import java.util.concurrent.Semaphore;

public class MultiThreadSequence04 {

    private Semaphore spa;

    private Semaphore spb;

    public MultiThreadSequence04() {
        spa = new Semaphore(0);
        spb = new Semaphore(0);
    }

    public static void main(String[] args) {
    }

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        //只有等first线程释放Semaphore后使Semaphore值为1,另外一个线程才可以调用（acquire）
        spa.release();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        // 只有spa为1才能执行acquire，如果为0就会产生阻塞
        spa.acquire();
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        spb.release();
    }

    public void third(Runnable printThird) throws InterruptedException {
        // 只有spb为1才能通过，如果为0就会阻塞
        spb.acquire();
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }
}
