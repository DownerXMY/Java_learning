package Java.DesignPattern;

/*
接下去我们就要讲到我们的老朋友了--Proxy设计模式
这种我们已经相当熟悉的设计模式,不论是研究还是实际开发都具有非常重大的价值
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyApp {

    public static void main(String[] args)
            throws Exception{
        Printable printable1 =
                new PrintProxy("Alice");
        System.out.println("Initially the name is "+printable1.getPrinterName());
        printable1.setPrinterName("Bob");
        System.out.println("Now the name is "+printable1.getPrinterName());
        printable1.print("Hello world...");
        /*
        这个程序没有任何难度,我们其实之前大部分都已经是了如指掌的
        最简答的理解方式就是我们在main()中没有用到Printer的实例,
        但是却执行了所有Printer类中的方法,
        其实这是因为,Printer的实例已经在PrintProxy代理类中动态生成了...
        虽然我们这里说的是"动态生成了",但是这种代理方式仍然只是静态代理!
        --------------------
        比较有趣的一点是,这里我们还是在PrintProxy代理类中现实调用了Printer的类名
        那么有没有什么办法可以不让PrintProxy知道Printer呢?
        我们一定要很快反应过来我们该用什么办法: Java反射
        Java反射是极其有趣且有难度的内容...
         */
        System.out.println("--------------------");
        Printable printable2 =
                new PrintProxy2("Alice","Java.DesignPattern.Printer");
        System.out.println("Initially the name is "+printable2.getPrinterName());
        printable2.setPrinterName("Bob");
        System.out.println("Now the name is "+printable2.getPrinterName());
        printable2.print("I love you!");
    }
}

class Printer {

    private String name;
    public Printer() {
        heavyJob("Is building new instance for Printer...");
    }
    public Printer(String name) {
        this.name = name;
        heavyJob("Is building new instance ("+name+")");
    }

    public void setPrinterName(String name) {
        this.name = name;
    }

    public String getPrinterName() {
        return this.name;
    }

    public void print(String string) {
        System.out.println("===="+name+"====");
        System.out.println(string);
    }

    private void heavyJob(String string) {
        System.out.println(string);
        for (int i = 0;i < 3;i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Ending...");
    }
}

interface Printable {
    void setPrinterName(String name)
            throws NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException;
    String getPrinterName() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    void print(String string)
            throws IllegalAccessException,
            InstantiationException;
}

class PrintProxy implements Printable {

    private String name;
    private Printer realPrinter;

    public PrintProxy (String name) {
        this.name = name;
    }

    @Override
    public synchronized void setPrinterName(String name) {
        if (realPrinter != null) {
            realPrinter.setPrinterName(name);
        }
        this.name = name;
    }

    @Override
    public String getPrinterName() {
        return this.name;
    }

    @Override
    public synchronized void print(String string) {
        if (realPrinter == null) {
            realPrinter = new Printer(name);
        }
    }
}

class PrintProxy2 implements Printable {

    private String name;
    private Class<?> cls;
    private Object object;

    public PrintProxy2(String name,String className)
            throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException {
        this.name = name;
        this.cls = ClassLoader.getSystemClassLoader()
                .loadClass(className);
        this.object = ClassLoader
                .getSystemClassLoader()
                .loadClass(className).newInstance();
    }

    @Override
    public void setPrinterName(String name)
            throws NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException {
        if (object != null) {
            Method method =
                    this.cls.getMethod("setPrinterName",String.class);
            method.invoke(object,name);
        }
    }

    @Override
    public String getPrinterName()
            throws NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException {
        Method method =
                this.cls.getMethod("getPrinterName");
        Object result = method.invoke(object);
        return (String) result;
    }

    @Override
    public void print(String string)
            throws IllegalAccessException,
            InstantiationException {
        if (object == null) {
            object = this.cls.newInstance();
        }
    }
}