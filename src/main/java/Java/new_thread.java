package Java;

import java.util.stream.IntStream;

public class new_thread {

    public static void main(String[] args) {
        // JAVA提供了两种方式来创建进程
        // 实现Runnable接口
        // 继承Thread
        Thread t = Thread.currentThread();
        t.setName("Main Thread");
        System.out.println("Main Thread: "+t);
        new NewThread(); // 这句话表明子线程先运行
        try {
            for (int item : IntStream.rangeClosed(1,5).toArray()) {
                System.out.println("Main Thread: "+item+"----"+t);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Main Thread stop!");
        }
        System.out.println("----------------------------------");
        new NewThread2();
        try {
            for (int item : IntStream.rangeClosed(1,5).toArray()) {
                System.out.println("Main: "+item);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Main stop!");
        }
    }
}

// Runnable interface:
class NewThread implements Runnable {
    Thread t1;
    NewThread() {
        t1 = new Thread(this);
        t1.setName("Child Thread");
        System.out.println("Child Thread: "+t1);
        t1.start();
    }

    public void run() {
        try {
            for (int item : IntStream.rangeClosed(1,5).toArray()) {
                System.out.println("Child Thread: "+item+"----"+t1);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Child Thread stop!");
        }
    }
}

// 我们从运行的结果中学习到：
// 1.我们并没有写t1.run();,实际上都包含在ti.start();中了
// 2.主线程和子线程共享CPU运行，但是没有固定的顺序
// 3.我们之前说过，主线程必须是最后结束的，但是这里有可能是主线程的任务先结束，
//   那么随之而来的问题就是主线程会被挂起来，资源就浪费了，
//   为了规避这种浪费，我们可以适当缩短子线程的沉睡时间，比如改成700 millis

// inherited Thread
class NewThread2 extends Thread {
    NewThread2() {
        super("Child2");
        System.out.println("Child2: "+this);
        start();
    }

    public void run() {
        try {
            for (int item : IntStream.rangeClosed(1,5).toArray()) {
                System.out.println("Child2: "+item);
                Thread.sleep(700);
            }
        } catch (InterruptedException e) {
            System.out.println("Child2 stop!");
        }
    }
}

// 那么自然的，多线程的创建也就没什么问题了...