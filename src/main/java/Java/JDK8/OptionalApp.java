package Java.JDK8;
/*
当然除了Lambda表达式和强大的Stream-API之外，
Java8还引入了很多很不错的应用级组件
比如之前介绍的"方法引用",以及我们现在要介绍的"Optional类"
--------------------------------------
其引入的目的是为了减少"空指针异常"
Optional类是一个容器类,代表一个值存在或者不存在
--------------------------------------
 */

import jdk.nashorn.internal.runtime.options.Option;

import java.util.Optional;
import java.util.function.Consumer;

public class OptionalApp {

    public void demo1() {
        Optional<Student> optionalStudent =
                Optional.of(new Student(
                        "MingyueXu",22,100.0));
        Consumer<Student> consumer =
                Student::printmethod;
        consumer.accept(optionalStudent.get());
        // --------------------
        /*
        Optional<Student> optionalStudent1 =
                Optional.of(null);
        */
        // 注意这么写会报空指针异常,那么问题就来了,我们不是为了解决空指针异常吗？
        // 注意我们说的Optional的优势,其实是帮助我们快速锁定"空指针异常"发生的地方
    }

    public void demo2() {
        // 但是有的时候我们就是想构建一个null,那怎么办呢？
        // Optional为我们提功了多种别的方式...
        Optional<Student> optionalStudent1 = Optional.empty();
        Optional<Student> optionalStudent2 =
                Optional.ofNullable(null);
    }

    public void demo3() {
        Optional<Student> optionalStudent =
                Optional.ofNullable(null);
        Student student =
                optionalStudent.orElseGet(() -> new Student(
                "HaoyuShi",23,99
        ));
        Consumer<Student> studentConsumer =
                Student::printmethod;
        studentConsumer.accept(student);
    }

    public static void main(String[] args) {
        OptionalApp optionalApp = new OptionalApp();
        optionalApp.demo1();
        optionalApp.demo2();
        optionalApp.demo3();
    }
}
