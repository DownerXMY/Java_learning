package Java.JVM;

/*
自定义类加载器的实现...
这是类加载器的最最重要的一个例子,
可以产生千变万化帮助我们理解类加载器的核心...
 */

import java.io.*;

public class SelfDefinedClassLoaderApp {

    public static void main(String[] args)
            throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException {
        MyClassLoader1 myClassLoader1 =
                new MyClassLoader1("MyLoader1");
        Class<?> class1 = myClassLoader1.
                loadClass("Java.JDK8.StreamAPI");
        myClassLoader1.getName();
        System.out.println(class1);
        System.out.println(class1.getName());
        System.out.println(class1.getSimpleName());
        System.out.println("------------------------");
        Object clsName = class1.newInstance();
        System.out.println(clsName);
        /*
        下面打印出来的结果很好地回答了我们提出的问题
        实际上,我们这个Object对象还是由应用类加载器加载的,
        而不是由我们自定义的类加载器加载的...
        其实本质问题就是出在了"双亲委托机制"!
        我们自定义的类加载器会把加载类的任务委托给它的"双亲",也就是AppClassLoader
        而AppClassLoader当然是可以加载这个 Java.JDK8.StreamAPI 类的
        这些问题我们将会到SelfDefinedClassLoaderApp2中去解决...
        */
        System.out.println(clsName.getClass().getClassLoader());
        /*
        我们的问题是:我们明明传入的是DIY_NAME="MyLoader1",那么
        1.为什么findClass中要让这个东西去做一个replace呢?
        (详见下面的回答...)
        ------------
        2.findClass我们从来都没有调用过,那么入参name究竟是什么呢?
        注意loadClass(name)最终返回的是findClass(name),
        所以说findClass中传入的name其实就是 "Java.JDK8.StreamAPI" ,
        这样一来,我们就能够解答第一个问题了,那就是其实这个DIY_NAME没什么卵用...
        ------------
        3.我们重写的findClass方法是不是中间只能用byte的数据结构呢?
        答案是肯定的,这一点我们只要去读一下defineClass的源码就很快能明白了
         */
    }
}

class MyClassLoader1 extends ClassLoader {
    private String DIY_NAME;
    private final String DEFAULT_EXTENSION = ".class";

    public MyClassLoader1(String DIY_name) {
        super();
        this.DIY_NAME = DIY_name;
    }

    public void getName() {
        System.out.println(this.DIY_NAME+":");
    }

    @Override
    protected Class<?> findClass(String name)
            throws ClassNotFoundException {

        /*
        问题就来了,既然我们知道findClass是在loadClass中被调用了,
        那么为什么下面一句话不会被打印出来呢?
        所以不要故意骗自己,费了半天劲写的这个findClass方法就是压根没有执行
        */
        System.out.println("findClass invoked: "+name);

        InputStream inputStream = null;
        byte[] classData = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            inputStream = new FileInputStream(
                    new File(name+this.DEFAULT_EXTENSION)
            );
            byteArrayOutputStream = new ByteArrayOutputStream();

            int index = 0;
            while (-1 != (index = inputStream.read())) {
                byteArrayOutputStream.write(index);
            }
            classData = byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.defineClass(name,classData,0, classData.length);
    }
}