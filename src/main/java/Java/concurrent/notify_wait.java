package Java.concurrent;

public class notify_wait {

    public static void main(String[] args) {
        System.out.println("To start the game...");
        count count = new count();
        new_thd thd1 = new new_thd(count,true);
        new_thd thd2 = new new_thd(count,false);
//        new_thd thd3 = new new_thd(count,true);
//        new_thd thd4 = new new_thd(count,false);
        new Thread(thd1).start();
        new Thread(thd2).start();
//        new Thread(thd3).start();
//        new Thread(thd4).start();
        // 这里实际上在尝试的过程中解决了我们的一个误区:
        // 你要好好去读源码的doc文档，注意区别两种构造新线程的方法
        // All in all, two runnable do not mean two threads!!!
    }
}

class count {
    private int count = 0;
    // wait()和notify()永远是成对出现的
    synchronized public void add() {
        if(count==0) {
            count += 1;
            System.out.print(count);
            notify(); // 这个notify()是为了唤醒下面那个wait的那个restore
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    synchronized public void restore() {
        if(count==0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            count -= 1;
            System.out.print(count);
            notify(); // 目的是为了唤醒wait的add方法
        }
    }
}

class new_thd implements Runnable {
    private count count;
    private boolean judge;
    new_thd(count c1,boolean jd) {
        this.count = c1;
        this.judge = jd;
    }
    public void run() {
        if (judge) {
            for(int i=0;i<30;i++) {
                try {
                    Thread.sleep((long)Math.random()*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count.add();
            }
        } else {
            for(int j=0;j<30;j++) {
                try {
                    Thread.sleep((long)Math.random()*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count.restore();
            }
        }
    }
}