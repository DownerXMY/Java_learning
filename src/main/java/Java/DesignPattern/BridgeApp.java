package Java.DesignPattern;

import java.util.stream.IntStream;

/*
Bridge设计模式,顾名思义,其作用一定是将两件东西联系在一起...
那么究竟是那两件事情是一个很关键的问题,其实我们之前已经接触过一些他们之间的联系了:
类的功能层次结构 和 类的实现层次结构
什么叫做类的功能层次结构?
    假如我想要给某一个类增加新的功能,一种最简单的方式就是创建一个子类
    这个时候就出现了一个最简单的类功能层次结构
什么叫做类的实现层次结构?
    最简单的一种情况就是,子类实现了抽象父类中的抽象方法
    这种结构的作用就是帮助实现任务分担,
    因为我们肯定不能把所有的具体实现都放在一个大类中
那么我们肯定还是要来看一个具体的例子...
 */
public class BridgeApp {

    public static void main(String[] args) {
        Display display1 =
                new Display(new StringDisplayImpl("Good morning"));
        Display display2 =
                new Display(new StringDisplayImpl("Good evening"));
        CountDisplay countDisplay =
                new CountDisplay(new StringDisplayImpl("Good night"));
        display1.display();
        display2.display();
        countDisplay.display();
        countDisplay.multiDisplay(5);
        /*
        那么我们看到这个程序实际上非常简单...
        这里无非我们要理清楚各个类之间的关系,
        以及Bridge设计模式在其中起的作用:
            显然CountDisplay相对于Display实现了新功能,
                所以是一种功能层次结构
                可以看到功能层次结构其实并不关心具体的功能,更多的是一种纲领
            而StringDisplayImpl只是实现了DisplayImpl中的抽象方法,
                所以是一种实现层次结构
                这里才是实现了具体的功能,比如我们这里将Print具体化成了字符串的Print
        那么我们说Bridge是这两种结构之间的桥梁,具体就表现在:
            在Display的构造器中,需要导入DisplayImpl的实例
                换句话说,两种最基本的类之间存在引用联系,
                就会导致他们的子类之间都能够存在调用的能力
         */
    }
}

class Display {

    private DisplayImpl impl;

    public Display(DisplayImpl impl) {
        this.impl = impl;
    }

    public void open() {
        impl.rawOpen();
    }
    public void print() {
        impl.rawPrint();
    }
    public void close() {
        impl.rawClose();
    }

    public final void display() {
        open();
        print();
        close();
    }
}


class CountDisplay extends Display {

    public CountDisplay(DisplayImpl impl) {
        super(impl);
    }

    public void multiDisplay(int times) {
        open();
        IntStream.rangeClosed(1,times).forEach(i -> {
            print();
        });
        close();
    }
}

abstract class DisplayImpl {
    public abstract void rawOpen();
    public abstract void rawPrint();
    public abstract void rawClose();
}

class StringDisplayImpl extends DisplayImpl {

    private String string;
    private int width;

    public StringDisplayImpl(String string) {
        this.string = string;
        this.width = string.getBytes().length;
    }

    @Override
    public void rawOpen() {
        printLine();
    }

    @Override
    public void rawPrint() {
        System.out.println("|"+string+"|");
    }

    @Override
    public void rawClose() {
        printLine();
    }

    private void printLine() {
        System.out.print("+");
        IntStream.rangeClosed(1,width).forEach(i -> {
            System.out.print("-");
        });
        System.out.println("+");
    }
}