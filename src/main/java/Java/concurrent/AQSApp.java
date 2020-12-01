package Java.concurrent;

import java.util.concurrent.locks.*;
import java.util.stream.IntStream;

public class AQSApp {
    // ReentrantLock
    private Lock lock = new ReentrantLock();

    public void method() {
        lock.lock();
        try {
            Thread.sleep(1000);
            System.out.println("method");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // ReentrantReadWriteLock
    private ReadWriteLock lock1 = new ReentrantReadWriteLock();
    public void method1() {
        try {
            // 有趣的事情:
            // 你会发现这里写ReadLock,那么所有的结果几乎是同时被打印出来的
            // 但是换成WriteLock,则将会按照每隔1s这样的模式打印
            // 可以想想是为什么...
            lock1.readLock();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("method1");
        } finally {
            lock1.readLock().unlock();
        }
    }

    public static void main(String[] args) {
        AQSApp aqsApp = new AQSApp();
        IntStream.rangeClosed(1,3).forEach(i -> {
            new Thread(() -> {
                aqsApp.method();
            }).start();
        });
        IntStream.rangeClosed(1,3)
                .forEach(i -> new Thread(aqsApp::method1).start());
        // 这种双冒号的写法叫做"方法引用",也是JAVA8种新增的...
        // 也是属于Lambda的一种...
        // 一个很简单的例子是:List.forEach(System.out::println)
    }
}
