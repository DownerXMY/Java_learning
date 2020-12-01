package Java.concurrent;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public class CB {

    public static void main(String[] args) {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        // 注意和CountDownLatch的区别...

        IntStream.rangeClosed(1,3).forEach(i -> new Thread(() -> {
            try {
                Thread.sleep((long)Math.random()*2000);
                int randomInt = new Random().nextInt(500);
                System.out.println("Hello "+randomInt+"!");

                cyclicBarrier.await();
                // 等待当前的屏障之前集合了全部的三个线程
                // 如果三个线程都调用了await()方法，那么就一起冲破屏障

                System.out.println("World "+randomInt+"!");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start());

        // 实际上这段代码用CDL也是能够实现的，那么这两者的最大区别在哪里呢？
        // CyclicBarrier不是一次性的,计数器是可以重置的(自动重置),所以可以被重用...
        // 可以想象一个企业级流程可能需要多个屏障...

        CyclicBarrier cyclicBarrier1 = new CyclicBarrier(3,() -> {
            System.out.println("Barrier Action");
            // Barrier Action实际上是每次所有线程到达屏障所触发的行为...
        });
        for (int item=0;item<2;item++) {
            // 第一个for表示有多个屏障(多个执行阶段)
            IntStream.rangeClosed(1,3).forEach(i -> new Thread(() -> {
                try {
                    Thread.sleep((long)Math.random()*2000);
                    int randomInt = new Random().nextInt(500);
                    System.out.println("Hello "+randomInt+"!");

                    cyclicBarrier1.await();
                    // 等待当前的屏障之前集合了全部的三个线程
                    // 如果三个线程都调用了await()方法，那么就一起冲破屏障

                    System.out.println("World "+randomInt+"!");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start());
        }
    }
}
