package Java.JDK8;
/*
Java8中最重要的两大改变:
一个是引入了Lambda表达式(方法引用是属于Lambda表达式的一种是实现)
另外一个就是引入了Stream API(说白了就是数据在传输的过程中可以做一系列"流水线式"的操作)
经过这些流水线式操作以后,原始的数据源是不会被改变的,而会生成一个新的数据流

Stream中有:Stream(串行流)和ParallelStream(并行流)两种
 */

import com.sun.tools.corba.se.idl.toJavaPortable.Stub;
import org.w3c.dom.ls.LSInput;
import sun.tools.jconsole.JConsole;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamAPI {

    public void demo1() {
        System.out.println("Build Stream");
        // 从数据源  ""创建流""  ,有很多种方式:
        // -------------------- 容器List的API
        List<String> list = new ArrayList<>();
        Stream<String> stringStream = list.stream();
        // -------------------- 数组Array的API
        String[] student = new String[5];
        Stream<String> stringStream1 = Arrays.stream(student);
        // -------------------- Stream类中的静态方法
        Stream<String> stringStream2 =
                Stream.of("MingyueXu","HaoyuShi","QidongSu");
        // -------------------- 创建"无限"流
        Stream.iterate(0,(item) -> item+2)
                .limit(10)
                .forEach(item -> {
                    try {
                        Thread.sleep(500);
                        System.out.print(item+" ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        System.out.println();
        // 那么比如说我不想要无限流,我只想要打印出10个偶数,只要加上.limit(10)即可
        // 这就是之前说到的流水线式的中间操作...

        // 除了通过"迭代"可以产生无限流,还有一种方式成为"生成"
        Random random = new Random();
        Stream.generate(() -> random.nextInt(20))
                .filter(item -> item<=10)
                .limit(10)
                .forEach(item -> {
                    try {
                        Thread.sleep(500);
                        System.out.print(item+" ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        System.out.println();
        // 比如说我们又想做一些中间操作,比如筛选出其中不大于10的数...
    }

    public void demo2() {
        System.out.println("Manipulations");
        // 中间的流水线式的一些常用的基本操作
        // 我们还是拿之前编写好的Student来尝试...
        List<Student> TestList = Student.list_student;
        Stream<Student> studentStream =
                TestList.stream();
        studentStream
                .filter(item -> item.score>=90)
                .map(item -> new Student(item.name, item.age, item.score-10))
                .limit(5) // 只要n个
                .skip(1) // 扔掉前n个
                .distinct() // 去重
                // ------------------------------------
                // 但是注意了,要想用.distinct()去重成功,
                // 必须重写Student类中的.HashCode()和.equals()方法
                // ------------------------------------
                .forEach(Student::printmethod);
        // 这里的filter,map这些称为是"中间操作"
        // forEach称为"终止操作"
        // "方法引用"的技巧,注意去分析一下这里其实是"类::实例方法名"
    }

    // 现在我们要实现的事情是将字符串中的字符一个个提取出来,并放到容器中
    public Stream<Character> demo3(String str) {
        List<Character> list3 = new ArrayList<>();
        for (Character item : str.toCharArray()) {
            list3.add(item);
        }
        return list3.stream();
    }

    public void printmethod1(Character character) {
        System.out.print(character+" ");
    }

    public void demo4() {
        System.out.println("Final Command");
        // Stream的终止操作有哪些
        List<Student> studentList4 = Student.list_student;
        // 我们先用之前学到的"方法引用"来看看具体的信息
        Consumer<Student> consumer =
                Student::printmethod;
        for (Student item : studentList4) {
            consumer.accept(item);
        }
        System.out.println("----------------------------");
        // 现在我们来看一下Stream中有哪些终止操作
        boolean result1 = studentList4
                .stream()
                .allMatch(student -> student.score<=90);
        // 是不是匹配所有元素
        System.out.print(result1+" ");

        boolean result2 = studentList4
                .stream()
                .anyMatch(student -> student.score<=90);
        // 是不是至少匹配一个元素
        System.out.print(result2+" ");

        boolean result3 = studentList4
                .stream()
                .noneMatch(student -> student.score<=90);
        // 是不是没有匹配的元素
        System.out.println(result3);

        Optional<Student> FirstOne = studentList4
                .stream()
                .sorted(Comparator.comparingDouble(s -> s.score))
                // 如果实在是不太理解,可以进到底层去看看.comparingDouble()
                .findFirst();
        // 注意.findFirst()得到的类型是Optional<Student>
        // 我们可以用.get()提取出Optional中的对象,并打印
        // 这里的排序默认是从小到大,所以FirstOne是最小的那个...
        consumer.accept(FirstOne.get());

        Optional<Student> LastOne = studentList4
                .stream()
                .max(Comparator.comparingDouble(s -> s.score));
        consumer.accept(LastOne.get());
        // 找到排序以后最大的那个

        System.out.println("--------------");
        Student finalresult = studentList4
                .stream()
                .reduce(new Student(),(s1,s2) -> {
                    if (s1 == s2) {
                        return s1;
                    } else {
                        return s2;
                    }
                });
        consumer.accept(finalresult);
        // 这里你要注意BiOperator接口的形式...
    }

    public void demo5() {
        List<Student> studentList5 = Student.list_student;
        Consumer<Student> consumer =
                Student::printmethod;

        // 我们一直在使用的都是串行流
        Optional<Student> result1 =
                studentList5
                .stream()
                .filter(student -> student.score>=95.00)
                .findAny();
        consumer.accept(result1.get());

        // 我们来看一下什么叫做"并行流"--ParallelStream
        // 本质上说是多个线程同时进行...
        Optional<Student> result2 =
                studentList5
                .parallelStream()
                .filter(student -> student.score>=95.00)
                .findAny();
        consumer.accept(result2.get());

        /*
        那么我们来说一下就运行结果而言体现的区别在哪里？
        之前我们使用串行流,所以运行的时候都是按照流的顺序,依次去filter然后find
        所以get出来的最终一定是MingyueXu,22,100.0
        但是如果我们转换的是并行流,那么是多个线程同时进行的filter和find
        所以,最终get到的是哪一个是随机性,完全取决于不同线程的执行速度...
         */
    }

    public void demo6() {
        List<Student> studentList6 = Student.list_student;
        List<String> list = studentList6
                .stream()
                .map(student -> student.name)
                .distinct()
                .collect(Collectors.toList());
        // Collectors工具类为我们提供了各种Collector接口的具体实现...
        System.out.println(list);
        // 但比如说我们想自己试试看能不能写Collector
        // 不行,至少用Lambda表达式是不行的,因为Collector并不是函数式借口
        // 除非采用匿名内部类的方式,重写其所有的抽象方法...
    }

    public static void main(String[] args) {
        StreamAPI streamAPI = new StreamAPI();
        streamAPI.demo1();
        System.out.println("--------------------");
        streamAPI.demo2();
        System.out.println("--------------------");
        // 接下去我们来引入flatMap
        List<String> stringList =
                new ArrayList<>(
                        Arrays.asList("Hello","world","love"));
        Stream<Stream<Character>> streamStream =
                stringList.stream()
                .map(streamAPI::demo3);
        // 这时候我们的到了一个"流中流",这显然不是我们想要的...
        // 比如我们想要遍历,就会变得很复杂
        streamStream
                .forEach(stream -> stream
                        .forEach(streamAPI::printmethod1));
        System.out.println();
        // 所以这个时候就是要用到flatMap
        // 他会把所有的值连接成一个流...
        Stream<Character> characterStream =
                stringList.stream()
                .flatMap(streamAPI::demo3);
        characterStream.forEach(streamAPI::printmethod1);
        System.out.println();
        System.out.println("-------------------------------");
        streamAPI.demo4();
        System.out.println("-------------------------------");
        streamAPI.demo5();
        System.out.println("-------------------------------");
        streamAPI.demo6();
    }
}

