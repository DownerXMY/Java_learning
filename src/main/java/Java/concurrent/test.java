package Java.concurrent;

import sun.java2d.loops.TransformHelper;

public class test {

    public static void main(String[] args) throws Exception {
        newThd thd_1 = new newThd();
        Thread t1 = new Thread(thd_1);
        Thread t2 = new Thread(thd_1);
        t1.start();
        t2.start();
    }
}
class newThd implements Runnable {
    // 如果一个对象中存在可被修改的成员变量，那么称为有状态的对象...
    int x;
    public void run() {
        x = 0;
        while (true) {
            System.out.println("result = " + x++);
            try {
                Thread.sleep((long)Math.random()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (x == 30) {
                break;
            }
        }
    }
}