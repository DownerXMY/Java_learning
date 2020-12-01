package Java.JVM.GC;
/*
我们将通过一个代码示例来看CMS收集器的效果...
比如我们将通过代码更加彻底地了解CMS的执行过程
-----------------
我们看到其中出现了这些打印输出:
GC (CMS Initial Mark)
[CMS-concurrent-mark-start]
[CMS-concurrent-preclean-start]
[CMS-concurrent-abortable-preclean-start]
GC (CMS Final Remark)
[CMS-concurrent-sweep-start]
[CMS-concurrent-reset-start]
我们看到,这就很直观地显示出了CMS执行垃圾回收的全过程...
 */

public class CMSApp {

    public static void main(String[] args) {

        int size = 1024 * 1024;

        byte[] bytes1 = new byte[4 * size];
        System.out.println("The first bytes...");
        byte[] bytes2 = new byte[4 * size];
        System.out.println("The second bytes...");
        byte[] bytes3 = new byte[4 * size];
        System.out.println("The third bytes...");
        byte[] bytes4 = new byte[2 * size];
        System.out.println("The final bytes...");

        System.out.println("Hello world...");

    }
}
