package Java.DesignPattern;

/*
之前我们已经学习了Builder设计模式的具体实现,
我们真的是逐渐开始进入到OOP设计模式的核心部分了,
后面的内容将会越来越难并且越来越重要!
AbstractFactory抽象工厂,是将抽象零件组装成抽象产品的一种设计模式
那么我们说其最大的难度也就是在于"所有的工作都是在抽象层面完成的"...
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class AbstractFactoryApp1 {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: Java.DesignPattern.AbstractFactoryApp.concreteFactory");
            System.out.println("Example1: listFactory");
            System.out.println("Example2: tableFactory");
        }
        AbstractFactory factory =
                AbstractFactory.getFactory(args[0]);

        Link us_yahoo = factory.createLink(
                "Yahoo!","http://www.yahoo.com/");
        Link people = factory.createLink(
                "PeopleNews","http://www.people.com.cn/");
        Link google = factory.createLink(
                "Google","http://www.google.com/");
        Link baidu = factory.createLink(
                "Baidu","http://www.baidu.com/");

        Tray traynews = factory.createTray("news");
        traynews.add(people);
        Tray trayyahoo = factory.createTray("yahoo");
        trayyahoo.add(us_yahoo);
        Tray traysearch = factory.createTray("search");
        traysearch.add(google);
        traysearch.add(baidu);

        Page page = factory.createPage("LinkPage","MingyueXu");
        page.add(traynews);
        page.add(trayyahoo);
        page.add(traysearch);
        page.output();
        /*
        首先我们一定要先看懂这里的抽象类的逻辑结构,
        明确一点那就是现在我们运行啥都没有,
        因为我们根本还没写makeHTML这个抽象方法的实现...
        所以要明白,到这里为止,我们只是写了一个最最简单的"服务"
        服务的具体实现,比如我们要在引导页里面加上对应网页的链接
        这是后面的事情,包括数据库开发,"前端"开发...
        当然我们肯定还是要做一点实际的应用,
        将会在AbstractFactoryApp2.java中...
         */
    }
}

// -----------------
// 抽象零件
abstract class Item {

    protected String caption;

    public Item(String caption) {
        this.caption = caption;
    }

    public abstract String makeHTML();
}

abstract class Link extends Item {
    // Link当然是网页的链接...

    protected String url;

    public Link(String caption,String url) {
        super(caption);
        this.url = url;
    }
}

abstract class Tray extends Item {
    // Tray可以理解成是对于网页的分类

    protected ArrayList<Item> arrayList = new ArrayList<>();

    public Tray(String caption) {
        super(caption);
    }

    public void add(Item item) {
        arrayList.add(item);
    }
}

// ------------------
// 抽象产品

abstract class Page {
    // Page当然是将链接汇总以后的网页
    // 根据在main方法中的代码,
    // 我们知道最终我们会写出一个名字为LinkPage.html的引导页...

    protected String title;
    protected String author;
    protected ArrayList<Item> content = new ArrayList<>();

    public Page(String title,String author) {
        this.title = title;
        this.author = author;
    }

    public void add(Item item) {
        content.add(item);
    }

    public void output() {
        try {
            String filename = title + ".html";
            Writer writer = new FileWriter(filename);
            writer.write(this.makeHTML());
            writer.close();
            System.out.println("Finish writing the "+filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract String makeHTML();
}

// -----------------
// 抽象工厂
abstract class AbstractFactory {

    public static AbstractFactory getFactory(String classname) {
        // 实际上这里的classname就是我们后面对AbstractFactory的具体实现的类名
        // 比如,我们后面可以这么在终端中运行:
        /*
        java Java.DesignPattern.AbstractFactoryApp1 ListFactory
         */
        // 那么这里有一个值得思考的问题就是我们能不能用loadClass?
        // 比如说:ClassLoader.getSystemClassLoader().loadClass(classname).newInstance();
        AbstractFactory factory = null;
        try {
            factory = (AbstractFactory) Class.forName(classname).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return factory;
    }

    public abstract Link createLink(String caption,String url);
    public abstract Tray createTray(String caption);
    public abstract Page createPage(String title,String author);
}