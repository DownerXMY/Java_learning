package Java;

import java.util.stream.IntStream;

public class exceptionApp {

    // exception: try / catch / throw / throws / finally
    // All exceptions:
    //              --- Exception => RuntimeException(被零除，非法数组索引...)
    // Throwable => |
    //              --- Error(通常是灾难性的错误，不是程序能够控制的)
    public static void main(String[] args) {
        // 我们先来简单看一下try和catch的使用
        int d,a;
        try {
            d = 0;
            a = 42 / d;
            System.out.println("This will not be printed");
        } catch (ArithmeticException e) {
            // 那么除了捕捉到异常，如果我们还想看看异常的描述，怎么办呢？
            System.out.println("Exception:"+e);
            System.out.println("Division by zero!");
        }
        System.out.println("After catch statement...");
        // 从打印的结果来看，一旦异常发生，马上从try转到catch
        // 永远不会从catch转到try
        // 实际上很好理解，try-catch的目的就是保护程序不被异常中断，并同时还能够检测捕捉到异常
        System.out.println("---------------------------------");

        // 多重catch语句
        try {
            int c = args.length;
            System.out.println("c = "+c);
            int b = 42 / c;
            int e[] = {1};
            e[42] = 99;
        } catch (ArithmeticException e) {
            System.out.println("Division by zero: "+e);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array index oob: "+e);
        }
        System.out.println("After catches...");

        // 异常不会到达的错误
        // 注意catch的时候，子类异常必须写在父类之前，否则不会到达
//        try {
//            int a1 = 0;
//            int b1 = 42 / a1;
//        } catch (Exception e) {
//            System.out.println("Father Exception");
//        } catch (ArithmeticException e) {
//            System.out.println("This will never be printed");
//        }
        // 你会发现上面代码是错误的.
        System.out.println("-------------------------------");

        // try的嵌套
        // 我们分别来看一看在"不传入参数","a2=1","a2=2"的情况下的catch结果
        System.out.println("no parameter:");
        try {
            int a2 = args.length;
            int b2 = 42 / a2;
            System.out.println("a2= "+a2);
            try {
                if (a2==1) {a2 = a2/(a2-a2);}
                if (a2==2) {int c2[] = {1};}
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Exception: "+e);
            }
        } catch (ArithmeticException e) {
            System.out.println("Exception: "+e);
        }
        System.out.println("~~~~~~~~~~~~~~");
        for (int item : IntStream.rangeClosed(1,2).toArray()) {
            System.out.println("a2 = "+item+":");
            try {
                int a2 = item;
                int b2 = 42 / a2;
                System.out.println("a2 = "+a2);
                try {
                    if (a2==0) {
                        int d2[] = {1};
                        d2[42] = 99;
                    }
                    if (a2==1) {a2 = a2/(a2-a2);}
                    if (a2==2) {
                        int c2[] = {1};
                        c2[42] = 99;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Exception: "+e);
                }
            } catch (ArithmeticException e) {
                System.out.println("Exception: "+e);
            }
            System.out.println("~~~~~~~~~~~~~~");
        }
        // 那么依据结果中我们总结如下:
        // 如果外面的try中已经发生异常，直接跳到catch，并且不会再返回内部的try；
        // 否则继续检查内部的try

        try {
            demo();
        } catch (NullPointerException e) {
            System.out.println("Caught outside: "+e);
        }
        // 换句话说，抛出异常，可以理解为我们人为地去搞出一个异常

        try {
            throwOne();
        } catch (IllegalAccessException e) {
            System.out.println("Caught: "+e);
        }

        // finally
        demo2();
    }

    // 异常的抛出(throw)
    public static void demo() {
        try {
            throw new NullPointerException("demo");
            // 注意这句话是实例化异常的方式
        } catch (NullPointerException e) {
            System.out.println("Caught inside");
            throw e;
        }
    }

    // throws
    // 用throws字句列出一个方法可以抛出的所有异常的类型，可以是一个异常列表
    // 那么之前为什么就可以呢？
    // 因为之前我们catch了，换句话说throws适用的情况就是你不想catch它
    static void throwOne() throws IllegalAccessException{
        System.out.println("inside throwOne");
        throw new IllegalAccessException("demo1");
    }

    // finally
    // 有一种情况:比如一个方法是打开一个文件并关闭
    // 现在中途出了异常，那么伴随而来的就是文件不能关闭了
    // 这就很难受，finally的引入就是为了解决这样的问题
    // finally语句块永远会被执行!!!
    static void demo2() {
        try {
            int a = 0;
            int b = 42 / a;
        } catch (ArithmeticException e) {
            System.out.println("Exception:"+e);
        } finally {
            System.out.println("This is always able to run...");
        }
    }
}
