package Java.JVM.GC;
/*
我们来试试看G1收集器...
-XX:MaxGCPauseMillis=200m
  表示我们能够接受的最大GC停顿时间是200毫秒
 */

public class G1App {

    public static void main(String[] args) {
        int size = 1024 * 1024;

        byte[] bytes1 = new byte[size];
        byte[] bytes2 = new byte[size];
        byte[] bytes3 = new byte[size];
        byte[] bytes4 = new byte[size];

        System.out.println("Hello world...");
    }
}
