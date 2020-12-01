package Java.Dynamic;
/*
什么叫做Java注解:
实际上"注解"在很多语言中被称为"修饰器"或者"装饰器",
比如我们写匿名内部类的时候,方法重构之前加上的"@Override"
再比如我们之前接触过的,写函数式接口的时候加上的"@FunctionalInterface"
...
---------------------
Java􏰯􏲥􏰣􏱍􏰇􏰎􏰺􏱟􏰐􏲄内置了一些注解,比如
"@Override"􏳐 -- 表示子类重写父类的方法
"@Deprecated" -- 表示方法已经过时,开发者不应该使用,但不是强制性的建议
"@SuppressWarnings􏲏" -- 表示压制Java的某种编译警告,必须声明压制的是那种警告
---------------------
Java的内置注解是比较少的
我们日常开发的时候,基本上都是用自定义的注解,
这才是我们真正要学习的东西...
---------------------
注解似乎有某种神奇的力量,通过简单的声明就能达到很复杂的效果,
它有点类似于Java并发中的synchronized关键字,
实际上很多时候底层帮助我们做了很多事...
*/

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Annotation1 {
    /*
    为了了解注解是怎么定义的,以及之后我们怎么创建自定义的注解
    我们先来看一下最熟悉的@Override

    -----------------
    import java.lang.annotation.*;

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Override {}
    -----------------
    1.注解也用到了Interface,只不过前面多了个@
    2.另外,前面的两个@Target和@Retention称为"元注解",被用于定义注解本身
        @Target表示注解的目标,目标可以有多个(比如"@SuppressWarnings")
            如果没有申明Target,则默认全部
        @Retention表示注解信息保留到什么时候,只有三个可选项:
            RetentionPolicy.SOURCE
                -- 只在源代码中保留,编译器将代码编译成字节码文件以后就会丢弃
            RetentionPolicy.CLASS
                -- 保留到字节码文件中,但是不一定会被JVM保存到内训中
            RetentionPolicy.RUNTIME
                -- 一直保留到运行时
            注意如果没有申明Retention,则默认.CLASS
            当然我们还得说明元注解并非只有这么两个,随着注解变复杂,就需要更多的元注解
    3.我们在"@SuppressWarnings"中看到了String[] value();
      这是在注解中定义的方法,但是本质上它的作用是为注解提供额外的参数
      注解内的参数不是什么都可以的,合法的包括:
      基本类型,String,Class,注解本身,以及这些类型的数组
      参数定义时,可以用 default 给出一个默认值,比如"@Inject"
    4.虽然也是有Interface,但是注意注解是不能继承的,
      不过注解却有一个和"继承"有关的元注解"@Inherited"
    */

    // @Interited
    // 下面是第一个我们自定义的注解...
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    static @interface Test {}

    // 让我们来用一下@Test
    @Test
    static class Base {}

    static class Child extends Base {}

    public static void main(String[] args) {
        /*
        @Test是一个注解,
        类Base有这个注解,
        Child继承了Base,
        那么哪怕Child没有声明@Test注解,
        @Inherited注解也能保证Child类有@Test注解
        ---------------
        所以本质上说注解的继承依赖两方面,缺一不可:
        1.注解必须有元注解"@Inherited"
        2.类必须具有继承关系
        ---------------
         */
        System.out.println(
                Child.class.isAnnotationPresent(Test.class)
        );
    }
}
