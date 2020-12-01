package Java;

import java.util.stream.IntStream;

public class Thread_prio {

    public static void main(String[] args) {
        // 优先级别高的线程往往能占用更多的CPU时间
        // 甚至是，即使是低优先级的线程正在运行，高优先级的线程仍然可以抢占CPU的资源

        Thread t = Thread.currentThread();
        t.setPriority(Thread.NORM_PRIORITY);
        t.setName("Main");
        NewThread_3 th1 = new NewThread_3(Thread.NORM_PRIORITY+2);
        NewThread_3 th2 = new NewThread_3(Thread.NORM_PRIORITY-2);
        th1.start();
        th2.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Main Interrupted!");
        }
        th1.stop();
        th2.stop();
        try {
            th1.t3.join();
            th2.t3.join();
        } catch (InterruptedException e) {
            System.out.println("Exception: "+e);
        }
        System.out.println("High prio"+" "+th1.num);
        System.out.println("Low prio"+" "+th2.num);
    }
}

class NewThread_3 implements Runnable {
    Thread t3;
    private volatile boolean running = true;
    // volatile确保running在每次被运行前都会被验证
    int num = 0;
    NewThread_3(int p) {
        t3 = new Thread(this);
        t3.setPriority(p);
        t3.setName("Ancillary3");
        System.out.println("Ancillary3: "+t3);
    }
    public void run() {
        while (running) {
            num++;
        }
    }
    public void start() {
        t3.start();
    }
    public void stop() {
        running = false;
    }
}

// 结果很接近，大概是因为我们的系统是无优先级的系统吧
// 运行于有优先级的系统，输出的结果将会接近:
// High prio: 589626904
// Low prio: 4408112
