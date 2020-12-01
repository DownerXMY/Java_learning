package Java.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

public class ThreadPoolApp {
    public static void main(String[] args) {
        // 创建线程池有两种不同的方式，之前也已经都尝试过了:
        // Threadpool_examp.java & Threadpool_SWFD.java
        // 这里我们先采用线程工厂来创建线程池
        ExecutorService executorService =
                Executors.newFixedThreadPool(3);
        // 实际上你点进newFixedThreadPool里面去看，
        // 发现他的本质还是new ThreadPoolExecutor
        // 所以实际上，两种创建线程池的方法是没有本质区别的...

/*       Executors.newSingleThreadExecutor();
        --只会创建单一一个线程
        Executors.newCachedThreadPool();
       --线程池中的线程数量会根据提交的任务而自动变化
 */
        executorService.submit(() -> {
            IntStream.rangeClosed(1,50)
                    .forEach(i -> {
                        System.out.println(
                                Thread.currentThread().getName()
                        );
                    });
        });
        // 发现如果只提交一个任务，最终只有一个线程参与
        // 比如我们尝试向线程池提交多个任务
        IntStream.rangeClosed(1,9)
                .forEach(i -> executorService.submit(() -> {
                    IntStream.rangeClosed(1,50).forEach(j -> {
                        System.out.println(Thread
                                .currentThread()
                                .getName());
                    });
                }));
        // 这时候三个线程都会参与...
        executorService.shutdown();
        // 你会发现没有这句"显示关闭线程池"的调用，程序最终是不会正常退出的
        // 实际上这是因为我们利用线程池创建的线程实际上是用户线程，不是守护线程(后台运行的线程)

    }
}
