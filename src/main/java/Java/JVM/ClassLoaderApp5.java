package Java.JVM;
/*
现在我们来研究接口的初始化
 */

import java.util.Random;

public class ClassLoaderApp5 {

    public static void main(String[] args) {
        System.out.println(Child5.b);
        /*
        首先复习一下:如果这两个都是类而不是接口
        那么因为这里的变量都没有final修饰,所以两个类的初始化都会发生
        因为初始化一个类的子类也属于对类的主动使用
        -----------------------
        那么在分析之前,我们先做几件事情:
        1.
        那就是把Parent5的字节码文件删除,
        然后重新运行程序,发现依旧能够运行!
        然后我们把Child5的字节码文件也删除再试一下运行,
        居然还是可以正常运行...
        2.
        现在我们把这里的6改成new Random().nextInt(10);
        这时候删除字节码文件去运行就会报错了...
        3.
        我们再改一下程序:把前面赋给a的值也改成new Random().nextInt(3);
        运行,然后删掉Parent5的字节码文件,然后再次运行
        程序居然报错了...
        (按理说我们打印的是Child5.b对吧,怎么删掉Parent5的字节码文件会出问题呢?)
        4.
        我们最后做了一件事情,
        那就是将Child5中的b又变回为一个常量6,
        然后运行,删除Parent5的字节码文件,再次运行
        发现程序是没有报错的
        -----------------------
        换句话说,只有当Child5中的常量是不能在编译阶段被确定的时候,
        Parent5的字节码文件是不能被删除的...
        -----------------------
        最后我们来解释这些事情的原因:
        1.首先对于接口来说,其中的变量都是默认public,static,final的
            所以其实我们说这里的5,6,其实都是常量
        2.改成new Random()其实也是因为常量的值不能够在编译阶段被确定下来
        3.接口的初始化还是会导致其父接口的初始化,但是这并不绝对
            可以想像,因为b = new Random().nextInt(10);
            所以在打印Child5.b的时候肯定是有接口Child5的初始化的
            从而就有父接口Parent5的初始化,所以删掉字节码文件是不行的
        */
    }
}

interface Parent5 {
    public static int a = 5;
}

interface Child5 extends Parent5 {
    public static int b = new Random().nextInt(10);
}
