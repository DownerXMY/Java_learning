package Java.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class CDL {

    public static void main(String[] args) {
        // 这里的3实际上是一个计数器的值
        CountDownLatch countDownLatch = new CountDownLatch(3);
        // CountDownLatch是一次性的，一旦计数器归零，那就永远是0了...

        // 这里采用JAVA8中最新的方式创建三个线程
        IntStream.rangeClosed(1,3).forEach(i -> new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Hello");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 每一次调用countDown,计数器的值都会以原子的形式减1,一直到0为止.
                countDownLatch.countDown();
            }
        }).start());
        System.out.println("Child Start!");

        try {
            countDownLatch.await();
            // 检查计数器里面的值是否为0,如果是的话,await()方法就会立刻返回
            // 如果计数器里面的值不为0，则这个线程将会直接进入阻塞队列，等待计数器变成0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main Finish!");
    }
}
