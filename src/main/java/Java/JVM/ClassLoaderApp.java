package Java.JVM;
/*
主动使用 VS 被动使用
 */
public class ClassLoaderApp {

    public static void main(String[] args) {
        System.out.println(Child1.str);
        // 对于静态字段来说,只有直接定义了改字段的类才会被初始化
        // 这里发生了对Parent1的主动使用,但是并没有对Child1的主动使用
        System.out.println("-------------------");
        // 跑下面一句代码时请注释掉上面那句!!!
        System.out.println(Child1.str2);
        /*
        那么这里显然是发生了对Child1的主动使用
        但是注意,这个时候我们已经初始化了Child1,
        那么其父类Parent1同样也被主动使用了,所以也被初始化了,
        所以Parent1静态代码块中的打印也同样被执行了!
        */
        /*
        好那么这个时候问题来了,在之前那句代码中,
        我们说Child1没有被主动使用,那么他究竟有没有被调用呢?
        那么实际上我们可以采用一些JVM上的手段查看有没有被调用...
        -XX:+TraceClassLoading (用于追踪类的加载信息并打印)
        这是一个JVM参数!
        再次打印以后我们就能知道其实JVM确实是加载了Child1这个类,但是没有初始化
         */
    }
}

class Parent1 {
    public static String str = "Hello World!";
    static {
        System.out.println("Parent1 static block...");
    }
}

class Child1 extends Parent1 {
    public static String str2 = "Welcome!";
    static {
        System.out.println("Child1 static block");
        // 我们看到这句话是不会执行的...
    }
}