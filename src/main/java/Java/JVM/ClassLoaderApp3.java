package Java.JVM;

import java.util.UUID;

public class ClassLoaderApp3 {

    public static void main(String[] args) {
        System.out.println(Parent3.str);
        /*
        有意思的事情来了,我们之前不是说final确定了这是个常量,
        然后所以Parent3其实是不会被初始化的吗？
        但这里却打印出了静态代码块,显然是被初始化了,为什么呢?
        ----------------
        首先要明确一点就是final修饰了以后确实是一个"常量",这一点毋庸置疑
        然而我们现在要说的关键在于"这个常量的值能否在编译阶段被确定下来"
        显然在我们这个例子中,这个常量str的值,是在运行的时候才能确定的
        那么就不会被存到之前说的常量池中,
        这就会导致目标类被初始化!
         */
    }
}

class Parent3 {
    public static final String str =
            UUID.randomUUID().toString();

    static {
        System.out.println("Parent3 static block");
    }
}
