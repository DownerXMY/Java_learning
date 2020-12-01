package Java;

public class Thd_synchronization2 {

    public static void main(String[] args) {
        // 现在有一个问题：
        // 按照我们之前的方法是在类内方法之前加上synchronized实现同步
        // 那么万一我们不能获取到源代码呢
        // 事实上这种情况是很常见的，因为我们总是在调用别人写的代码
        Thread t = Thread.currentThread();
        t.setName("Main");
        People_demo p = new People_demo();
        Realize r1 = new Realize(p,"MingyueXu",21);
        Realize r2 = new Realize(p,"QidongSu",20);
        Realize r3 = new Realize(p,"HaoyuShi",22);
        try {
            r1.t1.join();
            r2.t1.join();
            r3.t1.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}

class People_demo {
    public void print_info(String name1,int age1) {
        System.out.print(name1+" is of age ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println(age1);
    }
}

class Realize implements Runnable {
    Thread t1;
    People_demo people;
    String name;
    int age;
    public Realize(People_demo p,String name1,int age1) {
        people = p;
        name = name1;
        age = age1;
        t1 = new Thread(this);
        t1.start();
    }
    public void run() {
        // 发现不能同步了，实际上是因为people包含了所有的p1,p2,p3
        // 所以相当于后面的所有程序都是同步的
        // 这个时候只能使用之前的方法
//        synchronized(people) {
//            people.print_info();
//        }
        synchronized (people) {
            // 再换一种说法，这里有个坑，就是:
            // synchronized(people)
            // 会把所有的people实例都同步化
            // 比如你同时有:
//            People_demo p1 = new People_demo();
//            People_demo p2 = new People_demo();
            // 那么不论如何,p1和p2参与的Realize线程都将会同步进行...
            people.print_info(name,age);
        }
    }
}