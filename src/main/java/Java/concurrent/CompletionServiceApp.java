package Java.concurrent;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class CompletionServiceApp {

    public static void main(String[] args) {
        ExecutorService executorService =
                new ThreadPoolExecutor(
                        4,
                        10,
                        10L,TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(20),
                        new ThreadPoolExecutor.AbortPolicy());

        CompletionService<Integer> completionService =
                new ExecutorCompletionService<>(executorService);
        // 相当于completionService.submit()的任务最重是由executorService执行的

        IntStream.rangeClosed(1,10).forEach(i -> {
            completionService.submit(() -> {
               Thread.sleep((long)(Math.random()*1000));
               System.out.println(Thread.currentThread().getName());
               return i*i;
            });
        });
        for (int item = 1; item <= 10; item++) {
            try {
                int result = completionService.take().get();
                // .take()实际上就是把completionQueue中的结果依次提取出来
                // 所以这样一来，打印的先后顺序就是线程池任务执行完毕的顺序
                // 注意如果只是.take()，获取到的将是一个Future对象
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        // 注意这一点经常容易被忽略...
    }
}