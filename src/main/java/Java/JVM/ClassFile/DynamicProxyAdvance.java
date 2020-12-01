package Java.JVM.ClassFile;

import java.lang.reflect.*;

public class DynamicProxyAdvance {

    public static void main(String[] args)
            throws Exception {
        System.getProperties().put(
                "sun.misc.ProxyGenerator.saveGeneratedFiles",
                "true");
        /*
        这条代码是改变了一个系统属性,
        然后我们运行程序的时候,系统就会自动帮助我们生成代理类的字节码文件
        文件位于:Java/JVM/ClassFile/
        这都是阅读源码的收货...
        源码中有这样的语句
            if (saveGeneratedFiles) {
                Files.write(var2, var4, new OpenOption[0]);
            }
        然后我们查找这个变量就能找到对应的系统属性:
        private static final boolean saveGeneratedFiles = (Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.misc.ProxyGenerator.saveGeneratedFiles"));
        */

        RealSubject realSubject = new RealSubject();
        InvocationHandler invocationHandler =
                new DynamicSubject(realSubject);
        // 这里显然是一个Java多态的实现
        // 换句话说,这里的invocationHandler的静态类型是InvocationHandler,
        // 而实际类型是DynamicSubject
        Class<?> cls = realSubject.getClass();
        System.out.println(cls.getClassLoader());
        System.out.println("---------------------");
        // 接下去我们创建具体的代理...
        Subject proxy_subject =
                (Subject) Proxy.newProxyInstance(
                        cls.getClassLoader(),
                        cls.getInterfaces(),
                        // 注意这里可能跟我们之前的写法有点不同,
                        // 这里我们采用了反射的方式获取接口类型列表...
                        invocationHandler);
        proxy_subject.request();
        System.out.println("---------------------");
        proxy_subject.computing();
        /*
        这样一来,我们就实现了在方法前后打印日志的效果...
        当然这是最最简单的一个动态代理的例子,我们首先要充分理解java代码
        然后我们要从字节码层面去看看这个代理是怎么生成出来的...
        根据我们提供的必要参数:类加载器,接口,InvocationHandler的实例
        生成一个字节数组(实际上就是动态代理类的字节码文件对应的字节数组)
        然后创建这个类的实例,实现动态代理...
         */
        System.out.println("=====================");
        // 我们采用一种更加具体的方式创建代理,
        // 本质上就是细化了上面的创建方式...
        DynamicProxyAdvance dynamicProxyAdvance =
                new DynamicProxyAdvance();
        Subject proxy = dynamicProxyAdvance.establish_proxy();
        proxy.request();
        System.out.println("---------------------");
        proxy.computing();
        /*
        现在我们就知道了,invoke()方法究竟是在哪里实现的,
        实际上我们看反编译生成的代理类的字节码文件,我们就能知道,
        当我们调用代理对象的request()方法的时候,代理就会把它派发给invoke()方法,
        所以我们能够实现类似于方法重写的效果...
         */
    }

    public Subject establish_proxy()
            throws IllegalAccessException,
            InvocationTargetException,
            InstantiationException,
            NoSuchMethodException {
        RealSubject realSubject = new RealSubject();
        InvocationHandler dynamicSubject =
                new DynamicSubject(realSubject);
        Class<?> cls = realSubject.getClass();
        // 获取到代理的类型...
        Class<?> proxy_class =
                Proxy.getProxyClass(
                        cls.getClassLoader(),
                        cls.getInterfaces());
        // 我们想知道代理的类型究竟是什么?以及它的父类...
        int n = 1;
        Class<?> result = proxy_class;
        while (n <= 3) {
            System.out.println(result);
            result = result.getSuperclass();
            n++;
        }
        System.out.println("---------------------");
        // 定义代理的构造器...
        Constructor<?> constructor =
                proxy_class.getConstructor(
                        new Class<?>[] {InvocationHandler.class});
        Subject proxy_subject =
                (Subject) constructor.newInstance(dynamicSubject);
        // 为什么这个new Instance()可以传入一个参数,而且是必须传入一个参数,
        // 本质原因就在我们反编译出来的代理类字节码文件中,
        // 我们会看到只有一个带一个入参的构造器
        return proxy_subject;
    }
}

interface Subject {

    void request();

    void computing();
}

class RealSubject implements Subject {

    private static int a = 1;
    private static int b = 2;

    @Override
    public void request() {
        System.out.println("Real Subject...");
    }

    @Override
    public void computing() {
        int result = a+b;
        System.out.println("Computing result = "+result);
    }
}

class DynamicSubject implements InvocationHandler {

    private Object subject;

    public DynamicSubject(Object object) {
        this.subject = object;
    }

    @Override
    public Object invoke(
            Object proxy,
            Method method,
            Object[] args) throws Throwable {

        System.out.println("Before calling..."+method);

        Object result = method.invoke(this.subject,args);

        System.out.println("After calling..."+method);

        return result;
    }
}