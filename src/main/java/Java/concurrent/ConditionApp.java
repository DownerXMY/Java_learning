package Java.concurrent;

import java.util.ConcurrentModificationException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ConditionApp {
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private int count = 0;

    // await总是应给被放置在一个循环中...
    public void add() {
        try {
            lock.lock();
            while (count == 1) {
                try {
                    c2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count += 1;
            System.out.print(count);
            c1.signal();
        } finally {
            lock.unlock();
        }
    }

    public void minus() {
        try {
            lock.lock();
            while (count == 0) {
                try {
                    c1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count -= 1;
            System.out.print(count);
            c2.signal();
        } finally {
            lock.unlock();
        }
    }
    // 注意了，这里我们await和signal的Condition对象是不同的
    // 实际上这是Condition的一个相较于synchronized中的wait-notify的一个特性
    // 可以有多个waiting-list
    // 当然在这个例子中，我们很难看出这种机制的优势，因为wait的一直只有唯一的一个线程
    // 但是，如果有很多线程都在wait，那么他们会产生对锁的竞争
    // 这时候，如果我们只想然其中的部分参与竞争，就可以给他们划分一个独立的waiting-list

    public static void main(String[] args) {
        // 记得之前我们用wait-notify写了一个小程序
        // 接下去们用Lock以及Condition也来实现以下
        ConditionApp CA = new ConditionApp();
        Condition_thread Ct1 = new Condition_thread(CA,true);
        Condition_thread Ct2 = new Condition_thread(CA,false);
        new Thread(Ct1).start();
        new Thread(Ct2).start();
    }
}

class Condition_thread implements Runnable {
    public ConditionApp CA = new ConditionApp();
    boolean judge;
    Condition_thread(ConditionApp CA1,boolean jd) {
        this.CA = CA1;
        this.judge = jd;
    }
    public void run() {
        if (judge) {
            for (int item : IntStream.rangeClosed(1,30).toArray()) {
                try {
                    Thread.sleep((long)Math.random()*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CA.add();
            }
        } else {
            for (int item : IntStream.rangeClosed(1,30).toArray()) {
                try {
                    Thread.sleep((long)Math.random()*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CA.minus();
            }
        }
    }
}