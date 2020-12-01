package Java;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class future_pooling {
    // 我们可能会遇到这样的问题:
    // 要采用多线程去解决一个问题，但是过程中必须依赖上一步的结果来执行下一部
    // 这种问题常被称为"异步处理问题"
    public static void main(String[] args) {
        ExecutorService threadpool1 = Executors.newCachedThreadPool();
        // 这是一种比较简单的获取内置线程池的方式
//        threadpool1.submit(new call(10,20));
        // 以上这一步成为"提交任务"，但是为了处理异步计算，我们可以这么写:
        Future<Integer> future1 =
                threadpool1.submit(new call(10,20));
        // 一下是一些简单的API
        boolean judge1 = future1.isDone();
        System.out.println("first-time judge of DONE: "+judge1);
        boolean judge2 = future1.isCancelled();
        System.out.println("first-time judge of CANCELED: "+judge2);
        try {
            Integer result = future1.get();
            // 无限期等待，直到任务完成
            System.out.println("The computation result: "+result);
        } catch (Exception e) {
            System.out.println("Interrupted!");
        }
        boolean judge11 = future1.isDone();
        System.out.println("second-time judge of DONE: "+judge11);
        boolean judge22 = future1.isCancelled();
        System.out.println("second-time judge of CANCELED: "+judge22);
    }
}

class call implements Callable<Integer> {
    private int a1;
    private int a2;
    call(int a_1, int a_2) {
        this.a1 = a_1;
        this.a2 = a_2;
    }
    public Integer call() {
        String name = Thread.currentThread().getName();
        System.out.println("preparing for computation...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("interrupted!");
        }
        System.out.println("computation finished!");
        return a1 + a2;
    }
}