package Java;

import java.util.stream.IntStream;

public class CurrentThreadDemo {

    public static void main(String[] args) {
        Thread t = Thread.currentThread();
        System.out.println("Current Thread: "+t);
        // 打印出来的5表示线程优先级，最后的main表示所属线程组的名称
        t.setName("My Thread");
        System.out.println("After name changed: "+t);
        try {
            for (int item : IntStream.rangeClosed(1,5).toArray()) {
                System.out.println(item);
                Thread.sleep(1000);
                // Sleep方法可能会引发异常，比如其他线程想要打搅沉睡的线程
            }
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }
    }
}
