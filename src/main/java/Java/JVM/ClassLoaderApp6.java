package Java.JVM;

public class ClassLoaderApp6 {

    public static void main(String[] args) {
        Singleton singleton = Singleton.getInstance();
        System.out.println(Singleton.counter1);
        System.out.println(Singleton.counter2);
        System.out.println("-----------------------");
        Singleton2 singleton2 = Singleton2.getInstance();
        System.out.println(Singleton2.counter1);
        System.out.println(Singleton2.counter2);
    }
}

class Singleton {

    public static int counter1;
    public static int counter2 = 0;

    private static Singleton singleton = new Singleton();

    private Singleton() {
        counter1++;
        counter2++;
    }

    public static Singleton getInstance() {
        return singleton;
    }
}

class Singleton2 {
    public static int counter1;

    private static Singleton2 singleton2 = new Singleton2();

    private Singleton2() {
        counter1++;
        counter2++;
    }

    public static int counter2 = 0;

    public static Singleton2 getInstance() {
        return singleton2;
    }
}

/*
我们运行后发现两个counter的结果都是1
这一点应该是能够分析出来的,但是我们现在调换一下代码顺序
把counter2的定义放到构造方法下面去
惊呆了,打印出来的counter2的结果竟然变成了0
-------------------------
1.调用静态方法getInstance()表示我们对Singleton2做了初始化
2.初始化的顺序是按照代码的顺序从上之下的
    换言之,执行到构造方法的时候,其实两个counter都变成了1,
    问题就在于后面一句代码又把counter2变回到0去了...
 */