package Java.concurrent;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ThreadPoolApp2 {

    public static void main(String[] args) {
        // 自定义构造线程池的方法...
        ExecutorService executorService =
                new ThreadPoolExecutor(
                        3,
                        5,
                        0L,TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(5),
                        new ThreadPoolExecutor.AbortPolicy()
                );
        IntStream.rangeClosed(1,11).forEach(i -> {
            // 你会发现抛出异常了，这实际上是因为我们设定了AbortPolicy
            // 提交的任务数量11 > 可以被接纳的最大数量5+5=10
            executorService.submit(() -> {
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               IntStream.rangeClosed(1,50)
                       .forEach(j -> System.out.println(
                               Thread.currentThread().getName()));
            });
        });
        executorService.shutdown();
    }
}
