package Java.concurrent;

public class DeadLock {
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public void method1() {
        synchronized (lock1) {
            synchronized (lock2) {
                System.out.println("method1 involved");
            }
        }
    }
    public void method2() {
        synchronized (lock2) {
            synchronized (lock1) {
                System.out.println("Method2 involved");
            }
        }
    }

    public static void main(String[] args) {
        DeadLock dl = new DeadLock();
        DL_thread f1 = new DL_thread(true,dl);
        DL_thread f2 = new DL_thread(false,dl);
        new Thread(f1).start();
        new Thread(f2).start();

        // 我们之前构造新线程的方式就是定义新的线程类实现Runnable接口或者继承Thread类
        // 然而，在JDK8引入了lambda表达式，让我们来试试看，取代原先构建线程的方式：
//        Runnable r1 = () -> {
//            while (true) {
//                dl.method1();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        Runnable r2 = () -> {
//            while (true) {
//                dl.method2();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        new Thread(r1).start();
//        new Thread(r2).start();
        // 甚至，我们可以用上面那个lambda表达式直接代替括号里的r1,
        // 即，new Thread(() -> {...}).start();
    }
}
class DL_thread implements Runnable {
    public boolean judge;
    private static boolean judge2 = true;
    public DeadLock DL;
    DL_thread(boolean jdg,DeadLock dl) {
        this.judge = jdg;
        this.DL = dl;
    }
    public void run() {
        if (judge) {
            while (judge2) {
                DL.method1();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            while (judge2) {
                DL.method2();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// 最终你会发现程序最终停下来的，这就是出现了死锁的情况
// 可以在终端用jps查看是否出现了死锁.