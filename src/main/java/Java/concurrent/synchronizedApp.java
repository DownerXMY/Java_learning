package Java.concurrent;

import java.util.concurrent.locks.*;

public class synchronizedApp {

    private static Object object = new Object();

    public static void method() {
        synchronized (object) {
            System.out.println("This is an object!");
        } // synchronized代码块可以应用到任何对象，不一定是object
    }

    public static void method2() {
        synchronized (object) {
            System.out.println("welcome...");
            throw new RuntimeException();
        }
    }

    public synchronized static void method3() {
        System.out.println("This is another method for synchronization...");
    }

    public static void main(String[] args) {
        C1 c1 = new C1();
        C1 c2 = new C1();
        T1 t1 = new T1(c1);
        T2 t2 = new T2(c1); // 这里用c1则是Hello先输出，用c2则是world先输出
        new Thread(t1).start();
        new Thread(t2).start();
        System.out.println("-------------------------------");
        method();
        method2();
        method3();
    }
}

class C1 {
    public synchronized void hello() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Hello,");
    }
    public synchronized void world() {
        System.out.println("world!");
    }
}

class T1 implements Runnable {
    private C1 c1;
    public T1(C1 c11) {
        this.c1 = c11;
    }
    public void run() {
        c1.hello();
    }
}

class T2 implements Runnable {
    private C1 c1;
    public T2(C1 c11) {
        this.c1 = c11;
    }
    public void run() {
        c1.world();
    }
}