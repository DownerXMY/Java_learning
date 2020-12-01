package Java.concurrent;

public class ThreadLocalApp {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new InheritableThreadLocal();
        threadLocal.set("Hello");
        String result1 = threadLocal.get();
        System.out.println(result1);
    }
}
