package Java.JVM;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;

public class ContextClassLoaderApp2 {

    public void demo1() {
        ServiceLoader<Driver> serviceLoader1 =
                ServiceLoader.load(Driver.class);
        Iterator<Driver> iterator = serviceLoader1.iterator();

        while(iterator.hasNext()) {
            Driver driver = iterator.next();
            System.out.println("driver: "+driver.getClass());
            System.out.println("loader: "+driver.getClass().getClassLoader());
        }
        System.out.println(
                "current Thread Context ClassLoader: "
                        +Thread.currentThread().getContextClassLoader());
        System.out.println(
                "Service Loader: "
                        +ServiceLoader.class.getClassLoader());
        /*
        通过程序运行,我们看到以下这些结果:
        1.加载MySQL驱动"com.mysql.cj.jdbc.Driver"的类加载器是AppClassLoader
        2.当前线程的上下文类加载器是AppClassLoader
        3.而ServiceLoader的加载器是启动类加载器,因为它本身是位于Java核心包中的组件
         */
        /*
        当然我们会有一些问题:
        我们在程序中仅仅只是写了Driver.class,其中Driver是一个接口
        类加载器是怎么就找到了MySQL的驱动的呢?
        --------------------
        实际上是根据ServiceLoader的加载标准找到的,
        关键是要到META-INF/services目录下找完全限定的二进制驱动名...
        --------------------
        第二个问题就是:
        为什么驱动是由AppClassLoader去加载的呢?
        --------------------
        最本质的原因是因为在load()方法中,
        默认设定了加载的类加载器是当前线程上下文类加载器...
        --------------------
         */
     }

     public void demo2() {
        /*
        现在我们对程序做一个简单的改动...
        我们尝试手动改变一下当前线程的上下文类加载器
         */
         Thread.currentThread().setContextClassLoader(
                 ContextClassLoaderApp.class
                         .getClassLoader().getParent()
         );
         ServiceLoader<Driver> serviceLoader2 =
                 ServiceLoader.load(Driver.class);
         Iterator<Driver> iterator = serviceLoader2.iterator();

         while(iterator.hasNext()) {
             Driver driver = iterator.next();
             System.out.println("driver: "+driver.getClass());
             System.out.println("loader: "+driver.getClass().getClassLoader());
         }
         System.out.println(
                 "current Thread Context ClassLoader: "
                         +Thread.currentThread().getContextClassLoader());
         System.out.println(
                 "Service Loader: "
                         +ServiceLoader.class.getClassLoader());
         /*
         那么我们发现Iterator中就没有东西了,
         说明ExtClassLoader是找不到MySQL的驱动的...
          */
     }

    public static void main(String[] args) {
        new TestThread();
        /*
        我们来通过JDBC看看线程上下文类加载器的具体实现
        1.首先在maven中引入MySQL驱动的依赖...
         */
        ContextClassLoaderApp2 CCL1 = new ContextClassLoaderApp2();
        CCL1.demo1();
        System.out.println("--------------------");
        CCL1.demo2();
    }
}

class TestThread implements Runnable {

    private Thread thread;

    public TestThread() {
        thread = new Thread(this);
        thread.start();
    }
    @Override
    public void run() {
        ClassLoader classLoader =
                this.thread.getContextClassLoader();
        this.thread.setContextClassLoader(classLoader);
        System.out.println("Class: "+classLoader.getClass());
        System.out.println("Parent: "+classLoader.getParent());
        System.out.println("-------------------");
        /*
        我们看到我们打印出来的线程上下文类加载器已经是AppClassLoader,
        但是我们没有自己设置过,那就意味着肯定在某个地方系统帮我们自动设定了...
        实际上在sun.misc.Launcher中有一行代码
        将系统类加载器设置成了启动运行的线程的上下文类加载器
         */
        // 线程上下文类加载器的使用模式: 获取,使用,还原
    }
}