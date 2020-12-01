package Java.JDK8;
/*
最后，我们来简单做一个Lambda表达式的应用
我们要实现的需求是:对数字进行运算
 */
public class LambdaApplicaiton {

    public void test1(Integer ori) {
        computing<Integer> computing = x -> {
            int result = x * 2;
            return result;
        };
        System.out.print(computing.method(ori));
        // 抽象方法.method()的具体实现已经通过Lambda表达式完成了...
    }

    public static void main(String[] args) {
        LambdaApplicaiton lambdaApplicaiton = new LambdaApplicaiton();
        System.out.print("result = ");
        lambdaApplicaiton.test1(12);
        System.out.println();
    }
}

@FunctionalInterface
interface computing<T> {
    public T method(T t);
}

/*
但是实际上Java8已经将绝大多数我们会用到的接口都内置了，
只有在极少数的情况下，我们会去自己创建新的接口.
Java的几大内置核心函数式接口:
1.Consumer<T>(消费型接口)
    里面有一个抽象方法.accept(T t),没有返回值，你可以用这个t,相当于"消费"它.
2.Supplier<T>(供给型接口)
    里面有一个抽象方法 T get(),不提供参数但是有返回值.
3.Function<T,R>(函数型接口)
    其中的抽象方法是 R apply(T t),相当于是传入一个参数,返回一个结果.
4.Predicate<T>(断言型接口)
    抽象方法是 boolean test(T t),相当于做判断.
---------------------------
当然还有很多别的类型的函数式接口,比如
BiFunction<T,U,R>
    传入两个参数T和U,返回一个结果R
BinaryOperator<T>
BiConsumer<T,U>
...
 */

