package Java;

public class Thd_synchronization {

    public static void main(String[] args) {
        // 线程同步
        // 在JAVA中的同步是非常简单的，因为所有对象都有与之对应的隐式管程
        // 一种比较不好的情况是，几个线程争着完成任务
        Thread t = Thread.currentThread();
        t.setName("Main");
        Callme target = new Callme();
        Caller obj1 = new Caller(target,"Hello");
        Caller obj2 = new Caller(target,"World");
        Caller obj3 = new Caller(target,"Love");
        obj1.start();
        obj2.start();
        obj3.start();
        try {
            obj1.t1.join();
            obj2.t1.join();
            obj3.t1.join();
        } catch (InterruptedException e) {
            System.out.println("Main Interrupted!");
        }
    }
}
// 加入synchronized关键字以后，运行的结果就是：
// [Hello]
// [Love】
// [World]

class Callme {
    // 实际上很简单，我们只要在call之前加上synchronized关键字就可以了
    synchronized void call(String msg) {
        System.out.print("["+msg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Callme Interrupted!");
        }
        System.out.println("]");
    }
}

class Caller implements Runnable {
    Callme target;
    Thread t1;
    String msg;
    public Caller(Callme tg, String s) {
        t1 = new Thread(this);
        target = tg;
        msg = s;
    }
    public void run() {
        target.call(msg);
    }
    public void start() {
        t1.start();
    }
}

// 结果很早糟糕，这显然不是我们想要的
// [Hello
//[World
//[Love
//]
//]
//]
// 因为有了sleep，所以更加明显地加剧了三个线程争抢CPU的时间
// 这实际上就说明了为什么需要"Synchronization"
