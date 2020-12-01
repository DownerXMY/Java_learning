package Java.Dynamic;

/*
我们闲杂回过头去看看,Java的动态机制我们已经学了一部分了
现在我们能不能理解为什么叫做"动态机制"(Dynamic)呢？
-----------------
首先,我们要明确的是:Java Reflection是动态机制的基础,
然后,在注解中我们已经看到了怎么利用反射提取"注解"中的信息,
最最重要的是,我们通过这些反射提取到的信息写了代码,使得能够在运行过程中创建类
-----------------
那么,动态代理就是允许我们在运行时动态创建一个类,实现一个或多个接口,
    可以在不修改原有类的基础上动态地为通过该类获取的对象添加方法,修改行为等
-----------------
动态代理实际上是实现AOP(Aspect Oriented Programming)面向切面编程的基础
广泛应用于SSH,能做的事情包括:日志,性能监控,权限检查,数据库事务,...
动态代理有两种实现方式,
一种是Java SDK提供的,另外一种是由第三方库(cglib)提供的
-----------------
总的一句话,这是非常难的东西...
 */

/*
什么叫做"动态代理":
动态代理使得我们在运行时动态创建个类
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DynamicProxy1 {

    public void introduction() {
        /*
        要想理解"动态代理",我们首先要理解"静态代理"
        关键的要点就是下面这几句话:
        1.所谓"代理",就是用户不会直接去接触实际对象,而是跟它的代理打交道
        2.创建代理的时候不会真正地创建实际对象,只要在需要的时候才会去加载或创建
        3.比如涉及到"权限检查",肯定是代理检查权限,然后才是创建实际对象
         */
        System.out.println("Dynamic-Proxy:");
    }

    static interface IService {
        public void sayHello();
    }

    static class RealService implements IService {
        public void sayHello() {
            System.out.println("Hello World!");
        }
    }

    static class TraceProxy implements IService {

        private IService realService;

        public TraceProxy(IService iService) {
            this.realService = iService;
        }
        public void sayHello() {
            System.out.println("entering sayHello...");
            this.realService.sayHello();
            // 这里的问题就是sayHello在接口中是一个抽象方法,
            // 为什么能够这样调用呢？
            /*
            实际上后面我们是采用了Java多态中最重要的一个特性:
            具体类实现接口
            我们最熟悉的例子就是:
            Lock lock = new ReentrantLock();
             */

            System.out.println("leaving sayHello...");
        }
    }

    public static void main(String[] args) {
        DynamicProxy1 dynamicProxy1 = new DynamicProxy1();
        dynamicProxy1.introduction();
        System.out.println();
        IService realService = new RealService();
        IService proxyService = new TraceProxy(realService);
        proxyService.sayHello();
        /*
        让我们来想明白上面这件事情:
        在这个例子中,
        IService是公共接口,实际对象是RealService,代理是ProxyService
        我们要达到的目的是什么呢?
        1.我要在RealService这个类的实际对象sayHello方法调用前后加上一些调试语句
        2.我不想去修改RealService这个类(否则你直接在sayHello里修改就好了)

        最后,在代理ProxyService中的代码是在写程序是就固定下来的,
        所以我们称之为"静态代理"...
         */
    }
}
