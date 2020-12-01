package Java.Dynamic;
/*
我们之前实现利用Proxy.newProxyInstance()实现"动态代理",是在利用Java SDK
那么我们也可以用第三方库实现"动态代理"的功能,其中最为常用的是cglib
---------------------
我们再说一次:Java SDK能够实现动态代理的核心在于:
它能够直接实现对接口的代理!!!
---------------------
但是,如果一个类没有接口呢,那么Java SDK就没办法了
实际上这在实际开发中是很常见的情况,
所以像比如Spring,Hibernate这些企业级框架是不太用Java SDK的,
它们更加青睐能够解决这类问题的第三方库cglib
我们来看一个简答的例子,看看cglib是怎么实现动态代理的...
 */

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class DynamicProxy4 {

    // 这是我们需要代理的实例类,你可以看到他是没有实现接口的
    static class RealService {
        public void sayHello() {
            System.out.println("Hello world");
        }
    }

    static class SimpleInterceptor
            implements MethodInterceptor {

        @Override
        public Object intercept(
                Object object,
                Method method,
                Object[] args,
                MethodProxy proxy) throws Throwable {
            System.out.println("Entering "+method.getName());
            Object result = proxy.invokeSuper(object,args);
            System.out.println("Leaving "+method.getName());
            return result;
        }
    }

    private static <T> T getProxy(Class<T> cls) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new SimpleInterceptor());
        return (T) enhancer.create();
    }

    public static void main(String[] args) {
        RealService proxyService =
                getProxy(RealService.class);
        proxyService.sayHello();
    }
    /*
    我们已经实现了cglib带来的"动态代理"功能,
    在我们分析之前,我们必须要说明一点,
    那就是实际上cglib包我们以后其实不需要单独导入,
    企业级框架Spring-framework中就有,这也是我们以后必定是要去接触的框架...
    -----------------------
    那么接下去就让我们来分析一下cglib实现"动态代理"的基本原理...
    1.RealService是我们要代理的实际类,他没有实现接口
    2.getProxy()为它生成了一个代理对象
    3.注意,之所以还是"动态代理",是因为getProxy()方法拿到的是Class对象;
                            SimpleInterceptor中没有被代理的对象;
                            以及是通过invokeSuper()调用被代理类的方法
    -----------------------
    下面这一点是最重要的:
    cglib实现动态代理是通过继承实现的,
    它的核心原理是动态创建了一个类,这个类的父类恰好是要被代理的类
    然后代理类重写了父类的所有public非final方法,
    改为调用setCallback()中的所有方法
     */
}
