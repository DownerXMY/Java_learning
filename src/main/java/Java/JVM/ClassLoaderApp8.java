package Java.JVM;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;

public class ClassLoaderApp8 {

    public static void main(String[] args)
            throws ClassNotFoundException,
            IOException,
            InstantiationException,
            IllegalAccessException {
        Class<?> cls = Class.forName("java.lang.String");
        System.out.println(cls.getClassLoader());
        Class<?> cls1 = Class.forName("Java.JVM.Sample1");
        System.out.println(cls1.getClassLoader());
        System.out.println("--------------------");
        ClassLoader classLoader =
                ClassLoader.getSystemClassLoader();
        Class<?> cls2 =
                classLoader.loadClass("Java.JVM.Sample2");
        System.out.println(cls2);
        System.out.println("--------------------");
        Class<?> cls3 = Class.forName("Java.JVM.Sample2");
        System.out.println(cls3);
        // 这说明了实际上只有Class.forName()初始化了这个Sample2类
        // 而loadClass不是对类的主动使用,所以就不会导致类的初始化
        // 这就是主动使用中"反射"这种情况...
        System.out.println("--------------------");
        System.out.println(classLoader);
        while (classLoader != null) {
            classLoader = classLoader.getParent();
            System.out.println(classLoader);
        }
        System.out.println("--------------------");
        // 获取到当前线程上下文类加载器
        // 如果没有设置,那它就是其父线程的上下文类加载器,
        // 最底层的线程上下文类加载器就是应用类加载器...
        ClassLoader classLoader1 =
                Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader1);
        // 获取字节码文件对应的资源...
        String resourceName = "Java/JVM/ClassLoaderApp2.class";
        Enumeration<URL> urlEnumeration =
                classLoader1.getResources(resourceName);
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            System.out.println(url);
        }
        System.out.println("-------------------");
        Class<?> class1 = ClassLoaderApp8.class;
        System.out.println(class1.getClassLoader());
        System.out.println(class1);
        System.out.println(class1.getName());
        System.out.println(class1.getSimpleName());
        System.out.println("-------------------");
        // 自定义类加载器
        SelfDefinedClassLoader selfDefinedClassLoader =
                new SelfDefinedClassLoader("MyLoader");
        demo(selfDefinedClassLoader);
    }

    public static void demo(ClassLoader classLoader)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> cls = classLoader.
                loadClass("Java.JVM.ClassLoaderApp2");
        Object object = cls.newInstance();
        System.out.println(object);
    }
}

class Sample1 {
    public static int c1 = 5;
}

class Sample2 {
    static {
        System.out.println("Sample class");
    }
}

class SelfDefinedClassLoader extends ClassLoader {
    private String classLoaderName;
    private final String fileExtension = ".class";

    // 我们创建两种有少许差别的构造方法...
    public SelfDefinedClassLoader(String classLoaderName) {
        super();
        /*
        这句话的作用究竟是什么呢?
        指定这个自定义的类加载器的双亲是(系统)应用类加载器...
         */
        this.classLoaderName = classLoaderName;
    }

    public SelfDefinedClassLoader(
            ClassLoader parent,
            String classLoaderName) {
        super(parent);
        /*
        这句话的意思是:
        我们指定创建的类加载器的双亲是parent,
        不一定再是(系统)应用类加载器!
         */
        this.classLoaderName = classLoaderName;
    }

    public String toString() {
        return "["+this.classLoaderName+"]";
    }

    private byte[] loadClassData(String name) {
        InputStream inputStream = null;
        byte[] data = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            this.classLoaderName =
                    this.classLoaderName
                            .replace(".","/");
            inputStream = new FileInputStream(
                    new File(name+this.fileExtension));
            byteArrayOutputStream = new ByteArrayOutputStream();

            int ch = 0;
            while (-1 != (ch = inputStream.read())) {
                byteArrayOutputStream.write(ch);
            }
            data = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    // 自定义ClassLoader永远要重写findClass这个方法...
    @Override
    protected Class<?> findClass(String name)
            throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        return this.defineClass(name,data,0,data.length);
    }
}