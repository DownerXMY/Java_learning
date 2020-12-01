package Java.JVM;

import java.util.function.Predicate;

public class ClassLoaderApp4 {

    public static void main(String[] args) {

//        Parent4 parent4_1 = new Parent4();
        // 这显然是一个主动使用,所以当然会初始化,所以静态代码块也会被执行
//        Parent4 parent4_2 = new Parent4();
        // 当然静态代码块只会被执行一次,
        // 这就证明了初始化只会在首次主动使用的时候发生...
        // ------------------------
        // 我们注释掉之前的代码,然后看下面这个例子:
        Parent4[] parent4s = new Parent4[1];
        System.out.println(parent4s.getClass());
        // 唉？为什么最终却没有执行静态代码块呢？
        /*
        这就证明了上面这行代码并不表示对于类的主动使用
        然而我们知道有 new 关键字,那么肯定是创建了一个实例对象
        同时我们还知道的是这个实例对象不是Parent4的实例,
        那么究竟是什么呢?我们通过.getClass()打印出来看一下

        class [LJava.JVM.Parent4;

        但是我们好像没有在代码中声明过这个类,
        实际上是JVM在运行期间在底层帮我们做的事情...
         */
        Parent4[][] parent4s_1 = new Parent4[1][2];
        System.out.println(parent4s_1.getClass());

        System.out.println(parent4s.getClass().getSuperclass());
        System.out.println(parent4s_1.getClass().getSuperclass());
        // 对于数组实例来说,其类型是由JVM在运行期动态生成的,其父类均为
            // class java.lang.Object

        int[] ints = new int[1];
        System.out.println(ints.getClass());
        System.out.println(ints.getClass().getSuperclass());
    }
}

class Parent4 {
    static {
        System.out.println("Parent4 static block");
    }
}