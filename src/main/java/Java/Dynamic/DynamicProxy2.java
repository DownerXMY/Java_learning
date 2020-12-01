package Java.Dynamic;
/*
之前我们已经说了什么叫做静态代理
但是问题随之而来也产生了:
如果每一个类都需要实现方法运行前后的调试,
但是我们肯定也不希望我们写的类被随便改动,
那么只能一个个地写代理,这显然也是不现实的...
这个时候,为了解决这个问题,"动态代理"被发明出来了!

我们还是用之前静态代理的例子来看...
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy2 {

    static interface IService {
        public void sayHello();
    }

    public static class RealService implements IService {
        public void sayHello() {
            System.out.println("Hello World!");
        }
    }

    static class SimpleInvocationHandler
            implements InvocationHandler {

        private Object realObject;
        // 和静态方法不同的地方,我们并没有定义一个接口字段

        public SimpleInvocationHandler(Object object) {
            this.realObject = object;
        }

        @Override
        public Object invoke(Object proxy,
                             Method method,
                             Object[] args)
                throws Throwable {
            System.out.println("entering "+method.getName());
            Object result = method.invoke(realObject,args);
            System.out.println("leaving "+method.getName());
            return result;
        }
    }

    public void demo1(IService realService,
                      InvocationHandler invocationHandler) throws Throwable {
        IService proxyService2 =
                (IService) Proxy.newProxyInstance(
                        IService.class.getClassLoader(),
                        new Class<?>[] {IService.class},
                        (InvocationHandler) invocationHandler.invoke(
                                realService,
                                realService.getClass()
                                        .getMethod("sayHello"),
                                null
                        )
                );
        proxyService2.sayHello();
    }

    public void dynamic_proxy1() {
        System.out.println("Java SDK:");
    }

    /*
        不过我们肯定是会有很多疑问的:
        1.ClassLoader是什么？
            我们会在后面的某个时候重点介绍
        2.我们肯定想看看newProxyInstance()是什么样的？

            public static Object newProxyInstance(
            ClassLoader loader,
            Class<?>[] interfaces, // 这表示代理要实现的接口列表
            InvocationHandler h
            // 是一个接口,
               其中只有invoke()一个抽象方法,
               对代理接口的所有方法的调用都会转给该方法
            ) {}
    */

    public static void main(String[] args) throws Throwable {
        DynamicProxy2 dynamicProxy2 = new DynamicProxy2();
        dynamicProxy2.dynamic_proxy1();
        IService real_service = new RealService();
        IService proxyService =
                // 这里创建代理的方式比较特别...
                (IService) Proxy.newProxyInstance(
                IService.class.getClassLoader(),
                new Class<?>[] {IService.class},
                new SimpleInvocationHandler(real_service)
        );
        proxyService.sayHello();
        // 我们实现了和之前一模一样的结果...
        System.out.println("-----------------------");
        /*
        -------------------------
        我们还是要去了解动态代理的基本原理:
        实际上创建proxyService的代码块也可以这么写:

        Class<?> proxyCls =
                Proxy.getProxyClass(
                        IService.class.getClassLoader(),
                        new Class<?>[] {IService.class});
        第一步:创建了代理类的定义,类定义会被缓存...
        Constructor<?> constructor = proxyCls.getConstructor(
                new Class<?>[] {InvocationHandler.class}
        );
        第二部:获取代理类的构造方法...
        InvocationHandler handler =
                new SimpleInvocationHandler(real_service);
        IService proxyService =
                (IService) constructor.newInstance(handler);
        最后:创建代理类对象...
        !!!而"Proxy.newProxyInstance()只是能够把这三部综合起来而已...
        --------------------------

        那么这样一来,我们就能去说为什么叫做"动态代理"了,
        换句话说,"动态"究竟体现在哪里？

        1.看到第一步中的proxyCls和被代理的对象"real_Service"没有关系,
        2.与InvocationHandler的具体实现也没关系,
        3.只和接口数组(接口)有关系,它动态实现了接口的抽象方法
        4.实现以后就转发给InvocationHandler
        5.它与被代理对象的关系以及它的调用都是由InvocationHandler管理的

        --------------------------
        那么这样一来是不是就解决了我们最早提出的问题了啊？
        只要把接口都放进接口列表中就好了...
        */

        // 此外,我们注意到InvocationHandler是一个函数式接口,
        // 这意味着我们可以尝试使用Lambda...
        dynamicProxy2.demo1(
                real_service,
                (proxy,method,args1) -> {
                    System.out.println("Entering "+method.getName());
                    Object result = method.invoke(proxy,args1);
                    System.out.println("Leaving "+method.getName());
                    return result;
                }
        );
        // 最终确实是写出来,
        // 但是现在看来及其不建议用Lambda写InvocationHandler...
    }
}
