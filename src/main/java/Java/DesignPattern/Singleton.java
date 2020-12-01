package Java.DesignPattern;
/*
在一般的Java程序中,对象的创建都是随心所欲的,
但是有的时候实际需求并不是这样的:
举一个简单的例子,比如我们在设计计算机软件系统相关的类的时候,
视窗系统(window system)就只能出现一个,这是默认的标准,
这就意味着,我们在编写程序的时候只能调用一次new语句,
当然我们总不希望自己去记忆我们之前有没有调用过new,
所以我们需要一种设计模式来确保这件事情,
---------------------
能确保只生成一个实例的模式就被称为"Singleton"模式
---------------------
 */

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Singleton {

    public static void main(String[] args) {
        System.out.println("Start...");
        MySingleton mySingleton1 = MySingleton.getInstance();
        MySingleton mySingleton2 = MySingleton.getInstance();

        if (mySingleton1 == mySingleton2) {
            System.out.println("They are the same instances");
        } else {
            System.out.println("Thay are different...");
        }

        System.out.println("End...");
        /*
        那么从结果中我们就能发现,就说不管我们写了多少句创建实例的语句,
        最终我们生成的实例永远只有一个,那么这究竟是为什么呢?
        我们要说明以下几点事实:
        -----------------
        1.在MySingleton中,虽然我们没有定义构造器,
          但是JVM会为我们生成一个默认的无参构造器
        2.事情的关键就在于,我们采用的是getInstance()来创建实例
          我们看到这个方法返回的mysingleton实际上是MySingleton中的静态变量
          这就是我们实现Singleton模式的最关键的一步...
        3.那么这里其实也有我们之前学习类加载机制的内容作为支撑:
          实际上我们想象,我们说了一个类的初始化只有当他被首次使用的时候才会进行,
          所以在调用静态方法getInstance()的时候发生了MySingleton的初始化,
          作为静态成员变量mysingleton只会在这一次中被初始化
        ------------------
        最后我们来看一个假的Singleton模式,
        或者换句话说不是很严格的Singleton
        例如下面的MySingleton1
        */
        System.out.println("-------------------");
        Set<MySingleton1> set1 = new HashSet<>();
        Lock lock = new ReentrantLock();
        IntStream.rangeClosed(1,50).forEach(i -> {
            new Thread(() -> {
                lock.lock();
                try {
                    MySingleton1 mySingleton11 =
                            MySingleton1.getInstance();
                    set1.add(mySingleton11);
                } finally {
                    lock.unlock();
                }
            }).start();
        });
        System.out.println(set1);
        System.out.println(set1.size());
        /*
        那么最终我们会看到,我们创建了4个MySingleton1的实例,
        当然我们有别的办法防止这件事情的发生,
        比如我们曾经介绍过很多线程原子性操作的办法,最简单的比如Lock
        当然还有Synchronized关键字,再比如还要volatile关键字
        ...
         */
        // 通过这个例子,很欣慰学习了JVM和Java并发...
    }
}

class MySingleton {

    private static MySingleton mysingleton =
            new MySingleton();

    private MySingleton() {
        System.out.println("Only an instance...");
    }

    public static MySingleton getInstance() {
        return mysingleton;
    }
}

class MySingleton1 {

    private static MySingleton1 mySingleton1 = null;

    private MySingleton1() {
        System.out.println("Only an instance...");
    }

    public static MySingleton1 getInstance() {
        if (mySingleton1 == null) {
            mySingleton1 = new MySingleton1();
        }
        return mySingleton1;
    }
    /*
    为什么我们说这是个假的Singleton模式呢?
    实际上我们要考虑的是多线程并发的情况:
    假设线程P1调用了getInstance()方法,
        我们知道MySingleton1这个类就会初始化,
        那么静态变量mysingleton1也会跟着别初始化
    但是假如初始化的写入操作还没有结束,
        另外一个线程P2也开始调用getInstance,
        这个时候很可能会有不同的Mysingleton1的实例会被创建出来
     */
}
