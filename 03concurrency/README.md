# 作业与总结

---

## 作业一

### 作业要求

**2.（必做）思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？写出你的方法，越多越好，提交到 Github。**

### 做题思路

一共 10 种，大致如下：

- 不使用多线程并发工具：
  - NoLockMethod.java(使用循环不断判断)
  - ThreadJoinMethod.java(使用Thread Join)
- 使用多线程并发工具
  - 不使用 Future（使用类似等待-通知机制）
    - SynchronizedMethod.java
    - SemaphoreMethod.java
    - LockConditionMethod.java
    - CyclicBarrierMethod.java
    - CountDownLatchMethod.java
  - 使用 Future（使用线程池的 submit）
    - FutureMethod.java
    - FutureTaskMethod.java
    - CompletableFutureMethod.java

### 代码

代码也放到了当前文件的 code 文件夹工程中

NoLockMethod.java

```java
package homework;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 无锁方式
 * 主要是通过不断循环查询返回值是否为空，来判断值是否已经计算完成
 */
public class NoLockMethod {

    private volatile Integer value = null;

    public void sum(int num) {
        value = fibo(num);
    }

    private int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    public int getValue() {
        while (value == null) {
        }
        return value;
    }

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        final NoLockMethod method = new NoLockMethod();
        Thread thread = new Thread(() -> {
            method.sum(45);
        });
        thread.start();

        int result = method.getValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }
}
```

ThreadJoinMethod.java

```java
package homework;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * Thread join 方式
 */
public class ThreadJoinMethod {

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        AtomicInteger value = new AtomicInteger();
        Thread thread = new Thread(()-> {
            value.set(sum());
        });
        thread.start();
        thread.join();

        int result = value.get(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

    private static int sum() {
        return fibo(45);
    }

    private static int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

}
```

SynchronizedMethod.java

```java
package homework;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 通过的管程等待-通知机制，来获取值
 * synchronized方式
 */
public class SynchronizedMethod {

    private volatile Integer value = null;

    synchronized public void sum(int num) {
        value = fibo(num);
        notifyAll();
    }

    private int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    synchronized public int getValue() throws InterruptedException {
        while (value == null) {
            wait();
        }
        return value;
    }

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        final SynchronizedMethod method = new SynchronizedMethod();
        Thread thread = new Thread(() -> {
            method.sum(45);
        });
        thread.start();

        int result = method.getValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

}

```

SemaphoreMethod.java

```java
package homework;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * Semaphore方式
 */
public class SemaphoreMethod {

    private volatile Integer value = null;
    final Semaphore semaphore = new Semaphore(1);

    public void sum(int num) throws InterruptedException {
        semaphore.acquire();
        value = fibo(num);
        semaphore.release();
    }

    private int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    public int getValue() throws InterruptedException {
        int result;
        semaphore.acquire();
        result = this.value;
        semaphore.release();
        return result;
    }

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        final SemaphoreMethod method = new SemaphoreMethod();
        Thread thread = new Thread(() -> {
            try {
                method.sum(45);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        int result = method.getValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

}

```

LockConditionMethod.java

```java
package homework;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 管程Lock Condition方式
 */
public class LockConditionMethod {

    private volatile Integer value = null;
    private Lock lock = new ReentrantLock();
    private Condition calComplete = lock.newCondition();

    public void sum(int num) {
        lock.lock();
        try {
            value = fibo(num);
            calComplete.signal();
        } finally {
            lock.unlock();
        }
    }

    private int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    public int getValue() throws InterruptedException {
        lock.lock();
        try {
            while (value == null) {
                calComplete.await();
            }
        } finally {
            lock.unlock();
        }
        return value;
    }

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        final LockConditionMethod method = new LockConditionMethod();
        Thread thread = new Thread(() -> {
            method.sum(45);
        });
        thread.start();

        int result = method.getValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

}

```

CyclicBarrierMethod.java

```java
package homework;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * CyclicBarrierMethod 方式
 */
public class CyclicBarrierMethod {

    private volatile Integer value = null;
    CyclicBarrier barrier;

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public void sum(int num) throws BrokenBarrierException, InterruptedException {
        value = fibo(num);
        barrier.await();
    }

    private int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    public int getValue() throws InterruptedException {
        return value;
    }

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法


        final CyclicBarrierMethod method = new CyclicBarrierMethod();
        CyclicBarrier barrier = new CyclicBarrier(1, ()-> {
            int result = 0; //这是得到的返回值
            try {
                result = method.getValue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 确保  拿到result 并输出
            System.out.println("异步计算结果为："+result);

            System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        });
        method.setBarrier(barrier);

        Thread thread = new Thread(() -> {
            try {
                method.sum(45);
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        // 然后退出main线程
    }

}

```

CountDownLatchMethod.java

```java
package homework;

import java.util.concurrent.CountDownLatch;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * CountDownLatchMethod 方式
 */
public class CountDownLatchMethod {

    private volatile Integer value = null;
    private CountDownLatch latch;

    public void sum(int num) {
        value = fibo(num);
        latch.countDown();
    }

    private int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    public int getValue() throws InterruptedException {
        latch.await();
        return value;
    }

    /**
     * latch没有重置功能，用这个函数来传入新的
     * @param latch
     */
    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatchMethod method = new CountDownLatchMethod();
        method.setLatch(latch);
        Thread thread = new Thread(() -> {
            method.sum(45);
        });
        thread.start();

        int result = method.getValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

}

```

FutureMethod.java

```java
package homework;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * Future
 */
public class FutureMethod implements Callable<Long> {

    private long sum(int num) {
        return fibo(num);
    }

    private long fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    @Override
    public Long call() throws Exception {
        return sum(45);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Long> future = executor.submit(new FutureMethod());

        long result = future.get(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
        executor.shutdown();
    }


}

```

FutureTaskMethod.java

```java
package homework;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * FutureTask
 */
public class FutureTaskMethod {

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
            if ( a < 2) {
                return 1;
            }
            return fibo(a-1) + fibo(a-2);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        FutureTask<Integer> sum = new FutureTask<>(new Sum());
        FutureTask<Integer> get = new FutureTask<>(new Get(sum));

        Thread sumT = new Thread(sum);
        sumT.start();
        Thread getT = new Thread(get);
        getT.start();

        int result = get.get(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}

```

CompletableFutureMethod.java

```java
package homework;

import java.util.concurrent.CompletableFuture;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * CompletableFuture 方式
 */
public class CompletableFutureMethod {

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        int result = CompletableFuture.supplyAsync(()-> sum()).join();

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

    private static int sum() {
        return fibo(45);
    }

    private static int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }
}
```

## 作业二

### 作业要求

4.（必做）把多线程和并发相关知识带你梳理一遍，画一个脑图，截图上传到 Github 上。
可选工具：xmind，百度脑图，wps，MindManage 或其他。

### 总结

这个知识梳理基本就是下面的： Java 并发概览
脑图也放到里面了

---

# Java 并发概览

---

&ensp;&ensp;&ensp;&ensp;参考训练营老师的内容和下面三个，用自己的逻辑主线重新整理一遍知识点

- 《Java 并发编程实战》：代码的例子很多，偏实战，很好
- 《Java 并发编程的艺术》：有原理级别，有实战级别的，能读完收获不小，很好
- 《Java 并发编程实战》 极客时间 王宝令 真五星级专栏

&ensp;&ensp;&ensp;&ensp;并发相关知识如下：

![](https://github.com/lw1243925457/JAVA-000/blob/main/Week_04/multiThread.png)

## 并发理论基础

### 什么情况下需要注意并发问题

&ensp;&ensp;&ensp;&ensp;下面两个必要条件：当前变量有读有写；当前变量被多个线程访问

- 1.当前变量有读有写：只读的话数据，完全没有数据不一致问题，不用并发保护；只写，不使用的数据，留着干啥？
- 2.被多个线程访问：只有一个线程访问的话，没有数据不一致的问题

### 是什么导致了多线程的并发问题

&ensp;&ensp;&ensp;&ensp;下面的三个方面

#### 1.多级存储--可见性

&ensp;&ensp;&ensp;&ensp;在多核心 CPU 的环境下，每颗 CPU 有自己缓存，线程操作的是不同的 CPU 缓存，导致了数据不一致，也就是线程 A 和 B 对变量的操作相互对于两者都是不可见的。

![](https://static001.geekbang.org/resource/image/e2/ea/e2aa76928b2bc135e08e7590ca36e0ea.png)

&ensp;&ensp;&ensp;&ensp;在下面的示例代码中，我们使用两个线程对变量各自进行 10000 的累加，理应得到 20000 的数值，但很多情况下，都是小于这个数的，就是由于可见性的问题导致的。

```java
package temp;

public class VisiblenessTest {
    private long count = 0;

    private void add() {
        for (int i = 0; i < 10000; i++) {
            count += 1;
        }
    }

    public long getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        final VisiblenessTest visiblenessTest = new VisiblenessTest();

        Thread thread1 = new Thread(()->{
            visiblenessTest.add();
        });
        Thread thread2 = new Thread(()->{
            visiblenessTest.add();
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();


        System.out.println(visiblenessTest.getCount());
    }
}
```

&ensp;&ensp;&ensp;&ensp;**一个线程对共享变量的修改，另外一个线程能立即看到，称为可见性**

#### 2.CPU 分时复用--原子性

&ensp;&ensp;&ensp;&ensp;我们都知道 CPU 的时间切片，每个线程的执行时间都是不确定的，而 CPU 的指令和我们的程序指令之间还是有些差别的，如同下面这个简单的加一语句：

```java
number += 1
```

&ensp;&ensp;&ensp;&ensp;在直觉中我们觉得它是一步就能完成的，但在 CPU 中需要多条指令去完成，最少三条，大致如下：

- 指令 1：需要把变量 number 从内存加载到 CPU 的寄存器
- 指令 2：在寄存器中执行+1 操作
- 指令 3：将结果写入内存

&ensp;&ensp;&ensp;&ensp;一个+1 操作分为三个操作，加上 CPU 的切换，可能带来我们不想要的结果，如下面的例子：我们两个线程都执行+1 操作，希望得到的是 2，却得到了 1

![](https://static001.geekbang.org/resource/image/33/63/33777c468872cb9a99b3cdc1ff597063.png)

&ensp;&ensp;&ensp;&ensp;**一个或者多个操作在 CPU 执行的过程中不被中断的特性称为原子性**

#### 3.编译程序优化指令执行次序--有序性

&ensp;&ensp;&ensp;&ensp;在代码中我们写入的语句可能如下：

```java
int a = 7;
int b = 7;
```

&ensp;&ensp;&ensp;&ensp;但编译器在优化后可能变成了：

```java
int b = 7;
int a = 7;
```

&ensp;&ensp;&ensp;&ensp;顺序的不确定性可能会带个我们不可预知的错误，比如经典的双重检查创建单例对象：

```java

public class Singleton {
  static Singleton instance;
  static Singleton getInstance(){
    if (instance == null) {
      synchronized(Singleton.class) {
        if (instance == null)
          instance = new Singleton();
        }
    }
    return instance;
  }
}
```

&ensp;&ensp;&ensp;&ensp;加入线程 AB 同时执行，A 先得到了锁，B 则进入阻塞。A 创建成功以后，B 唤醒，检查 instance 不为 null，直接返回，不再创建对象。看起来一切完美，但还是会出现问题，而问题出在操作 new 上，直觉上 new 的操作为：

- 1.分配一块内存
- 2.在内存 M 上初始化 Singleton 对象
- 3.将 M 的地址赋给 instance 变量

&ensp;&ensp;&ensp;&ensp;但编译器实际优化后是这样：

- 1.分配一块内存
- 2.将 M 的地址赋给 instance 变量
- 3.在内存 M 上初始化 Singleton 对象

&ensp;&ensp;&ensp;&ensp;当线程 A 在第二步的时候发生了线程切换，并切到了 B 上，B 判断 instance 已经初始化了，直接返回，但如果其他线程访问 instance 变量的话，因为对象并没有初始化，就会出现空指针异常。如下图所示：

![](https://static001.geekbang.org/resource/image/64/d8/64c955c65010aae3902ec918412827d8.png)

### Java 如何解决多线程的并发问题

#### 解决可见性和有序性问题

&ensp;&ensp;&ensp;&ensp;可见性的问题是各个线程数据写入了各自的缓存中，直观的解决办法是禁止使用缓存，全部写到内存中

&ensp;&ensp;&ensp;&ensp;有序性的问题是编译优化的指令重排序，直观的解决办法是禁止编译器优化

&ensp;&ensp;&ensp;&ensp;但缓存和编译优化的目的都是为了提升程序的性能的，粗暴的全部禁用掉，那性能可能就堪忧了

&ensp;&ensp;&ensp;&ensp;那合理的方案就是合理禁用缓存和编译优化了，也可以说是局部禁用缓存和编译优化

&ensp;&ensp;&ensp;&ensp;在 Java 中提供了程序员解决这两方面的问题的方法，这些方法包括 volatile、synchronized 和 final 三个关键字，以及六项 Happens-Before 规则

- Happens-Before 规则：这个可以说是底层规则了，保证了程序的有效性和可见性，具体如下：
  - 1.程序的顺序性：程序代码执行顺序
  - 2.volatile 变量规则：变量写先与读
  - 3.传递性：A 先 B，B 先 C，则 A 先 C
  - 4.管程锁定规则：解锁后序与加锁
  - 5.线程启动规则：start 先与子线程后续操作
  - 6.线程终止规则：子线程中的操作先于 join
  - 7.线程中断规则：对线程 interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生，可以通过 Thread.interrupted()方法检测到是否有中断发生
  - 8.对象终结规则：一个对象的初始化完成(构造函数执行结束)先行发生于它的 finalize()方法的开始
- Volatile：禁用缓存和变量优化
- synchronized：Happens-Before 保证了锁操作相关的可见性和有序性
- final：表示变量只读了，那还需要考虑啥可见性和有序性，告诉编译器随便整了

#### 原子性

&ensp;&ensp;&ensp;&ensp;原子性的问题是在当前线程还没有完全执行完当前变量的一套操作的时候，发生了线程切换，而且其他的线程对当前变量也有操作，导致了不可预知的错误。

&ensp;&ensp;&ensp;&ensp;解决的办法就是在当前变量发送操作的时刻，只能有一个线程能进行操作,发生了线程切换那就等待到下一个时间切片，在这期间，不允许其他线程进行操作。王宝令老师专栏的描述是下面这样的，意思应该差不多：

> “同一时刻只有一个线程执行”这个条件非常重要，我们称之为互斥。如果我们能够保证对共享变量的修改是互斥的，那么，无论是单核 CPU 还是多核 CPU，就都能保证原子性了。

&ensp;&ensp;&ensp;&ensp;Java 里面锁相关的大致是 synchronized 之类的了，这里不进行详细说明了，简单提一下。

### 并发锁的那些事

#### 锁的用法

&ensp;&ensp;&ensp;&ensp;锁的基本使用步骤如下：有点像蹲坑，一个坑只能一个人用，后面的人需要等前面的人用完了才能用......

- 1.声明锁：锁可以使用现有的对象，也可以新建；在 Java 中锁有级别，如实例级、对象级等
- 2.加锁：加锁，第一个访问的可以获得锁，后面锁上门，后面的就能不能进入
- 3.访问保证资源：受保护的资源可以是一个也可以是多个，需要注意资源的封装了，集中式的保护起来更容易
- 4.释放锁：访问问以后就开门，让后面人进入

&ensp;&ensp;&ensp;&ensp;大致模型如下：

![](https://static001.geekbang.org/resource/image/28/2f/287008c8137a43fa032e68a0c23c172f.png)

&ensp;&ensp;&ensp;&ensp;在用锁的时候需要注意锁与保护资源之间的关系

&ensp;&ensp;&ensp;&ensp;可以是一把锁保护多个资源，也就是 1：N，如下图，没有并发问题

![](https://static001.geekbang.org/resource/image/26/f6/26a84ffe2b4a6ae67c8093d29473e1f6.png)

&ensp;&ensp;&ensp;&ensp;但不能是多把锁保护一个或者多个资源，也就是相当于一个锁发了多个钥匙，可以多个人打开锁进去了，如下图，没有保护作用，有并发问题

![](https://static001.geekbang.org/resource/image/60/be/60551e006fca96f581f3dc25424226be.png)

&ensp;&ensp;&ensp;&ensp;用锁的时候注意对受保护对象进行精细化关联，使用细粒度锁，这样能提高程序性能

&ensp;&ensp;&ensp;&ensp;此外需要注意一个锁保护多个资源时，资源释放相互有关联，有关联的话就需要用粒度比较大的锁。这里不再详细的赘述，可以参考下面两篇文章：

- [03 | 互斥锁（上）：解决原子性问题](https://time.geekbang.org/column/article/84344)
- [04 | 互斥锁（下）：如何用一把锁保护多个资源？](https://time.geekbang.org/column/article/84601)

#### 使用锁过程中可能导致的问题

&ensp;&ensp;&ensp;&ensp;虽然使用锁的好处有很多，但万事万物都是两面性的，锁的使用不当，容易发生下面这些问题：死锁、活锁、饥饿、性能问题

##### 死锁

&ensp;&ensp;&ensp;&ensp;死锁的比较专业的定义如下：

> 一组互相竞争资源的线程因相互等待，导致“永久”阻塞的现象

&ensp;&ensp;&ensp;&ensp;下面的死锁的示例代码，锁 AB 是两个线程所需要的，但刚开始彼此各获得了 A 和 B，线程 1 等待 B，线程 2 等待 A，但没有完成操作两个线程就不会释放锁，他们之间就会这样一直等待下去。

```java
package com.company;

public class DeadLockSample extends Thread {
    private String first;
    private String second;

    public DeadLockSample(String name, String first, String second) {
        super(name);
        this.first = first;
        this.second = second;
    }

    @Override
    public void run() {
        synchronized (first) {
            System.out.println(this.getName() + " get lock: " + first);
            try {
                Thread.sleep(1000);
                synchronized (second) {
                    System.out.println(this.getName() + " get lock: " + second);
                }
            } catch (InterruptedException e) {

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String lockA = "LockA";
        String lockB = "LockB";

        DeadLockSample t1 = new DeadLockSample("Thread1", lockA, lockB);
        DeadLockSample t2 = new DeadLockSample("Thread2", lockB, lockA);

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
```

&ensp;&ensp;&ensp;&ensp;那应该如何预防死锁？死锁必须具备下面四个条件才会发生：

- 1.互斥：共享资源 X 和 Y 只能被一个线程占用
- 2.占有且等待：线程 T1 或者资源 X，在等待 Y 的时候，不释放共享资源 X
- 3.不可强占：其他线程不能强占 T1 占用的资源
- 4.循环等待：T1 等待 T2 的资源，T2 等待 T1 的资源

&ensp;&ensp;&ensp;&ensp;第一个互斥没有办法，毕竟加锁互斥是基础，但对于其他的还是有办法的：

- 占用且等待：一次性申请所有的资源
- 不可强占：占用部分资源的线程进一步申请其他资源的时候，如果申请不到，可以主动释放它占有的资源
- 循环等待：按序申请资源

##### 活锁

&ensp;&ensp;&ensp;&ensp;在上面死锁的不可占用解决方案中：

- 不可强占：占用部分资源的线程进一步申请其他资源的时候，如果申请不到，可以主动释放它占有的资源

&ensp;&ensp;&ensp;&ensp;存在一种情况：两个线程几乎同时获得锁和释放锁，并一直循环，虽然没有阻塞，但程序还是执行不下去，这种情况就叫活锁。

&ensp;&ensp;&ensp;&ensp;就如同两个人在路口相遇，两个人同时想要对方，于是一起向右，一起向左，向右、向左......

&ensp;&ensp;&ensp;&ensp;解决的办法就是随机的等待一个时间再去获取锁

&ensp;&ensp;&ensp;&ensp;如同我先不同，看你往左了，我就往右就行了

##### 饥饿

> 所谓“饥饿”指的是线程因无法访问所需资源而无法执行下去的情况。

&ensp;&ensp;&ensp;&ensp;在 CPU 繁忙的情况下，优先级低的线程得到执行的机会很小，就可能发生饥饿；持有锁的线程，如果执行的世界过长，也可能导致饥饿

&ensp;&ensp;&ensp;&ensp;饥饿有三种解决方案：

- 1.保证资源充足：计算机就那么点有限资源，所有适用的场景有限
- 2.公平的分配资源：此方案 场景较多，在并发编程中，主要是使用公平锁（一种先来后到的分配方案）
- 3.避免持有锁的线程长时间执行：计算机就那么点有限资源，所有适用的场景有限

##### 性能问题

&ensp;&ensp;&ensp;&ensp;加锁就意味着只能一个线程进行访问，而这部分代码一个一个的线程访问就相当于串行化了。所有如果锁的影响区域过大，那就不能发挥成多线程的优势了，甚至可以因为多线程的上下文切换而导致多线程程序性能还不如单线程程序

&ensp;&ensp;&ensp;&ensp;避免性能问题，有下面两个方案：

&ensp;&ensp;&ensp;&ensp;一、不战而屈人之兵，方是上上策，所以最好的方案就是使用无锁的算法和数据结构，相关技术如下：

- 线程本地存储（Thread Local Storage， STL）
- 写时复制（Copy-on-write）
- 乐观锁
- Java 并发包中的无锁的数据结构，Atomic 之类的，使用 CAS 技术
- Disruptor：无锁的内存队列

### 一些种类锁说明

#### 可重入锁

> 可重入锁：顾名思义，指线程可以重复获取同一把锁

&ensp;&ensp;&ensp;&ensp;如下面的示例代码：在 addOne 函数中获得了锁，进入 get 函数后，如果不是可重入锁，那就会发送阻塞；如果是可重入锁，则获取成功，继续执行

```java

class X {
  private final Lock rtl =
  new ReentrantLock();
  int value;
  public int get() {
    // 获取锁
    rtl.lock();         ②
    try {
      return value;
    } finally {
      // 保证锁能释放
      rtl.unlock();
    }
  }
  public void addOne() {
    // 获取锁
    rtl.lock();
    try {
      value = 1 + get(); ①
    } finally {
      // 保证锁能释放
      rtl.unlock();
    }
  }
}
```

#### 读写锁

&ensp;&ensp;&ensp;&ensp;读写锁遵守下面三条原则：

- 1.允许多个线程同时读共享变量
- 2.只允许一个线程写共享变量
- 3.如果一个写线程正在执行写操作，此时禁止读线程共享变量

## Java 并发工具

### [Java 等待通知机制--synchronized、wait、notify](https://time.geekbang.org/column/article/85241)

&ensp;&ensp;&ensp;&ensp;synchronnized 的代码块使用、函数使用等使用比较简单，与其配合的还有 wait 和 notify，可以实现一个等待-通知机制。

&ensp;&ensp;&ensp;&ensp;如下面的示例程序，apply 函数一次性申请所有的锁资源，但申请不到的时候进入等待状态。当锁释放以后，再次唤醒运行

```java
package temp;

import java.util.ArrayList;
import java.util.List;

public class NotifySynchronized {
    private List<Integer> als = new ArrayList<>();

    synchronized void apply(Integer lock1, Integer lock2) {
        while (als.contains(lock1) || als.contains(lock2)) {
            try {
                System.out.println("无法一次性申请所有资源，进入等待");
                wait();
            } catch (Exception e) {

            }
        }
        System.out.println("资源申请成功");
        als.add(lock1);
        als.add(lock2);
    }

    synchronized void free(Integer lock1, Integer lock2) {
        als.remove(lock1);
        als.remove(lock2);
        System.out.println("资源释放成功");
        notifyAll();
    }

    public static void main(String[] args) throws InterruptedException {
        final Integer lock1 = 1;
        final Integer lock2 = 2;

        final NotifySynchronized example = new NotifySynchronized();
        example.apply(lock1, lock2);

        Thread thread = new Thread(()->{
            example.apply(lock1, lock2);
        });
        thread.start();

        Thread.sleep(10000);
        example.free(lock1, lock2);

        thread.join();
    }
}
```

&ensp;&ensp;&ensp;&ensp;这里涉及到的一些知识点稍微提及下：

&ensp;&ensp;&ensp;&ensp;尽量使用 notifyAll()，notify() 是会随机地通知等待队列中的一个线程，而 notifyAll() 会通知等待队列中的所有线程。

&ensp;&ensp;&ensp;&ensp;wait() 方法和 sleep() 方法都能让当前线程挂起一段时间，那它们的区别是什么？

- wait 会释放所有锁而 sleep 不会释放锁资源.
- wait 只能在同步方法和同步块中使用，而 sleep 任何地方都可以.
- wait 无需捕捉异常，而 sleep 需要.

&ensp;&ensp;&ensp;&ensp;Java 采用的是管程技术，synchronized 关键字及 wait()、notify()、notifyAll() 这三个方法都是管程的组成部分，相关原理请查看链接：[08 | 管程：并发编程的万能钥匙](https://time.geekbang.org/column/article/86089)

### Lock 和 Condition

- [14 | Lock 和 Condition（上）：隐藏在并发包中的管程](https://time.geekbang.org/column/article/87779)
- [15 | Lock 和 Condition（下）：Dubbo 如何用管程实现异步转同步？](https://time.geekbang.org/column/article/88487)

&ensp;&ensp;&ensp;&ensp;在前面的 synchronized 中，它虽然有等待-通知机制，但还是不够灵活。Lock 和 Condition 相当于它的灵活变种，尤其在解决死锁问题上，能很好的破坏不可强占条件。

&ensp;&ensp;&ensp;&ensp;Lock 和 Condition 相比较 synchronized 多了下面三个：

- 1.能够响应中断
- 2.支持超时
- 3.非阻塞的获取锁

&ensp;&ensp;&ensp;&ensp;响应的 API 如下：

```java
// 支持中断的API
void lockInterruptibly() throws InterruptedException;
// 支持超时的API
boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
// 支持非阻塞获取锁的API
boolean tryLock();
```

&ensp;&ensp;&ensp;&ensp;Lock 主要是用于互斥，简单使用的示例代码如下：

```java

class X {
  private final Lock rtl =
  new ReentrantLock();
  int value;
  public void addOne() {
    // 获取锁
    rtl.lock();
    try {
      value+=1;
    } finally {
      // 保证锁能释放
      rtl.unlock();
    }
  }
}
```

&ensp;&ensp;&ensp;&ensp;Condition 用于同步，使用示例如下：

```java

public class BlockedQueue<T>{
  final Lock lock =
    new ReentrantLock();
  // 条件变量：队列不满
  final Condition notFull =
    lock.newCondition();
  // 条件变量：队列不空
  final Condition notEmpty =
    lock.newCondition();

  // 入队
  void enq(T x) {
    lock.lock();
    try {
      while (队列已满){
        // 等待队列不满
        notFull.await();
      }
      // 省略入队操作...
      //入队后,通知可出队
      notEmpty.signal();
    }finally {
      lock.unlock();
    }
  }
  // 出队
  void deq(){
    lock.lock();
    try {
      while (队列已空){
        // 等待队列不空
        notEmpty.await();
      }
      // 省略出队操作...
      //出队后，通知可入队
      notFull.signal();
    }finally {
      lock.unlock();
    }
  }
}
```

### 信号量--Semaphore

- [16 | Semaphore：如何快速实现一个限流器？](https://time.geekbang.org/column/article/88499)

&ensp;&ensp;&ensp;&ensp;信号量的一个方便使用场景是：限流器，能运行特定多个线程访问保护资源

&ensp;&ensp;&ensp;&ensp;信号量模型可以简单概括为：一个计数器、一个等待队列、三个方法，计数器和等待队列是私有的，通过调用信号量模型提供的三个方法来访问他们。

&ensp;&ensp;&ensp;&ensp;这三个方法分别是：init、down、up

- init()：设置计数器的初始值
- down（）：计数器的值减一；如果小于 0 则阻塞
- up（）：计数器加 1；如果计数器的值小于或等于 0，唤醒等待队列中的一个线程，将其从等待队列中移除

&ensp;&ensp;&ensp;&ensp;模型大致如下：

![](https://static001.geekbang.org/resource/image/6d/5c/6dfeeb9180ff3e038478f2a7dccc9b5c.png)

&ensp;&ensp;&ensp;&ensp;简单使用的示例如下：

```java

class ObjPool<T, R> {
  final List<T> pool;
  // 用信号量实现限流器
  final Semaphore sem;
  // 构造函数
  ObjPool(int size, T t){
    pool = new Vector<T>(){};
    for(int i=0; i<size; i++){
      pool.add(t);
    }
    sem = new Semaphore(size);
  }
  // 利用对象池的对象，调用func
  R exec(Function<T,R> func) {
    T t = null;
    sem.acquire();
    try {
      t = pool.remove(0);
      return func.apply(t);
    } finally {
      pool.add(t);
      sem.release();
    }
  }
}
// 创建对象池
ObjPool<Long, String> pool =
  new ObjPool<Long, String>(10, 2);
// 通过对象池获取t，之后执行
pool.exec(t -> {
    System.out.println(t);
    return t.toString();
});
```

#### 读写锁--ReadWriteLock

- [17 | ReadWriteLock：如何快速实现一个完备的缓存？](https://time.geekbang.org/column/article/88909)

&ensp;&ensp;&ensp;&ensp;使用于读多写少的场景，使用示例如下，实现一个缓存工具类：

```java

class Cache<K,V> {
  final Map<K, V> m =
    new HashMap<>();
  final ReadWriteLock rwl =
    new ReentrantReadWriteLock();
  // 读锁
  final Lock r = rwl.readLock();
  // 写锁
  final Lock w = rwl.writeLock();
  // 读缓存
  V get(K key) {
    r.lock();
    try { return m.get(key); }
    finally { r.unlock(); }
  }
  // 写缓存
  V put(K key, V value) {
    w.lock();
    try { return m.put(key, v); }
    finally { w.unlock(); }
  }
}
```

#### StampedLock

- [18 | StampedLock：有没有比读写锁更快的锁？](https://time.geekbang.org/column/article/89456)

&ensp;&ensp;&ensp;&ensp;比 ReadWriteLock 更激进的锁，在特定条件下相对也更快，性能更好

&ensp;&ensp;&ensp;&ensp;StampedLock 的乐观读，允许一个线程获取写锁，也就是说不是所有的写操作都被阻塞

&ensp;&ensp;&ensp;&ensp;StampedLock 使用是有特定场景的，读多写少，对一致性延迟有容忍。需要注意它不是可重入锁。使用 StampedLock 一定不要调用中断操作，如果需要支持中断功能，一定使用可中断的悲观读锁 readLockInterruptibly() 和写锁 writeLockInterruptibly()

&ensp;&ensp;&ensp;&ensp;示例如下：

```java

class Point {
  private int x, y;
  final StampedLock sl =
    new StampedLock();
  //计算到原点的距离
  int distanceFromOrigin() {
    // 乐观读
    long stamp =
      sl.tryOptimisticRead();
    // 读入局部变量，
    // 读的过程数据可能被修改
    int curX = x, curY = y;
    //判断执行读操作期间，
    //是否存在写操作，如果存在，
    //则sl.validate返回false
    if (!sl.validate(stamp)){
      // 升级为悲观读锁
      stamp = sl.readLock();
      try {
        curX = x;
        curY = y;
      } finally {
        //释放悲观读锁
        sl.unlockRead(stamp);
      }
    }
    return Math.sqrt(
      curX * curX + curY * curY);
  }
}
```

#### 用于拓扑序列类型的多线程工具--CountDownLatch 和 CyclicBarrier

- [19 | CountDownLatch 和 CyclicBarrier：如何让多线程步调一致？](https://time.geekbang.org/column/article/89461)

&ensp;&ensp;&ensp;&ensp;有时候我们线程不是各自运行的，他们之间有一定的约束和步骤，比如线程 3 需要线程 1 和 2 完成后才执行之类的，有点类似于拓扑序列，涉及到这部分，可以使用 CountDownLatch 和 CyclicBarrier,这里就再详细写了，自行查看链接学习吧

&ensp;&ensp;&ensp;&ensp;复杂的拓扑序列需要使用 FutureTask 之类的

#### 无锁类数据结构

- [21 | 原子类：无锁工具类的典范](https://time.geekbang.org/column/article/90515)

&ensp;&ensp;&ensp;&ensp;大致就是 Java 并发包中的那些 Atomic 之类的，使用的 CAS 原理

#### 并发容器

- [20 | 并发容器：都有哪些“坑”需要我们填？](https://time.geekbang.org/column/article/90201)
- 《Java 并发编程实战》 第五章 基础构建模块
- 《Java 并发编程的艺术》 第六、七、八章

## 其他

- [Java 多线程概览](https://github.com/lw1243925457/SE-Notes/blob/master/profession/program/java/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/Java%20%E5%A4%9A%E7%BA%BF%E7%A8%8B%E6%A6%82%E8%A7%88.md)

  - [09 | Java 线程（上）：Java 线程的生命周期](https://time.geekbang.org/column/article/86366)
  - [10 | Java 线程（中）：创建多少线程才是合适的？](https://time.geekbang.org/column/article/86666)
  - [11 | Java 线程（下）：为什么局部变量是线程安全的？](https://time.geekbang.org/column/article/86695)
  - [22 | Executor 与线程池：如何创建正确的线程池？](https://time.geekbang.org/column/article/90771)
  - [23 | Future：如何用多线程实现最优的“烧水泡茶”程序？](https://time.geekbang.org/column/article/91292)
  - 《Java 并发编程实战》 第一章 多线程的优点与缺点
  - 《Java 并发编程实战》 第六章 任务执行
  - 《Java 并发编程实战》 第七章 取消与关闭
  - 《Java 并发编程实战》 第八章 线程池的使用
  - 《Java 并发编程实战》 第十一章 性能与可伸缩性
  - 《Java 并发编程实战》 第十二章 并发程序的测试
  - 《Java 并发编程的艺术》 第四章 Java 并发编程基础
  - 《Java 并发编程的艺术》 第 9、10 章

- [Java 多线程编程最佳实践](https://github.com/lw1243925457/SE-Notes/blob/master/profession/program/java/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/Java%E5%A4%9A%E7%BA%BF%E7%A8%8B%E7%BC%96%E7%A8%8B%E6%9C%80%E4%BD%B3%E5%AE%9E%E8%B7%B5.md)

  - [12 | 如何用面向对象思想写好并发程序？](https://time.geekbang.org/column/article/87365)
  - 并发设计模式:Java 并发编程实战的第三部分；比较好的编写设计原则，相关的设计模式讲解
  - 《Java 并发编程实战》 第三章 对象的共享
  - 《Java 并发编程实战》 第四章 对象的组合
  - 《Java 并发编程实战》 第十章 避免活跃性危险
  - 《Java 并发编程实战》 第十一章 性能与可伸缩性

- CAS：《Java 并发编程实战》 第十二章 原子变量与非阻塞同步机制

> 用锁的最佳实践你已经知道，用锁虽然能解决很多并发问题，但是风险也是挺高的。可能会导致死锁，也可能影响性能。这方面有是否有相关的最佳实践呢？有，还很多。但是我觉得最值得推荐的是并发大师 Doug Lea《Java 并发编程：设计原则与模式》一书中，推荐的三个用锁的最佳实践，它们分别是：
>
> 永远只在更新对象的成员变量时加锁
>
> 永远只在访问可变的成员变量时加锁
>
> 永远不在调用其他对象的方法时加锁
>
> 这三条规则，前两条估计你一定会认同，最后一条你可能会觉得过于严苛。但是我还是倾向于你去遵守，因为调用其他对象的方法，实在是太不安全了，也许“其他”方法里面有线程 sleep() 的调用，也可能会有奇慢无比的 I/O 操作，这些都会严重影响性能。更可怕的是，“其他”类的方法可能也会加锁，然后双重加锁就可能导致死锁。并发问题，本来就难以诊断，所以你一定要让你的代码尽量安全，尽量简单，哪怕有一点可能会出问题，都要努力避免。
>
> 除了并发大师 Doug Lea 推荐的三个最佳实践外，你也可以参考一些诸如：减少锁的持有时间、减小锁的粒度等业界广为人知的规则，其实本质上它们都是相通的，不过是在该加锁的地方加锁而已。你可以自己体会，自己总结，最终总结出自己的一套最佳实践来。

## 参考资料

- 《Java 并发编程实战》
- 《Java 并发编程的艺术》
- 《Java 并发编程实战》 极客时间 王宝令 真五星级专栏
