package Java.JDK8;
/*
方法引用是Lambda表达式的另外一种实现
方法引用主要有三种语法格式:
------------------------------
对象::实例方法名
类::静态方法名
类::实例方法名(比较特殊)
------------------------------
注意"方法引用"是要整个替代Lambda表达式,而不是只替代Lambda体
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;

public class MethodReference {

    public void test() {
        BiConsumer<String,Integer> consumer =
                (str1,int1) -> System.out.println(
                        str1+" is of age "+int1
                );
        consumer.accept("MingyueXu",22);
    }

    public void test2() {
        // Lambda表达式:
        Comparator<Integer> integerComparator =
                (x,y) -> Integer.compare(x,y);
        int result1 = integerComparator.compare(11,8);
        System.out.print(result1+ " ");
        // 方法引用
        Comparator<Integer> integerComparator1 =
                Integer::compare;
        // 注意这里Integer是类,compare是静态方法...
        int result2 = integerComparator1.compare(9,13);
        System.out.print(result2);
    }

    public void test3() {
        // 比如现在我们要实现的需求是比较两个字符串是否相同
        // 在我们对"方法引用"还不是很熟悉的时候,可以先写出Lambda表达式试试看
        BiPredicate<String,String> biPredicate =
                (Str1,Str2) -> Str1.equals(Str2);
        boolean result = biPredicate.test("Hello","World");
        System.out.println(result);
        // 那么接下去思考怎么用"方法引用"来实现:
        BiPredicate<String,String> biPredicate1 =
                String::equals;
        // 注意这里的.equals()是一个实例方法,并不是静态方法
        /*
        但是这里我们肯定会冒出来一个问题:
        这里"方法引用"的参数情况和我们之前的认识不一样了
        .equals()只接受一个参数,但是我们前面是<String,String>
        这是怎么回事呢？
        -----------------------------
        其实这就是"类::实例方法名"这种方法引用的要求:
        第一个参数是这个实例方法的调用者，
        第二个参数是这个实例方法的入参.
        -----------------------------
         */
        boolean result2 = biPredicate1.test("Hello","Hello");
        System.out.println(result2);
    }

    public void test4() {
        // 比如我们还是用自己创建的People类...
        // 还是一样,如果不太会可以先看看Lambda表达式的样子
        BiFunction<String,String,People> biFunction =
                (str1,str2) -> new People(str1,str2);
        People HaoyuShi =
                biFunction.apply("HaoyuShi","profession");
        HaoyuShi.printmethod(HaoyuShi.name, HaoyuShi.work);

        // 再来试试看"方法引用":
        BiFunction<String,String,People> biFunction1 =
                People::new;
        // 非常神奇,其实不是很好理解的...
        // 构造器方法叫做"new"!!!
        // 假设类中有很多种构造器，那么方法引用会选择其中"和当下环境参数匹配的"构造器.
        People QidongSu =
                biFunction1.apply("QidongSu","OS-Engineering");
        QidongSu.printmethod(QidongSu.name, QidongSu.work);
    }

    public void test5() {
        // 这次我们直接写"方法引用",不要在先写Lambda表达式
        Function<Integer, List<String>> function =
                ArrayList::new;
        List<String> list = function.apply(10);
        List<String> list1 = function.apply(10);
        /*
        那么这到底是什么意思呢,究竟调用的是哪一个"new"呢？
        我们直接点进new中去看就知道了,
        这里传入的Integer实际上是List的初始长度
         */
        IntStream.rangeClosed(1,10).forEach(i -> list.add("Id"+i));
        System.out.println(list);
        // 或者:

        IntStream.rangeClosed(1,10)
                .mapToObj(item -> "ID"+Integer.toString(item))
                .forEach(list1::add);
        System.out.println(list1);
        // 所以说Stream-API很大程度上是为了分布式计算引入的...
    }

    public static void main(String[] args) {
        MethodReference methodReference = new MethodReference();
        methodReference.test();
        // 一个最简单的"方法引用"是:
        IntStream.rangeClosed(1,5).forEach(System.out::print);
        System.out.println();
        System.out.println("-----------------------------");

        // 我们来看一下"类::静态方法名"
        methodReference.test2();
        System.out.println();
        // 我们也可以自己创建一个类来试试看:
        new People().printmethod("MintyueXu","Hadoop-Engineering");
        // 上面这种是普通的调用,下面是调用"方法引用"
        BiConsumer<String,String> consumer2 =
                People::printmethod;
        /*
        要看明白,"方法引用"是整一个替代了Lambda表达式
        那么有人会问:不需要传入参数吗？
        实际上参数是在BiConsumer中已经声明了,
        换句话说,在这里"方法引用"的方法必须是有两个String类型的入参的...
         */
        consumer2.accept("MingyueXu","Hadoop-Engineering");
        System.out.println("-------------------------------");

        // 最后我们来看一下最为特殊的一种
        // 类::实例方法名
        methodReference.test3();

        // 我们来看看看什么叫做"构造器引用"
        // 类::构造器(实际上就是"创建对象"的意思)
        methodReference.test4();
        System.out.println("-------------------------------");

        // 实际上"方法引用"的例子是很多的,比如我们来看看下面的"数组引用"
        methodReference.test5();
    }
}

class People {
    public String name;
    public String work;

    People(){}

    People(String name1,String work1) {
        this.name = name1;
        this.work = work1;
    }

    public static void printmethod(
            String name1,
            String work1
    ) {
        System.out.println(
                name1+" is working in "+work1
        );
    }
}