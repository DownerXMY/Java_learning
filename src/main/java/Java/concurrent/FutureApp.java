package Java.concurrent;

import com.sun.org.apache.xpath.internal.functions.FuncTranslate;

import java.util.Random;
import java.util.concurrent.*;

public class FutureApp {

    public static void main(String[] args) {
        // 采用lambda表达式编写Callable
        Callable<Integer> callable = () -> {
            System.out.println("pre excution");
            Thread.sleep(5000);
            int rd = new Random().nextInt(500);
            System.out.println("post excution");
            return rd;
            // Callable和Runnable最大的区别在于Callable是有返回结果的,而Runnable没有
        };

        FutureTask<Integer> futureTask = new FutureTask(callable);
        // 我们要建一个线程将futureTask传进去
        // 为什么可以呢？
        // 因为查看源码可知,FutureTask继承了RunnableFuture,从而继承了Runnable
        new Thread(futureTask).start();
        System.out.println("Thread start...");
        try {
            Thread.sleep(2000);
            System.out.println(futureTask.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/* 这个例子中的睡眠可以很好的帮助我们理解异步处理机制的含义:
   子线程睡眠5秒，主线程睡眠2秒，
   按道理来说应该是主线程先运行，
   但是主线程中的get()方法，对于经过异步处理的子线程具有阻塞作用，
   换句话说，他必须无条件等待子线程完成futureTask中的任务，
   主线程需要等待3秒钟...
 */

/*
你一定要明白为什么我们要引入Future?
如果你仅仅只是为了解决线程先后顺序的问题，那么我们有太多的方法可以做到
你比如: 锁，CDL，CB
那么究竟为什么我们要引入Future呢？
要注意一下几点：
    这里不仅仅是线程的先后，而是前一个线程的结果要被后一个线程用到
    (举一个简单的例子:比如我们要用多线程计算斐波那契数列)
       --线程要有返回值，所以用Callable而不是Runnable
    因为线程的互斥性，一般情况下不能直接调用别的线程的结果的
       --Future就登场了，通过get()方法让当前线程阻塞，
         这样子就能call()获得前一个线程的返回结果了
 */