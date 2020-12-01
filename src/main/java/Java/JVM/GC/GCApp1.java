package Java.JVM.GC;

/*
实际上,我们之前就说过,如果一个对象没法在新生代生成,
那么它将直接在老年代生成,这是JVM帮我们默认的事情,不过我们可以设置这个标准,
换句话说,就是人为规定到什么时候对象应该直接在老年代生成...
------------------
那么我们设置的JVM参数除了上次已经介绍的之外,
我们还额外增加了一个:-XX:+PretenureSizeThreshold=4194304
这实际上是我们设定的阈值,
换句话说,超过了这个4M内存大小,对象将会直接在老年代生成...
------------------
那么我们运行,我们会发现实际上结果并不是我们所预料的那样
对象还是在Eden空间上生成并被赋予内存空间了,这是怎么回事呢?
实际上JVM的参数很多时候都是需要关联起来看的,不如我们这里:
-XX:PretenureSizeThreshold=XXXXX实际上是使用与串行的GC,
但是我们早就说过了,JDK8的默认GC组合是Parallel
不过JVM的一切都是可以通过虚拟机参数来人为控制的,
所以我们可以再加上-XX:+UseSerialGC
实际上我们通过打印输出我们还能看到一些Serial和Parallel的区别:
比如"tenured generation",这其实就是Parallel中的Old,翻译过来叫做"持久代"
 */

public class GCApp1 {

    public static void main(String[] args) {
        /*
        我们可以通过命令打印出JVM的默认参数...
        -XX:+PrintCommandLineFlags -version
        当然这个命令不一定要加到程序的配置文件中去,实际上我们可以在终端直接查看:
        java -XX:+PrintCommandLineFlags -version
        我们看到输出结果的最后一个Flag是:
        -XX:+UseParallelGC这实际上就指明了我们在JDK8的时候默认的GC组合
         */
        int size  =1024 * 1024;

        byte[] Allocation1 = new byte[5 * size];
        /*
        1.当然这里如果我们是创建一个大小为5M的对象,
        我们知道如果采用的还是Parallel,那么将会在Eden空间生成
        2.如果改成8M,那么我们也很容易理解将会直接在老年代生成
        3.那么如果改成10M呢,会发生什么事情?
          我们看到不仅发生了多达5次GC,而且还发生了OOM错误...
         */

        System.out.println("Hello world...");

        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
