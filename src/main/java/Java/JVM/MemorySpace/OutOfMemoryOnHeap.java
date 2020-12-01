package Java.JVM.MemorySpace;

/*
在Java堆空间出现 OutOfMemory 错误
注意 OutOfMemory 不是异常而是错误
下面我们执行的一个程序是死循环,
我们知道终有一个时刻会出现OutOfMemory的错误,
但是这样的等待时间未免会太长了
实际上我们可以通过设置一些JVM的参数可以更加快且准确地看到效果
----------------------
-Xms5m -Xmx5m -XX:+HeapDumpOnOutOfMemoryError
上面这三个参数的作用分别是:
设置堆空间的最小内存空间,设置堆空间的最大内存,打印出堆转储文件到磁盘上
----------------------
当然我们说了我们把内存堆转储快照打印到了磁盘上,
实际上就出现在了Java_learning/下,是一个.hprof的文件
那么我们要通过jvisualvm分析一下:
通过"类"的菜单栏,我们看到一共创建了240098个OutOfMemory的实例对象
当我们加入System.gc()以后,
实际上堆空间的使用一直维持在2m以内,垃圾回收活动异常频繁
----------------------
那么我们就像如果把堆空间的内存改成1m会发生什么?
运行我们就会知道,JVM挂掉了,OutOfMemory错误
 */

import java.util.ArrayList;
import java.util.List;

public class OutOfMemoryOnHeap {

    public static void main(String[] args) {
        List<OutOfMemoryOnHeap> list = new ArrayList<>();
        for (;;) {
            list.add(new OutOfMemoryOnHeap());
            /*
            现在我们加一行代码...
            目的是建议JVM对未被使用的对象进行回收,
            以便他们占据的内存能够被重用...
            这样以后运行,我们发现程序是能够一直运行的...
             */
            System.gc();
        }
    }
}
