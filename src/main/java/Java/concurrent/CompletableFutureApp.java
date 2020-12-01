package Java.concurrent;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureApp {

    public static void main(String[] args) {
        String result = CompletableFuture
                .supplyAsync(() -> "Hello") //异步执行1
                // 注意supplyAsync是返回结果的，
                // 而还有一个是runAsync，里面接受的是一个Runnable
                // 所以runAsync是不返回结果的...
                .thenApplyAsync(value -> value+" world") // 异步执行2
                // 因为ThenApplyAsync返回的还是一个CompletableFuture,所以需要join()
                .join(); // returns the result value when complete
        System.out.println(result);
        System.out.println("-----------------------------");

        CompletableFuture
                .supplyAsync(() -> "Hello")
                .thenApplyAsync(value -> value+" world")
                .thenAccept(value -> System.out.println(value+"..."));
        System.out.println("-----------------------------");

        String result1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "world";
        }),(x,y) -> x+ " " +y).join();
        System.out.println(result1);
        System.out.println("-----------------------------");

        // 最后我们看看CompletableFuture怎么解决Future的缺点
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task finished");
        });

        completableFuture.whenComplete((t,action) -> System.out.println("Finish"));
        /* 你可以把.whenComplete看做之前的.get(),
           但是有两点我们必须说明：
               首先这个方法不是阻塞方法，即使Future异步线程没有完成任务，也不会阻止主线程继续执行
               但是，一旦异步线程完成任务，他就会立刻"回调"，继续执行下面的代码
         */
        System.out.println("Done!"); // 这句话先打印出来，表示主线程没有被阻塞
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
