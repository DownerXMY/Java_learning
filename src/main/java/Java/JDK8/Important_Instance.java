package Java.JDK8;
/*
我们要将在LambdaApp.java.class中的最优的解决方案做一个最好的总结
实际上这是一个非常好的例子，综合使用了接口，Lambda表达式，抽象方法的重载...
首先我们具体要实现什么需求:
我们要将Student类中学生列表，按照不同需求进行筛选
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Important_Instance {

    public static List<String> printmethod(List<Student> list) {
        List<String> result = new ArrayList<>();
        for (Student student : list) {
            result.add(student.name+" "+student.age+" "+student.score);
        }
        return result;
    }

    public static void main(String[] args) {
        // 获取到全部的学生信息
        List<Student> studentList = Student.list_student;
        // 通过Lambda表达式实现我们想要的筛选效果

        // 获取成绩不低于95分的学生
        List<Student> result =
                new Student()
                .filter_student_by_condition(
                        studentList,
                        (e) -> e.score>=95.0
                );
        /*
        --------------------------------------------
        这里是第二个核心点:
        当然我们知道接口中的judge方法必须重载(给定我们希望的判断条件),
        那么这里就是通过Lambda表达式实现的...
        换句话说，虽然你没有看见我们去override接口中的judge()方法
        但是其实都已经出现在了Lambda表达式中了
        这实际上也是为什么我们可以直接把接口传入具体方法中，而不用担心还没实现抽象方法的原因!!!
        --------------------------------------------
         */
        System.out.println(printmethod(result));
        System.out.println("------------------------------");

        // 获取年龄不大于21岁的学生
        List<Student> result1 =
                new Student()
                .filter_student_by_condition(
                        studentList,
                        (e) -> e.age<=21
                );
        System.out.println(printmethod(result1));
        System.out.println("-------------------------------");

        // 想让所有的学生成绩都减10分
        List<Student> result2 =
                new Student()
                .change_information_by_condition(
                        studentList,
                        (e) -> e.score-10.00
                );
        System.out.println(printmethod(result2));
        // 最后我们要说一句，Lambda表达式可以完成远远超过这样层次的逻辑实现
        // 后面我们会慢慢学习...
        System.out.println("--------------------------------");
        // 之前说的都很重要，但是我们tm还有更加强大的Stream API
        // 假设除了list_student之外什么都没有
        // 接口，实现类，过滤方法什么都没写...
        studentList.stream()
                .filter((e) -> e.score >= 95)
                .forEach(item -> {
                    System.out.println(
                            item.name+" "+item.age+" "+item.score);
                });
        studentList.stream()
                .map(student -> student.name)
                .forEach(System.out::print);
        System.out.println();
        // 想干什么都可以，为所欲为!!!
        // 就问你Java8牛不牛???!!!
    }
}

class Student {
    public String name;
    public int age;
    public double score;

    Student() {}

    Student(String name,int age,double score) {
        this.name = name;
        this.age = age;
        this.score = score;
    }

    public void printmethod() {
        System.out.println(
                this.name+" is of age "+this.age+" and scores "+this.score
        );
    }
    public static List<Student> list_student =
            Arrays.asList(
                    new Student("MingyueXu",22,100),
                    new Student("MingyueXu",22,100),
                    new Student("HaoyuShi",23,98),
                    new Student("HaoyuShi",23,98),
                    new Student("QianruYu",21,88),
                    new Student("QidongSu",20,92),
                    new Student("HaoqiYuan",22,96)
            );

    public List<Student> filter_student_by_condition(
            List<Student> list,
            judge_interface<Student> judgeInterface) {
        /*
        --------------------------------------------
        这里是第一个核心点，那就是"把Interface传入方法"!!!
        为什么能够把接口(抽象方法)穿进去，在后面我们会说...
        --------------------------------------------
         */
        List<Student> result = new ArrayList<>();
        for (Student student : list) {
            if (judgeInterface.judge(student)) {
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> change_information_by_condition(
            List<Student> list,
            change_interface<Student> changeInterface
    ) {
        List<Student> result = new ArrayList<>();
        for (Student student : list) {
            result.add(
                    new Student(
                            student.name,
                            student.age,
                            changeInterface.change(student))
            );
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age &&
                Double.compare(student.score, score) == 0 &&
                Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, score);
    }
}

interface judge_interface<T> {
    public boolean judge(T t);
}

interface change_interface<T> {
    public double change(T t);
}