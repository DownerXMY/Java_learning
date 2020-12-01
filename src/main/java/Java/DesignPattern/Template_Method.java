package Java.DesignPattern;

import java.util.stream.IntStream;

public class Template_Method {

    public static void main(String[] args) {
        /*
        什么叫做Template Method模式?
        其核心思想是将任务交给子类来处理,
        这是一种带有"模板"功能的设计模式,
        换句话说就是父类提供"模板"(抽象方法)
        子类实现"模板"的具体操作,
        在父类中,我们只能看到模板存在,但具体的实现方式是不知道的...
        ----------------------
        像这样的在父类中定义处理流程,在子类中实现具体处理的模式就称为:
        Template Method模式
        我们在下面的代码中可以看到,
        这种设计模式最大的特点就是可以实现处理逻辑的通用化...
        ----------------------
        在这里可以尝试这样的一个练习,比如说:
        java.io.InputStream就采用了Template Method设计模式
        可以阅读以下官方的源码,理清设计思路...
        */
        AbstractInterfaceDisplay abstractInterfaceDisplay =
                new CharDisplay('H');
        AbstractDisplay abstractDisplay1 =
                new StringDisplay("Hello World");
        abstractInterfaceDisplay.display();
        System.out.println();
        abstractDisplay1.display();
    }
}

abstract class AbstractDisplay {

    abstract void open();
    abstract void print();
    abstract void close();

    public final void display() {
        open();
        IntStream.rangeClosed(1,5).forEach(i -> {
            print();
        });
        close();
    }
}

interface AbstractInterfaceDisplay {
    void open();
    void print();
    void close();

    // 有的书上说接口不能实现这种设计模式,
    // 那是因为他们不知道现在接口已经有了默认方法...
    default void display() {
        open();
        IntStream.rangeClosed(1,5).forEach(i -> {
            print();
        });
        close();
    }
}

class CharDisplay implements AbstractInterfaceDisplay {

    private char ch;

    public CharDisplay(char ch) {
        this.ch = ch;
    }

    @Override
    public void open() {
        System.out.print("<<");
    }

    @Override
    public void print() {
        System.out.print(ch);
    }

    @Override
    public void close() {
        System.out.print(">>");
    }
}

class StringDisplay extends AbstractDisplay {

    private String string;
    private int width;

    public StringDisplay(String string) {
        this.string = string;
        this.width = string.getBytes().length;
    }

    @Override
    public void open() {
        printLine();
    }

    @Override
    public void print() {
        System.out.println("|"+string+"|");
    }

    @Override
    public void close() {
        printLine();
    }

    public void printLine() {
        System.out.print("+");
        IntStream.rangeClosed(1,width).forEach(i -> {
            System.out.print("-");
        });
        System.out.print("+"+"\n");
    }
}