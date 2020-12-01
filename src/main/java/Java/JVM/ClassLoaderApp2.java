package Java.JVM;

public class ClassLoaderApp2 {

    public static void main(String[] args) {
        System.out.println(Parent2.m);
    }
}

class Parent2 {
    public static final String str = "parent";
    // 我们仅仅只是加了一个final关键字,事情就变化了
    // 静态代码块中的内容不再被执行了...
    /*
    下面这些话非常重要:
    ---------------------
    因为我们知道final表示定义的变量实际上是一个常量,
    那么Java自然也是知道的,所以在编译阶段,这个常量就会被存入到:
    "调用这个常量的那个方法所在的类的常量池中去"
    这是需要理解的,
    在这个例子中str是被存到了ClassLoaderApp2的常量池中去了!
    在这之后,ClassLoaderApp2和Parent2之间就不再有任何关系了
    --------
    甚至不客气地说,我们可以删除Parent2的.class文件
    比如我们先跑一次,
    然后可以真的去target文件夹下把Parent2的字节码文件删除,
    再运行程序,发现程序还是能够被打印出来的...
    --------
    所以本质上,并没有直接引用到定义常量的类,也就是这里的Parent2
    所以也不会触发这个类的初始化,那么自然静态代码块也不会被执行!
    --------------------
     */

    public static final short s = 7;
    public static final int i = 128;
    public static final int m = 1;
    static {
        System.out.println("Parent2 static block");
    }
}
