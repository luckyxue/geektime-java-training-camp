package java0.conc0302.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 虽然创建的周期性线程池，没有指定延时时间就立即执行
 */
public class ExecutorServiceDemo {

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(16);
        try {
            String str = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "I am a task, which submitted by the so called boss, and run by those anonymous workers";
                }
            }).get();

            System.out.println("str=" + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }

}
