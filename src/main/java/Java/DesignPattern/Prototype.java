package Java.DesignPattern;

/*
我们之前通过Singleton设计模式,已经实现了生成单一对象实例的需求
接下去我们要介绍的设计模式还是跟生成对象息息相关的:
    一般而言,我们都是通过new关键字生成实例的,
    用new关键字的必须条件是我们要指定类名,
    但是有的时候,我们的实际需求要求我们在不指定类名的时候
    也能够生成对应的实例,换句话说要求我们根据现有的实例来生成新的实例
这件事情其实很难理解,
那么我们还是通过具体的实例来理解这种需求和我们具体的实现方式
------------------
不根据类来生成实例,而是根据已有的实例来生成新的实例的设计模式
就称为"Prototype模式"
在Java中,有一个特别重要的接口 Cloneable
顾名思义,这个接口就是为了辅助我们完成Prototype设计模式的实现的
 */

import java.util.HashMap;
import java.util.stream.IntStream;

public class Prototype {

    public static void main(String[] args) {
        Manager manager = new Manager();
        UnderlinePen underlinePen =
                new UnderlinePen('-');
        MessageBox messageBox1 = new MessageBox('*');
        MessageBox messageBox2 = new MessageBox('/');
        manager.register("Strong message",underlinePen);
        manager.register("Warning message",messageBox1);
        manager.register("Slash message",messageBox2);

        Products products1 = manager.create("Strong message");
        products1.use("Hello world");
        System.out.println("-----------------");
        if (products1 == underlinePen) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        // 这里说明了确实生成了新的实例...
        if (products1.getClass() == underlinePen.getClass()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        // 这里说明了他们确实是同一个类的实例...
        System.out.println("-----------------");
        Products products2 = manager.create("Warning message");
        products2.use("Hello world");
        Products products3 = manager.create("Slash message");
        products3.use("Hello world");
        /*
        怎么理解程序:
        程序的调用过程是create() -> createClone() -> clone()
        我们通过原有的实例(我们放到HashMap中的实例)来创建新的实例
         */
    }
}

interface Products extends Cloneable {

    // 接口默认public, final, abstract...
    void use(String string);

    Products createClone();
}

class Manager {

    private HashMap<String,Products> showcase =
            new HashMap<>();

    public void register(String name, Products products) {
        showcase.put(name,products);
    }

    public Products create(String protoname) {
        Products product =
                (Products) showcase.get(protoname);
        return product.createClone();
    }
}

class MessageBox implements Products {

    private char decochar;

    public MessageBox(char decochar) {
        this.decochar = decochar;
    }

    @Override
    public void use(String string) {
        int length = string.getBytes().length;
        IntStream.rangeClosed(1,length+4).forEach(i -> {
            System.out.print(decochar);
        });
        System.out.println();
        System.out.println(decochar+" "+string+" "+decochar);
        IntStream.rangeClosed(1,length+4).forEach(i -> {
            System.out.print(decochar);
        });
        System.out.println();
    }

    @Override
    public Products createClone() {
        Products newproducts = null;
        try {
            newproducts = (Products)clone();
        } catch (CloneNotSupportedException e ) {
            e.printStackTrace();
        }
        return newproducts;
    }
}

class UnderlinePen implements Products {

    private char ulchar;

    public UnderlinePen(char ulchar) {
        this.ulchar = ulchar;
    }

    @Override
    public void use(String string) {
        int length = string.getBytes().length;
        System.out.println("\""+string+"\"");
        System.out.print(" ");
        IntStream.rangeClosed(1,length).forEach(i -> {
            System.out.print(ulchar);
        });
        System.out.println();
    }

    @Override
    public Products createClone() {
        Products newproducts = null;
        try {
            newproducts = (Products)clone();
        } catch (CloneNotSupportedException e ) {
            e.printStackTrace();
        }
        return newproducts;
    }
}