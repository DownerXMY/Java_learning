package Java;

public class Thd_conmunication {

    public static void main(String[] args) {
        // 线程之间的通信
        // 我们需要考虑类似序列化的问题:
        // 当一个程序正在产生数据，而另一个程序却需要去使用这些数据
        Thread t = Thread.currentThread();
        t.setName("Main");
        // 我们会用到 wait(),notify(),notifyAll()这三个API
        // 它们都是仅在synchronized方法中才能被使用,我们查看源码的doc就会知道是为什么
        Q q = new Q();
        Producer p = new Producer(q);
        Consumer c = new Consumer(q);
        p.start();
        c.start();
        try {
            p.t1.join();
            c.t2.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }
        System.out.println("Can be stopped on keyboard");
    }
}

// wait( )告知被调用的线程放弃管程进入睡眠直到其他线程进入相同管程并且调用notify( )
// notify( ) 恢复相同对象中第一个调用 wait( ) 的线程
// notifyAll( ) 恢复相同对象中所有调用 wait( ) 的线程。具有最高优先级的线程最先运行

class Q {
    int n;
    boolean valueSet = false;
    synchronized int get() {
        if(!valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted!");
            }
        } else {
            System.out.println("Got: "+n);
            valueSet = false;
            notify();
        }
        return n;
    }
    synchronized void put(int n) {
        if (valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted!");
            }
        } else {
            this.n = n;
            valueSet = true;
            System.out.println("Put: "+n);
            notify();
        }
    }
}

class Producer implements Runnable {
    Thread t1;
    Q q;
    Producer(Q q) {
        this.q = q;
        t1 = new Thread(this);
        t1.setName("Producer");
    }
    public void run() {
        int i = 0;
        try {
            while(true) {
                q.put(i++);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Take a rest...");
        }
    }
    public void start() {
        t1.start();
    }
}

class Consumer implements Runnable {
    Thread t2;
    Q q;
    Consumer(Q q) {
        this.q = q;
        t2 = new Thread(this);
        t2.setName("Consumer");
    }
    public void run() {
        try {
            while(true) {
                q.get();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Take a rest...");
        }
    }
    public void start() {
        t2.start();
    }
}

// 可以看到，线程之间的通信还是非常实用的...
// 有一点需要注意，线程通信必须在同步的方法中才能使用!!!