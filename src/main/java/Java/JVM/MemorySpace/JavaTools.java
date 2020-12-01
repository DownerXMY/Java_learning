package Java.JVM.MemorySpace;
/*
我们介绍一些Java的工具,尤其是JDK自带的工具
当然我们要知道怎么用这些工具,最好的方式当然是通过读官方的帮助文档
具体的操作是:(我们以jmap为例)
1.which jmap
2.进入到打印出来的地址
3.然后直接输入命令jmap,
4.就会打印出官方的帮助文档...
 */

public class JavaTools {

    public static void main(String[] args) {
        /*
        我们先介绍jmap(Java内存映象工具)
        可以看到官方文档给的使用方式是:
        jmap [option] <pid>
        后面要跟着一个进程的id(注意是进程id)
        那么首先第一个问题来了,我们怎么知道具体id呢？
        其中一个很愚蠢的方式是我们运行程序,然后从jvisualvm或者jconsole里面去查找
        当然我们有更加专业的方式,比如通过终端命令:
        ps -ef | grep java
        当然还是有别的方式,比如我们之前熟知的jps同样也可以...
        那么其中对应打印出来的第二个数字就是进程id(注意这里是进程),比如我们这里是1445
        所以呢,我们可以用jmap了
        jmap -clstats 1445
        但是你会发现实际上是连不上的,这其实是Mac系统的问题,可以理解成是一个BUG
        只有在JDK11以后才能没有这样的BUG...
        实际上jmap的用处就是获取当前线程类加载器的关系
        -----------------------
        接下去我们介绍新的工具:
        jstat -gc <vmid>
        实际上在打印出来的信息中,我们重点要关注的是MC和MU这两个参数,
        MC表示系统为应用设定的元空间大小,MU表示已经使用的元空间大小,
        比如我们如果还是用上一次写的cglib的动态代理的程序,
        那么我们应该是要发现MC在不断增加...
        可以检验就是这样子的,实际我们测试的结果是:
        MC和MU将会一起不断加大,这也印证了元空间的动态扩容的特性...
        -----------------------
        我们接下去将介绍一个非常重要的工具jcmd,
        当然我们可以采用jcmd -help来查看所有的参数...
        当然接下去我们只介绍其中比较重要的那一些...
        jcmd 2627 VM.flags(查看JVM的启动参数)
        jcmd 2627 help(我们可以对当前Java进程执行的操作)
        jcmd 2627 PerfCounter.print(查看当前进程的所有性质信息)
        jcmd 2627 JFR.dump
        jcmd 2627 GC.class_histogram(查看当前进程中的所有类的统计信息)
        jcmd 2627 Thread.print(打印出当前进程的所有线程信息(线程的堆栈信息))
            实际上我们还能看到这个命令可以检测出死锁信息...
        jcmd 2627 GC.heap_dump /Users/mingyuexu/Desktop/print.hprof
            (生成堆转储文件,到我们指定的目录,用我们指定的名字)
            (导出的堆转储文件可以通过jvisualvm查看)
        jcmd 2627 VM.system_properties(查看JVM的属性信息)
        -----------------------
        jstack可以查看Java应用程序中的堆栈信息
        -----------------------
        jmc(Java Mission Control)这是一个一体化的图形界面工具
        它相比于jconsole和jvisualvm来书更加强大
        但是针对于JDK8及以后,官方说明已经不自带jmc了,需要自行下载
        我们已经通过wget的方式将其放到了Home/bin中,打开的方式是:
        open "JDK Mission Control.app"
         */
        for (;;) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("this is a dead loop...");
        }
    }
}
