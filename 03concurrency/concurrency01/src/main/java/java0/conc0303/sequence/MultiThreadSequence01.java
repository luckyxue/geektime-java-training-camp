package java0.conc0303.sequence;

/**
 * 多线程顺序打印0~10000
 */
public class MultiThreadSequence01 {
    // 控制变量
    private int flag = 0;
    // 定义Object对象为锁
    private Object lock = new Object();

    public MultiThreadSequence01() {
    }

    public static void main(String[] args) {

    }

    public void first(Runnable printFirst) throws InterruptedException {
        synchronized (lock) {
            // 如果flag不为0则让first线程等待，while循环控制first线程如果不满住条件就一直在while代码块中，
            // 防止出现中途跳入，执行下面的代码，其余线程while循环同理
            while (flag != 0) {
                lock.wait();
            }
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            // 定义成员变量为 1
            flag = 1;
            // 唤醒其余所有的线程
            lock.notifyAll();
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        synchronized (lock) {
            //如果成员变量不为1则让二号等待
            while (flag != 1) {
                lock.wait();
            }
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();
            //如果成员变量为 1 ，则代表first线程刚执行完，所以执行second，并且改变成员变量为 2
            flag = 2;
            //唤醒其余所有的线程
            lock.notifyAll();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        synchronized (lock) {
            //如果flag不等于2 则一直处于等待的状态
            while (flag != 2) {
                lock.wait();
            }
            // printThird.run() outputs "third". Do not change or remove this line.
            //如果成员变量为 2 ，则代表second线程刚执行完，所以执行third，并且改变成员变量为 0
            printThird.run();
            flag = 0;
            lock.notifyAll();
        }
    }
}
