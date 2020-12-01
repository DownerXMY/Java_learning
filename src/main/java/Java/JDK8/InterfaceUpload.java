package Java.JDK8;
/*
我们知道,接口中一般都是抽象方法,
但是在Java8中,接口中开始支持有实现了的方法(默认方法)以及静态方法
 */

/*
随之而来的问题也就产生了,
1.如果一个类和一个接口中都有相同名字具体方法的实现,
然后我们同时继承了这个类,实现了接口,
那么调用方法的时候,究竟怎么办呢?
   ---答案是"类优先"原则
2.如果有好几个接口同时都有名字相同的具体实现的方法呢？
   ---答案是报错了
   ---那就意味着我们需要重写其中一个方法
   ---格式: 想要的那个接口名.super.具体方法名
 */

class testClass extends MyClass implements MyInterface {}

class testClass2 implements MyInterface,MyInterface2 {
    @Override
    public String getName() {
        return MyInterface2.super.getName();
    }
}

class testClass3 implements MyInterface3 {
    public void test() {
        // 因为static的属性,所以可以直接这么用...
        MyInterface3.show();
    }
}

public class InterfaceUpload {
    public static void main(String[] args) {
        testClass testClass = new testClass();
        String name = testClass.getName();
        System.out.println(name);
        // 究竟打出来的是哪个getName()呢？
        System.out.println("--------------------");
        String name2 = new testClass2().getName();
        System.out.println(name2);
        System.out.println("--------------------");
        new testClass3().test();
    }
}

interface MyInterface {
    // 以前在接口中值允许有"全局静态常量"和"抽象方法"这两个东西
    // 但是在Java8以后,事情就变得不一样了
    default String getName() {
        return "MingyueXu";
    }
    // 可以看到这是一个已经实现了的方法,
    // 但是在接口中我们会发现需要一个default修饰,
    // 这就是所谓的"默认方法"...
    /*
    那么一个很直观的问题是,为什么要设计出这样的功能呢？
    大概是处于一下两点的考虑(我们的猜测):
    1.首先就是Java8中引入了Lambda表达式,但是我们知道只能用在函数式接口上,
      换句话说,对于那些有很多抽象方法的接口,Lambda表达式就"照顾"不到,
      那么这肯定不是我们想要的,所以就会想,要是我们能够把接口中那些不太重要的抽象方法实现了
      只留下一个抽象方法,那不就能够被Lambda表达式应用了吗？
    2.另外一个原因就是:我们知道有一个Collector接口,
      但同时Java还提供了Collectors类为我们提供各种具体的实现,
      那么工程师们就会去想,我为什么不把他们写到一起去呢?
     */
}

interface MyInterface2 {
    default String getName() {
        return "HaoyuShi";
    }
}

interface MyInterface3 {
    // Java8中的接口不仅可以有默认方法,还可以有静态方法
    public static void show() {
        System.out.println("This is a Static Method");
    }
}

class MyClass {
    public String getName() {
        return "Downer";
    }
}