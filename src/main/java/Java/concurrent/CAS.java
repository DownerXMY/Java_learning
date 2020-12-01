package Java.concurrent;

// atomic包是提供原子操作的
import java.util.concurrent.atomic.AtomicInteger;

public class CAS {

    private int count;

    public int getCount() {
        return count;
    }

    public void increase() {
        this.count++;
    }

    public static void main(String[] args) {
        // 比如我们有一个计数器，有多个线程访问，每一次访问都会让计数器加一
        // 但是如果不做任何附加操作，会出现两个线程访问以后只加一的情况
        // 这是因为，当其中一个线程还没完成写入操作的时候，另外一个线程已经在读取计数器了
        // 这就是CAS机制要解决的问题...
        System.out.println("Hello");
        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println(atomicInteger.get());
        System.out.println(atomicInteger.getAndSet(8));
        System.out.println(atomicInteger.get());
        System.out.println(atomicInteger.getAndIncrement());
        System.out.println(atomicInteger.get());
    }
}
