package Java.JDK8;

/*
在Java8中引入了一个新的操作符 "->",将整个Lambda表达式分成两个部分...
"->"左侧:Lambda表达式的参数列表(实现接口中抽象方法的参数)
"->"右侧:Lambda表达式中所需要执行的功能,成为"Lambda体"(实现接口中抽象方法的具体实现)
之前我们已经想明白了,Lambda表达式就是对接口的实现,但是随之而来就有一个很重要的问题:
---------------------
如果说一个接口中有很多个抽象方法,那么Lambda表达式究竟是实现了哪一个呢？
(Lambda表达式需要"函数式接口"(只有一个抽象方法的接口)的支持...
换句话说,上面提到的问题不能被解决)
比如我们以后想要写函数式接口,可以在前面加上一个装饰器
@FunctionalInterface
这样子如果写了多个抽象方法,会报错!
---------------------
为什么Lambda的参数列表里不需要写数据类型？
(因为Java的JVM编译器可以根据上下文推断出数据类型...)
---------------------
 */

import java.util.Comparator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class LambdaExpression {
    private static Lock lock = new ReentrantLock();
    int id = 1;

    public Thread runnableExe() throws InterruptedException {
        Thread t = new Thread(() -> {
            IntStream.rangeClosed(1,5).forEach(i -> {
                System.out.print("Hello id ");
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(id++);
                System.out.println();
                // 为什么输入id++会报错呢？
                // 具体方法引用同级别的变量必须是final的，
                // 换句话说，最正确的写法其实是final int id = 1;
                // 那么最简单的处理就是提高变量的级别...
            });
        });
        t.start();
        return t;
    }

        // 在这个很熟悉的例子中，我们一下子看到了两种Lambda表达式的情况
        // 有参数无返回值，无参数无返回值

    public void test1() {
        Consumer<String> consumer =
                (x) -> System.out.println(x);
        consumer.accept("My name is Downer!");
    }

    public void test2(Integer ori) {
        Consumer<Integer> consumer2 =
                item -> System.out.println(item+1);
        consumer2.accept(ori);
    }

    public void test3(Integer ori1, Integer ori2) {
        // 如果Lambda体中有多条语句,必须使用大括号...
        Comparator<Integer> comparator = (x,y) -> {
            Integer x_up = x-1;
            return Integer.compare(x_up,y);
        };
        System.out.println(
                (comparator.compare(ori1,ori2) == -1) ? "smaller" :
                        (comparator.compare(ori1,ori2) == 0) ? "equal" :
                "bigger");
        // 看清楚上面这个表达方式，非常难学但是非常酷...
    }

    public void test4() {
        Comparator<String> comparator2 =
                (s1,s2) -> Integer.compare(s1.length(),s2.length());
        System.out.println(comparator2.compare("Hello","Mingyue"));
    }

    public static void main(String[] args) throws InterruptedException {
        LambdaExpression lambdaExpression = new LambdaExpression();
        Thread current = lambdaExpression.runnableExe();
        Thread.sleep(1000);
        current.join();
        // 在考虑Main-Thread和Child-Thread的先后执行的逻辑时，
        // 千万别去想Lock,Condition之类的，先去考虑.join()
        System.out.println("--------------------------------");

        // 无参数无返回值
        Runnable r1 = () -> System.out.println("Hello Lambda");
        r1.run();
        System.out.println("--------------------------------");

        // 有参数无返回值
        lambdaExpression.test1();
        // 实际上如果Lambda表达式只有一个入参,那么小括号可以省略...
        lambdaExpression.test2(11);
        System.out.println("--------------------------------");

        // 有多个参数有返回值
        System.out.println("Lambda体有多条语句");
        lambdaExpression.test3(4,9);
        lambdaExpression.test3(12,11);
        lambdaExpression.test3(14,8);
        // 如果Lambda体中只有一条语句,那么return和大括号都可以省略...
        System.out.println("Lambda体中只有一条语句");
        lambdaExpression.test4();
        System.out.println("---------------------------------");

    }
}
