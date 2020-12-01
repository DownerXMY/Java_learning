package Java.JVM.GC;

/*
这是一个很综合的例子...
其中有一些新的JVM垃圾收集的参数:
-XX:TargetSurvivorRatio=60
    表示我们将Survivor空间中的对象使用率设为60%(默认为50%)
    那么这件事情还可以说的更加清楚一点:
        当Survivor空间中已经有60%被对象占据的时候,
        JVM就会重新设定晋升到老年代的阈值
-XX:+PrintGCDateStamps
    表示打印出GC的时间戳
-XX:+UseConcMarkSweepGC
    表示使用CMS垃圾收集器
 */

import java.util.stream.IntStream;

public class GCApp3 {

    public static void main(String[] args)
            throws InterruptedException {
        byte[] bytes1 = new byte[512 * 1024];
        byte[] bytes2 = new byte[512 * 1024];
        /*
        我们要注意这两个字节数组相对于下面方法中创建的字节数组
        是不会那么轻易被回收掉的,它们肯定是会在两个Survivor空间中来回copy
        那么他们的"存活年龄"就会不断增加...
         */

        demoGC();
        Thread.sleep(1000);
        System.out.println("Hello world...");

        demoGC();
        Thread.sleep(1000);
        System.out.println("Hello again1...");

        demoGC();
        Thread.sleep(1000);
        System.out.println("Hello again2...");

        demoGC();
        Thread.sleep(1000);
        System.out.println("Hello again3...");

        byte[] bytes3 = new byte[1024 * 1024];
        byte[] bytes4 = new byte[1024 * 1024];
        byte[] bytes5 = new byte[1024 * 1024];
        // 在这里发生的GC之后,我们通过日志可以明显看出:
        // 晋升的Threshold从3变成了1,这是因为
        // 我们通过-XX:+PrintHeapAtGC可以看到
        // Survivor空间的利用率已经到了63%,这超出了我们设定的Ratio
        // 所以JVM会动态地帮助我们重新设定晋升的阈值...
        // 比如我们将最初设定的阈值改成70%,那么会发现就没有调整
        // 因为没有超出过这个利用率...

        demoGC();
        Thread.sleep(1000);
        System.out.println("Hello again4...");

        demoGC();
        Thread.sleep(1000);
        System.out.println("Hello again5...");

        System.out.println("Finally...");
    }

    private static void demoGC() {
        // 首先我们要很清楚一点,在方法中定义的变量叫做"局部变量"
        // 它将会随着方法的结束而结束其声明周期,并在下一次GC的时候被回收
        IntStream.rangeClosed(1,40).forEach(i -> {
            byte[] byteArray = new byte[1024 * 1024];
        });
    }
}
