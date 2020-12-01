package Java.DesignPattern;
/*
在Visitor设计模式中,数据结构和数据处理被分离开来,
我们编写一个"访问者"的类来访问数据结构中的元素,
并把对数据的处理交给那些实际的访问者,
这样子我们在处理数据的时候,就不用为了数据结构的差异而担心了...
当然这么说可能有点抽象,我们还是写一个实际的实例来看看这种设计模式是怎么做到的...
 */

import java.util.ArrayList;
import java.util.Iterator;

public class Visitor {

    public static void main(String[] args)
            throws FileTreatmentException{
        System.out.println("Making root entries...");
        Dir rootDir = new Dir("root");
        Dir binDir = new Dir("bin");
        Dir tmpDir = new Dir("tmp");
        Dir usrDir = new Dir("usr");
        rootDir.add(binDir);
        binDir.add(tmpDir);
        tmpDir.add(usrDir);
        binDir.add(new MyFile("vi",10000));
        rootDir.accept(new ListVisitor());
        /*

         */
    }
}

abstract class MyVisitor {

    public abstract void visit(MyFile file);
    public abstract void visit(Dir dir);
}

interface Element {
    void accept(MyVisitor myVisitor);
}

abstract class MyEntry implements Element {

    public abstract String getName();
    public abstract int getSize();

    public MyEntry add(MyEntry myEntry)
            throws FileTreatmentException {
        throw new FileTreatmentException();
    }

    public Iterator iterator()
        throws FileTreatmentException {
        throw new FileTreatmentException();
    }

    @Override
    public String toString() {
        return getName()+"("+getSize()+")";
    }
}

class MyFile extends MyEntry {

    private String name;
    private int size;

    public MyFile(String name,int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public void accept(MyVisitor myVisitor) {
        myVisitor.visit(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSize() {
        return size;
    }
}

class Dir extends MyEntry {

    private String name;
    private ArrayList<MyEntry> dir = new ArrayList<>();

    public Dir(String name) {
        this.name = name;
    }

    @Override
    public void accept(MyVisitor myVisitor) {
        myVisitor.visit(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSize() {
        int size = 0;
        Iterator<MyEntry> iterator = dir.iterator();
        while (iterator.hasNext()) {
            MyEntry myEntry = (MyEntry) iterator.next();
            size += myEntry.getSize();
        }
        return size;
    }

    @Override
    public MyEntry add(MyEntry myEntry)
            throws FileTreatmentException {
        dir.add(myEntry);
        return this;
    }

    @Override
    public Iterator iterator()
            throws FileTreatmentException {
        return dir.iterator();
    }
}

class ListVisitor extends MyVisitor {

    private String currentDir;

    @Override
    public void visit(MyFile file) {
        System.out.println(currentDir+"/"+file);
    }

    @Override
    public void visit(Dir dir) {
        System.out.println(currentDir+"/"+dir);
        String saveDir = currentDir;
        currentDir += "/"+dir.getName();
        Iterator iterator = dir.iterator();
        while (iterator.hasNext()) {
            MyEntry myEntry = (MyEntry) iterator.next();
            myEntry.accept(this);
        }
        currentDir = saveDir;
    }
}
