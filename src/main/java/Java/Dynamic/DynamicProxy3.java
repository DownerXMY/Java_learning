package Java.Dynamic;
/*
我们最终还是要来看看"动态代理"相对于静态代理的优势,
之前我们一直说静态代理的确是能解决所有的问题,
但是如果有很多类实现了很多不同的接口,
这样就会有很多的抽象方法需要代理实现调试,静态代理就只能一个个写出来
---------------------
其实"静态代理"的这种缺点源自它的代理思路:
根据具体的实现对象做出代理实例
---------------------
然而"动态代理"只需要构造出一个代理类,就能解决所有不同的接口及其实现类的代理
因为动态代理的思路是:
我代理出Interface,然后自动根据接口的实例区创建代理实例对象
---------------------
这么说可能还是有点抽象,我们可以来看一个简答的小例子...
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy3 {

    static interface IServiceA {
        public void sayHello();
    }

    static interface IServiceB {
        public void sayHello();
    }

    static class ServiceAImp implements IServiceA {
        public void sayHello() {
            System.out.println("Hello ServiceA");
        }
    }

    static class ServiceBImp implements IServiceB {
        public void sayHello() {
            System.out.println("Hello ServiceB");
        }
    }

    static class SimpleInvocationHandler
            implements InvocationHandler {

        private Object realObject;

        public SimpleInvocationHandler(Object object) {
            this.realObject = object;
        }

        @Override
        public Object invoke(
                Object proxy,
                Method method,
                Object[] args) throws Throwable {
            System.out.println(
                    "Entering "+
                    realObject.getClass().getSimpleName()+
                    "::"+ method.getName());
            Object result = method.invoke(realObject,args);
            System.out.println(
                    "Leaving "+
                    realObject.getClass().getSimpleName()+
                    "::"+method.getName());
            return result;
        }
    }

    private static <T> T getProxy(
            Class<T> intfs, T realObject) {
        return (T) Proxy.newProxyInstance(
                intfs.getClassLoader(),
                new Class<?>[] {intfs},
                new SimpleInvocationHandler(realObject));
    }

    public static void main(String[] args) {
        IServiceA iServiceA = new ServiceAImp();
        IServiceA AProxy = getProxy(IServiceA.class,iServiceA);
        AProxy.sayHello();
        System.out.println("------------------------");
        IServiceB iServiceB = new ServiceBImp();
        IServiceB BProxy = getProxy(IServiceB.class,iServiceB);
        BProxy.sayHello();
        /*
        那这下我们终于看到了,
        两个interfaces在两个不同的类中实现的的抽象方法是不一样的
        但是我们利用"动态代理",
        调用同样的方法getProxy()就实现了让他们共享代理逻辑,
        其实我们在前后加上注释的这种做法也是很常用的，
        在现实情况中,可以看作是添加日志打印...
        -------------------
        当然最终要的是要明白:动态代理实现的关键在于意识到了:
        问题的难点本质上是在不同的接口拥有不同的抽象方法,
        然后不同的实现会带来不同的"抽象方法的实例化"
        所以从接口的本质做文章是解决的捷径...
         */
    }
}
