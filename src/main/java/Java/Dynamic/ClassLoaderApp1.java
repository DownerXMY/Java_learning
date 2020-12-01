package Java.Dynamic;

import net.mindview.util.BinaryFile;

import java.io.IOException;

/*
我们来看一下Java中的类加载过程,学习如何自定义类加载器实现更加强大的动态功能...
ClassLoader就是我们在这一章中要重点讨论的内容
下面这几句话尤其重要:
1.类加载器ClassLoader是加载其他类的类,
2.ClassLoader负责将字节码文件加载到内存,创建对应的Class对象
3.ClassLoader一般是由系统提供的,不过如果想要实现更加强大的功能,很多时候需要自定义
4.列举一些由ClassLoader实现的比较高级的功能:
    热部署,应用的模块化和相互隔离,从不同地方灵活加载
5.自定义的ClassLoader有助于我们理解系统程序和框架,比如Tomcat,JSP,OSGI,...
6.类加载器不是只有一个,一般在程序运行时,会有三个类加载器被启动
    启动类加载器(Bootstrap ClassLoader) -- 负责加载Java的基础类
    扩张类加载器(Extension ClassLoader) -- 负责加载Java的扩展类(Jar包)
    应用程序类加载器(Application ClassLoader) -- 负责加载自己写的和第三方提供的类
    从上至下保持父子的从属关系,这一点其实在下面的demo1()很容易看出来...
 */
public class ClassLoaderApp1 {

    public void demo1() {
        // 通过"类名.class.getClassLoader()获取类加载器
        // 通过.getParent()获得父类加载器
        // 如果已经到了最上面一级,就返回null
        ClassLoader classLoader =
                ClassLoaderApp1.class.getClassLoader();
        while(classLoader != null) {
            System.out.println(classLoader.getClass().getName());
            classLoader = classLoader.getParent();
        }
        System.out.println(String.class.getClassLoader());
    }

    public void demo2() {
        // 获取默认的系统类加载器
        ClassLoader classLoader =
                ClassLoader.getSystemClassLoader();
        System.out.println(classLoader);
        try {
            Class<?> cls =
                    classLoader.loadClass("java.util.ArrayList");
            ClassLoader actualLoader = cls.getClassLoader();
            // 由于委派机制,即使我们是用classLoader去加载java.util.ArrayList的
            // 因为java.util.ArrayList实际上是由Bootstrap ClassLoader加载的
            // 所以最终打印出来的是null
            System.out.println(actualLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class Hello {
        // 注意这句代码块是包含在类的初始化方法中的
        static {
            System.out.println("Hello...");
        }
    }

    public void demo3() {
        ClassLoader classLoader =
                ClassLoader.getSystemClassLoader();
        String clsName = ClassLoaderApp1.class.getName()+"$Hello";
        try {
            // 这里其实就说明了两种加载类的方法扥不同之处...
            // ClassLoader.loadClass() VS Class.forName()
            // loadClass不会执行类的初始化代码,但是forName是会的
            Class<?> cls = classLoader.loadClass(clsName);
            System.out.println("~~~~~~~");
            Class<?> cls1 = Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public class MyClassLoader extends ClassLoader {
        private static final String BASE_DIR =
                "/Users/mingyuexu/IdeaProjects/Java_learning/target/classes/Java/";

        @Override
        protected Class<?> findClass(String name)
                throws ClassNotFoundException {
            String fileName =
                    name.replaceAll("\\.","/");
            fileName = BASE_DIR+fileName+".class";
            try {
                byte[] bytes = BinaryFile.read(fileName);
                return defineClass(name,bytes,0,bytes.length);
            } catch (IOException e) {
                throw new ClassNotFoundException(
                        "Failed to load the class "+name,e
                );
            }
        }
    }

    public void demo4() throws ClassNotFoundException {
        // 现在让我们开始学习自定义的ClassLoader
        /*
        首先,我们要明确大致的思路,如何自定义一个ClassLoader？
        1.继承ClassLoader,然后重写抽象方法findClass就可以了
        2.重写findClass换句话说就是用自定义的方式找到字节码文件,然后转化为Class对象
         */
        MyClassLoader myClassLoader = new MyClassLoader();
        String className = "HelloWorld";
        Class<?> cls = myClassLoader.loadClass(className);
        System.out.println(cls);
    }

    public static void main(String[] args) {
        ClassLoaderApp1 classLoaderApp1 = new ClassLoaderApp1();
        classLoaderApp1.demo1();
        System.out.println("-----------------");
        classLoaderApp1.demo2();
        System.out.println("-----------------");
        classLoaderApp1.demo3();
        System.out.println("-----------------");
//        classLoaderApp1.demo4();
        // 实际山最后我们还有个热部署的应用例子,不过我想我们可以慢慢来,
        // 一方面是后面我们学习JVM的时候肯定会回过头来看类加载器的,这是极其重要的一块内容
        // 等到真正做企业级开发的时候再去学习...
    }
}
