package Java;

import sun.awt.windows.ThemeReader;

import java.util.stream.IntStream;

public class thread_API {

    public static void main(String[] args) {
        Thread t = Thread.currentThread();
        t.setName("Main");
        System.out.println("Main: "+t);
        NewThread_1 th1 = new NewThread_1();
        NewThread_2 th2 = new NewThread_2();
        // join()方法等待所有调用线程结束
        // 这样就能确保主线程是最后结束的
        try {
            th1.t1.join();
            th2.t2.join();
            for (int item : IntStream.rangeClosed(1,5).toArray()) {
                System.out.println("M: "+item);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Main Interrupted");
        }
    }
}

class NewThread_1 implements Runnable {
    Thread t1;
    NewThread_1() {
        t1 = new Thread(this);
        t1.setName("Ancillary");
        System.out.println("Ancillary: "+t1);
        t1.start();
    }
    public void run() {
        int s = 0;
        try {
            for (int item : IntStream.rangeClosed(1,5).toArray()) {
                s += item;
                Thread.sleep(500);
            }
            System.out.println("A1: "+s);
        } catch (InterruptedException e) {
            System.out.println("Ancillary Interrupted");
        }
    }
}

// 想让主线程是最后结束的，之前我们采用了调整sleeptime的方法
// 现在我们来尝试采用isAlive()和join()的API接口
// 我们一般很少会用到isAlive()，绝大多数的情况下会选择join()

class NewThread_2 implements Runnable{
    Thread t2;
    NewThread_2() {
        t2 = new Thread(this);
        t2.setName("Ancillary2");
        System.out.println("Ancillary2: "+t2);
        t2.start();
    }
    public void run() {
        int r = 10;
        try {
            for (int item : IntStream.rangeClosed(1,5).toArray()) {
                r += item;
                Thread.sleep(500);
            }
            System.out.println("A2: "+r);
        } catch (InterruptedException e) {
            System.out.println("Ancillary2 Interrupted");
        }
    }
}