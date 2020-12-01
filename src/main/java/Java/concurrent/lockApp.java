package Java.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

public class lockApp {
    private Lock lock = new ReentrantLock();

    public void method1() {
        try {
            lock.lock();
            System.out.println("method1 involve");
        } finally {
//            lock.unlock();
        }
    }

    public void method2() {
        try {
            lock.lock();
            System.out.println("method2 involve");
        } finally {
            lock.unlock();
        }
    }

    public void method3() throws InterruptedException {
        if (lock.tryLock(800, TimeUnit.MILLISECONDS)) {
            System.out.println("method3 involve");
        } else {
            System.out.println("cannot get the lock");
        }
    }
    public static void main(String[] args) {
        lockApp la = new lockApp();
        // 使用lambda表达式创建新的线程
        Thread t1 = new Thread(() -> {
            for(int i=0;i<10;i++) {
                la.method1();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i : IntStream.rangeClosed(1,10).toArray()) {
                la.method2();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t3 = new Thread(() -> {
            for (int item : IntStream.rangeClosed(1,10).toArray()) {
                try {
                    la.method3();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
//        t2.start();
        t3.start();
    }
}
