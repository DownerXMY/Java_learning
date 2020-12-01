package Java.JVM;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ContextClassLoaderApp {

    public static void main(String[] args) {
        System.out.println(
                Thread.currentThread().getContextClassLoader()
        );
        System.out.println(Thread.class.getClassLoader());
        /*
        在介绍线程上下文类加载器之前,
        我们先来回顾一个概念"当前类加载器"(Current ClassLoader)
        我们先要重复一个事实,那就是:
        每一个类都会尝试用加载自身的类加载器去加载其他这个类所依赖的类...
        ----------------------
        线程上下文类加载器(Context ClassLoader)
        1.是从JDK1.2开始引入的,
        2.Thread类中的getContextClassLoader()与setContextClassLoader()
          分别用来获取和设置线程上下文类加载器,
        3.如果没有设置,那么线程将会继承其父线程的上下文类加载器,
        4.Java运行时启动的初始线程上下文加载器是"系统类加载器",
          那么我们知道如果不做任何改动,将会是AppClassLoader
        ----------------------
        在框架开发中非常常用,比如应用开发,web开发,典型的比如TomCat
        我们将会用一个JDBC的例子来看看线程上下文类加载器的效果...
         */
        System.out.println("-------------------");
        Lock lock = new ReentrantLock();
        IntStream.rangeClosed(1,3).forEach(i -> {
            new Thread(() -> {
                lock.lock();
                System.out.println(Thread.currentThread()
                        .getContextClassLoader());
                try {
                    Thread.sleep(2000);
                    IntStream.rangeClosed(1,9)
                            .forEach(System.out::print);
                    System.out.println();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }).start();
        });
    }
}
