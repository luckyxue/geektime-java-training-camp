package java0.conc0301.op;

public class Join {

    public static void main(String[] args) {
        Object oo = new Object();

        MyThread thread1 = new MyThread("thread1 -- ");
        thread1.setOo(oo);
        thread1.start();
        // main线程获取thread1对象上的锁
        synchronized (thread1) {
            for (int i = 0; i < 100; i++) {
                if (i == 20) {
                    try {
                        // main进入waiting状态，同时释放了thread1上面的锁
                        // 这里和JoinNotRelease例子中的不释放对象oo上面的锁不太一样
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " -- " + i);
            }
        }
    }

}

class MyThread extends Thread {

    private String name;
    private Object oo;

    public MyThread(String name) {
        this.name = name;
    }

    public void setOo(Object oo) {
        this.oo = oo;
    }

    @Override
    public void run() {
        // thread1线程只有获取到thread1对象本身上的锁才能继续执行
        // 这段代码能执行显然是获取到了thread1对象本身上的锁
        synchronized (this) {
            for (int i = 0; i < 100; i++) {
                System.out.println(name + i);
            }
        }
    }

}