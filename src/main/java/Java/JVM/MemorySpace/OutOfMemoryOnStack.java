package Java.JVM.MemorySpace;

/*
Java虚拟机栈同样存在内存溢出的问题,
在我们使用的HotSpot虚拟机中并不区分虚拟机栈和本地方法栈,
我们用来检验栈内存溢出的一个常用操作是"方法的递归"...
换句话说,让一个方法永远不能终止,每次都是在中间再一次调用该方法本身
同时为了让程序能够更快输出,我们还要设置一个虚拟机参数...
---------------------
-Xss(设置内存大小)
---------------------
那么假如我们设置成比如:
-Xss100k
会发生什么事情呢?实际上这个时候虚拟机是会组织运行的:
The stack size specified is too small, Specify at least 160k
换句话说,JVM对栈的大小的需求是由要求的!
 */

public class OutOfMemoryOnStack {

    private int length;

    public int getLength() {
        return length;
    }

    public void demo() throws InterruptedException {
        this.length++;
        Thread.sleep(2000);
        // 降低程序的运行速度,然后可以通过jvisualvm来监视
        // 当然我们也可以用jconsole来监视...
        demo();
        // 无限循环...
    }

    public static void main(String[] args) {
        OutOfMemoryOnStack main1 =
                new OutOfMemoryOnStack();
        try {
            main1.demo();
        } catch (Throwable e) {
            System.out.println(main1.length);
            e.printStackTrace();
        }
    }
}
