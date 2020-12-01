package Java.JVM.GC;
/*
之前我们就说过,新生代被划分成了一个Eden空间和两个Survivor空间,
而且两个Survivor空间是轮流转换的,
那么换句话说未被垃圾回收的对象将会往返于两个Survivor空间,
直到某一个时候才会晋升到老年代中去,经历过的GC的次数就叫做"存活年龄"
----------------------
如果达到某个年龄段以后发现总大小已经大于Survivor空间的50%,
那么这时候就要调整阈值,否则会导致Survivor的空间不足,导致直接启用老年代.
注意这一点非常重要,我们将在GCApp3中具体分析...
----------------------
在这个新的例子中,我们新增加了几个JVM参数:
-XX:+PrintHeapAtGC
    表示打印出GC前后堆可用容量的变化
-XX:MaxTenuringThreshold=5
    表示规定了晋升到老年代中的对象的存活年龄
    在CMS中默认值是6,但是在G1中是15
-XX:+PrintTenuringDistribution
    表示打印出不同年龄段的对象的信息汇总
 */

public class GCApp2 {

    public static void main(String[] args) {

        int size = 1024 * 1024;

        byte[] Allocation1 = new byte[2 * size];
        byte[] Allocation2 = new byte[2 * size];
        byte[] Allocation3 = new byte[2 * size];
        byte[] Allocation4 = new byte[2 * size];

        System.out.println("Hello world...");
    }
}
