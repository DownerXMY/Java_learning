package Java;

import java.lang.Math;

public class finalApp {
    int a = 1;

    // 成员式内部类
    public class My1 {
        // 如果修饰符为static，则为类级，否则为对象级
        // 内部类不能有static
        int sum(int b) {
            return a + b;
        }
    }

    void testfunc() {
        My1 my1 = new My1();
        System.out.println(my1.sum(3));
    }

    public class My2 {
        String append(String c) {
            return Integer.toString(a) + c;
        }
    }

    // 我们来看一下怎么在函数块中来定义类：
    void Myfunc() {
        final int cons = 10;
        String str = "hello world";
        class Inner {
            void innertest() {
                System.out.println(a);
                System.out.println(str);
                System.out.println(cons);
            }
        }
        Inner inner = new Inner();
        inner.innertest();
    }

    public static void main(String[] args) {
        // final
        // final修饰的类不能被继承.
        // final修饰的方法不能被重写.
        // final修饰的变量不能被再次赋值(即常量).
        // final修饰的局部变量可以只声明不赋值，但后续只能被一次性赋值.
        // 一个很直接的例子就是java.lang.Math
        System.out.println("'final' is simple!");
        // 如何实例化一个内部类
        finalApp app = new finalApp();
        finalApp.My2 my2 = app.new My2();
        System.out.println(my2.append("sss"));
        app.Myfunc();

        // 实例化抽象类，这非常有趣！
        // 另外一句话，抽象类本身不能被实例化
        Person P = new Child();
        P.eat();
    }
}

// 让我们来看看什么叫做抽象类(匿名类)
abstract class Person {
    public abstract void eat();
}

class Child extends Person {
    // 注意抽象类中是什么样的，子类中也必须一样，比如这里的public不能少
    public void eat() {
        System.out.println("eat something...");
    }
}