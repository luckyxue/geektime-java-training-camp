package java0.conc0301.op;

public class JoinNotRelease {

    public static void main(String[] args) {
        Object oo = new Object();

        MyThreadDemo thread1 = new MyThreadDemo("thread1 -- ");
        thread1.setOo(oo);
        thread1.start();
        // 获取对象oo上面的锁
        synchronized (oo) {
            for (int i = 0; i < 100; i++) {
                if (i == 20) {
                    try {
                        // main线程进入waiting状态，但是不释放oo上面的锁
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

class MyThreadDemo extends Thread {

    private String name;
    private Object oo;

    public MyThreadDemo(String name) {
        this.name = name;
    }

    public void setOo(Object oo) {
        this.oo = oo;
    }

    @Override
    public void run() {
        // thread1获取不到对象oo上面的锁，所以没法执行后续语句
        synchronized (oo) {
            for (int i = 0; i < 100; i++) {
                System.out.println(name + i);
            }
        }
    }

}
