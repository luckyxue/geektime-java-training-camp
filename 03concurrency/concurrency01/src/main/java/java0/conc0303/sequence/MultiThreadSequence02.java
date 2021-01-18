package java0.conc0303.sequence;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程顺序打印0~10000
 */
public class MultiThreadSequence02 {
    // 控制变量
    private int flag = 0;
    // 定义Object对象为锁
    private Object object = new Object();

    private ReentrantLock lock = new ReentrantLock();

    public MultiThreadSequence02() {
    }

    public static void main(String[] args) {

    }

    public void first(Runnable printFirst) throws InterruptedException {
        try {
            lock.lock();
            while (flag != 0) {
                object.wait();
            }
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            // 定义成员变量为 1
            flag = 1;
            // 唤醒其余所有的线程
            object.notifyAll();
        } finally {
            lock.unlock();
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        try {
            lock.lock();
            while (flag != 1) {
                object.wait();
            }
            // printFirst.run() outputs "first". Do not change or remove this line.
            printSecond.run();
            // 定义成员变量为 1
            flag = 2;
            // 唤醒其余所有的线程
            object.notifyAll();
        } finally {
            lock.unlock();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        try {
            lock.lock();
            while (flag != 2) {
                object.wait();
            }
            // printFirst.run() outputs "first". Do not change or remove this line.
            printThird.run();
            // 定义成员变量为 1
            flag = 0;
            // 唤醒其余所有的线程
            object.notifyAll();
        } finally {
            lock.unlock();
        }
    }
}
