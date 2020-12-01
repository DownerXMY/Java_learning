package Java.JVM.MemorySpace;

/*
之前我们说过,在方法区中,一个类想要被垃圾回收的条件是比较苛刻的,
所以在方法区中,发生内存溢出的错误也是比较常见的,简称为"方法区溢出"
在JDK8以后,因为方法区中的"永久代"完全被"元空间"所替代,
而因为元空间具有"动态扩容"的特性,
这使得"方法区溢出"变得异常不容易,
当然我们还是可以有通过设置JVM参数的手段来看到方法区溢出的情况...
这里我们尝试采用cglib...
当然最后我们要加上虚拟机参数:
-XX:MaxMetaspaceSize=10m
------------------
元空间究竟是什么?

 */

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class MetaSpace {

    public static void main(String[] args) {
        for(;;) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(MetaSpace.class);
            enhancer.setUseCache(false);
            MethodInterceptor methodInterceptor =
                    (object,method,argz,proxy) -> {
//                        System.out.println("Entering "+method.getName());
                        Object result = proxy.invokeSuper(object,argz);
//                        System.out.println("Leaving "+method.getName());
                        return result;
                    };
            enhancer.setCallback(methodInterceptor);
            System.out.println("It's running...");
            enhancer.create();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            // 注意加上线程睡眠,那么元空间的内存使用情况将和之前大为不同
            // 具体的原因还未知...
        }
    }
}
