package Java.DesignPattern;
/*
我们每天都在用计算机文件系统中的"文件夹",其实这是一种容器结构
这里容器结构的定义是:既可以放入东西,也可以被放入别的容器当中...
那么虽然文件和文件夹是不同的东西,但是当他们都被放入到更大的容器中以后,
我们都会把他们看做是相类似的东西,这样子的认知是有助益我们更加方便地处理问题的
我们把这种容器和内容具有一定的一致性的设计模式称为"Composite设计模式"
而我们接下去,不如就以"文件系统"为例子来看看效果...
 */

import java.util.ArrayList;
import java.util.Iterator;

public class Composite {

    public static void main(String[] args) {
        try {
            System.out.println("Making root entries...");
            Directory rootdir = new Directory("root");
            Directory bindir = new Directory("bin");
            Directory tmpdir = new Directory("tmp");
            Directory usrdir = new Directory("usr");
            rootdir.add(bindir);
            bindir.add(tmpdir);
            tmpdir.add(usrdir);
            bindir.add(new File("vi",10000));
            bindir.add(new File("latex",20000));
            rootdir.printList();

            System.out.println();
            System.out.println("Making usr entries...");
            Directory yuki = new Directory("yuki");
            Directory hanako = new Directory("hanako");
            usrdir.add(yuki);
            usrdir.add(hanako);
            yuki.add(new File("composite.java",200));
            hanako.add(new File("memo.tex",300));
            rootdir.printList();
        } catch (FileTreatmentException e) {
            e.printStackTrace();
        }
        /*
        那么我们还是老说法,示例程序本身没有什么值得多说的,
        关键还是我们要掌握这种"一致性"的设计模式的含义:
            就是说我们让File和Directory看上去具有一致性的本质原因是
            他们共同继承了Entry这个抽象类,然后我们还会发现其实
            Directory中的add()方法也是添加Entry实例对象...
         */
    }
}

abstract class Entry {

    public abstract String getName();
    public abstract int getSize();

    public Entry add(Entry entry)
            throws FileTreatmentException {
        throw new FileTreatmentException();
    }

    public void printList() {
        printList("");
    }

    public abstract void printList(String prefix);

    @Override
    public String toString() {
        return getName()+"("+getSize()+")";
    }
}

class File extends Entry {

    private String name;
    private int size;

    public File(String name,int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void printList(String prefix) {
        System.out.println(prefix+"/"+this);
    }
}

class Directory extends Entry {

    private String name;
    private ArrayList<Entry> directory = new ArrayList<>();

    public Directory (String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSize() {
        int size = 0;
        Iterator<?> iterator = directory.iterator();
        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            size += entry.getSize();
        }
        return size;
    }

    @Override
    public Entry add(Entry entry) {
        directory.add(entry);
        return this;
    }

    @Override
    public void printList(String prefix) {
        System.out.println(prefix+"/"+this);
        Iterator<?> iterator = directory.iterator();
        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            entry.printList(prefix+"/"+name);
        }
    }
}

class FileTreatmentException extends RuntimeException {
    public FileTreatmentException() {}
    public FileTreatmentException(String msg) {
        super(msg);
    }
}
