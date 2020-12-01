package Java;

public class multithreadingApp {

    public static void main(String[] args) {
        // JAVA的多线程是内置的
        // 多任务处理有两种不同的形式：基于进程和基于线程
        // 多进程程序处理"大图片"，多线程程序处理细节
        // 多进程不收JAVA控制，多线程收到JAVA控制
        // 多线程帮你写出CPU的最大利用效率

        System.out.println("Main Thread:");
        // 当JAVA程序跑起来的时候，一个线程马上就运行起来了，成为"主线程"
        // 主线程执行各种关闭动作，所以它必须最后被完成执行
        // 由主线程产生各种子线程
        // 尽管主线程自动创建，但是它可以由一个Thread对象控制
        // 详情查看别的文件.
    }
}