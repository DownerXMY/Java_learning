package Java;

public class Thd_synchronization3 {

    public static void main(String[] args) {
        Thread t = Thread.currentThread();
        t.setName("Main");
        Demo demo = new Demo();
        demo.school = "SJTU";
        place m1 = new place(demo,"上院");
        place m2 = new place(demo,"中院");
        place m3 = new place(demo,"下院");
        try {
            m1.t1.join();
            m2.t1.join();
            m3.t1.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }
    }
}

class Demo {
    String school;
    public void schoolplace(String place) {
        System.out.print(school);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }
        System.out.println(place);
    }
}

class place implements Runnable {
    Demo demo;
    Thread t1;
    String place;
    public place(Demo d, String placestring) {
        demo = d;
        place = placestring;
        t1 = new Thread(this);
        t1.start();
    }
    public void run() {
        // 用同步块实现同步...
        synchronized (demo) {
            demo.schoolplace(place);
        }
    }
}