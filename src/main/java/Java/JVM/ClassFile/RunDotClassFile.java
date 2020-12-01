package Java.JVM.ClassFile;

public class RunDotClassFile {

    public static void demo() {
        System.out.println("static demo");
    }

    public void demo2(GrandFather grandFather) {
        System.out.println("GrandFather...");
    }
    public void demo2(Father father) {
        System.out.println("Father...");
    }

    public void demo2(Son son) {
        System.out.println("Son...");
    }

    public static void main(String[] args) {
        demo();
        GrandFather p1 = new Father();
        GrandFather p2 = new Son();
        RunDotClassFile runDotClassFile = new RunDotClassFile();
        runDotClassFile.demo2(p1);
        runDotClassFile.demo2(p2);
        System.out.println(p1.getClass());
        System.out.println(p2.getClass());
        /*
        所以实际上下面两个重写的demo2()方法根本没有被调用...
        这里就涉及方法的"静态分派"(dispatch)
        我们首先要声明一点,那就是p1的静态类型是GrandFather,而实际指向的类型是Father
        变量本身的静态类型在编译阶段是确定的(这里确定的含义是比较难理解的),
        哪怕是我们用装箱进行向下类型的转换,比如(GrandFather)p1,
        也只是静态类型在使用的时候发生了改变,而且最重要的是:
        ----------------
        静态类型的改变一定是编译阶段可知的!!!
        这就是之前说的静态类型在编译阶段是"确定的"的含义...
        ----------------
        这是"Java多态"的一种体现
        那么我们也看到了实际类型是可以被代码所改变的,所以是只有在运行期间才能确定的...
        ----------------------
        接下去我们来解释为什么调用的是第一个demo2()方法:
        1.这里我们多写了两个demo2(),实际上是方法的重载
            (一定要搞清楚重载和重写的区别,后面会对方法重写做一个说明)
        2.方法重载,对于JVM来说,是一种静态行为!
        3.所以是根据p1的静态类型来匹配具体的重载版本...
        4.这种匹配是在编译阶段就能确定的,我们可以去看看字节码文件
        5.这里就是所谓的方法的"静态分派"
            所有依赖静态类型来决定方法执行版本的分派动作都称为"静态分派"，
            其中最常见的就是"方法重载"
         */
        System.out.println("-------------------");

        // 接下去我们在来看看"方法重写"
        Fruit apple = new Apple();
        Fruit orange = new Orange();
        apple.test();
        orange.test();
        apple = new Orange();
        apple.test();
        /*
        如果还是根据静态类型进行方法匹配,那么最终输出的一定都是"Fruit..."
        但是我们现在看到的结果不是这样的,说明这里不是按照"静态分派"的机制来运行了
        这里我们介绍 "invokevirtual" 字节码指令的多态查找流程:
            寻找到操作数栈顶的第一个元素所指向的对象的"实际类型"(注意这里不再是"静态类型")
            在这个实际类型中寻找到对应的方法,
            进行权限校验,通过后就去调用这个特定方法,
            如果找不到这个特定方法,那么就按照继承的关系,从下往上依次搜寻这样的方法
            如果始终没有找到,最后抛出AbstractMethodError
        话句话说,他会根据方法的接受者(实际调用者)来选择对应版本的重写方法
        这种在运行期根据实际类型确定方法的具体版本的分派过程称为"动态分派"
         */
    }
}

class GrandFather {}
class Father extends GrandFather{}
class Son extends Father{}

class Fruit {
    public void test() {
        System.out.println("Fruit...");
    }
}

class Apple extends Fruit {
    @Override
    public void test() {
        System.out.println("Apple...");
    }
}

class Orange extends Fruit {
    @Override
    public void test() {
        System.out.println("Orange...");
    }
}