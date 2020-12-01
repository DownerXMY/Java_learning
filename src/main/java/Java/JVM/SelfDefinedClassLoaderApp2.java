package Java.JVM;

// 深刻理解"双亲委托机制"!

import java.io.*;

/*
我们顺带对自定义的类加载器做一些改善(其实是对自定义的findClass方法做一点改善)
因为我们总是要表现出自定义类加载器的优势,不然我们直接用AppClassLoader就可以了
1.我们要定义类加载的路径
    否则的话就是默认从当前工程中加载,那么这显然是不理想的,
    比如我们可能想从别的地方去加载类,比如说从桌面,从某个文件夹中,...
2.怎么才能避免双亲委托机制造成的应用类加载器加载的情况呢？
    这次我们创建一个测试文件TestApp.class
    在桌面创建Java/JVM文件路径:
        mkdir -p Desktop/Java/JVM
    将TestApp.class移动到改目录下
        cd IdeaProjects/Java_learning/target/classes
        mv Java/JVM/TestApp.class /Users/mingyuexu/Desktop/Java/JVM
    将代码中的LOAD_PATH改成新的桌面文件路径
    -----------------
    我惊呆了!
    首先加载成功不足奇怪,但是居然是通过我们自定义的类加载器加载的!
    -----------------
    这就说明应用类加载器是加载不了工程之外的.class文件的...
    现在我们让问题更加复杂,我们重新build工程,让TestApp.class重新被编译出来
    不改变代码(LOAD_PATH仍然是桌面路径)
    这个时候请问再次运行会有什么样的结果,类加载器是哪个?
    -----------------
    再次惊呆!
    居然又变成了AppClassLoader,
    可是我们加载的还是位于桌面文件夹中的TestApp.class啊...
    -----------------
    这是因为AppClassLoader能够在工程中找到这个字节码文件,所以就加载了,
    那么这样一来我们自定义的类加载器就没有机会再去加载桌面上的相同的字节码文件了
    本质上还是"双亲委托机制"在起作用,
    自定义类加载器先委托它的双亲AppClassLoader去加载字节码文件
    如果能够获取,那么自定义类加载器就没有机会加载了
    如果AppClassLoader找不到,那么自定义的类加载器才会去尝试定位加载对应名字的类
 */

public class SelfDefinedClassLoaderApp2 {

    public static void main(String[] args)
            throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException {
        MyClassLoader2 myClassLoader2 =
                new MyClassLoader2("MyLoader2");
//        myClassLoader2.set_PATH("/Users/mingyuexu/IdeaProjects/" +
//                "Java_learning/target/classes/");
        myClassLoader2.set_PATH("/Users/mingyuexu/Desktop/");
        Class<?> cls = myClassLoader2
                .loadClass("Java.JVM.TestApp");
        System.out.println(cls.getName());
        System.out.println(cls.hashCode());
        Object object = cls.newInstance();
        System.out.println(object);
        System.out.println(object.getClass().getClassLoader());
        System.out.println("---------------------");
        // 现在为了更好地理解类加载器的"双亲委托机制",
        // 我们再去定义一个新的类加载器
        MyClassLoader2 myClassLoader2_1 =
                new MyClassLoader2("MyLoader2_1");
        myClassLoader2_1.set_PATH("/Users/mingyuexu/Desktop/");
        Class<?> cls2 =
                myClassLoader2_1.loadClass("Java.JVM.TestApp");
        System.out.println(cls2.getName());
        System.out.println(cls2.hashCode());
        Object object2 = cls2.newInstance();
        System.out.println(object2);
        System.out.println(object2.getClass().getClassLoader());
        /*
        当我们删除工程中的字节码文件 TestApp.class 以后,
        再去运行,我们发现现在两个自定义的ClassLoader的实例都去加载了,
        而且我们得到了同一个Class对象的不同HashCode,
        之前说过,同一个类只会被加载一次(详见loadClass方法的Java-doc),
        那么就说明这里是两个不同的Class对象
        ------------------------
        这里就涉及到了所谓"类加载器的命名空间"的问题...
        "在同一个命名空间中,不会出现完整名字相同的两个类!"
        但是"在不同的命名空间中,是可以出现完整名字完全相同的两个类"的!
        ------------------------
        你比如说不在工程中删除字节码文件,那么两个都将是由AppClassLoader加载
        那么这就是属于同一个命名空间,所以你会看到两个的HashCode将是完全一样的!
         */
        System.out.println("---------------------");
        // 我们现在再去构建一个新的自定义类加载器实例,
        // 但是这一次,我们让新的实例变成MyLoader1的子系...
        MyClassLoader2 myClassLoader2_2 =
                new MyClassLoader2("MyLoader2_2",myClassLoader2);
        myClassLoader2_2.set_PATH("/Users/mingyuexu/Desktop/");
        Class<?> cls3 = myClassLoader2_2
                .loadClass("Java.JVM.TestApp");
        System.out.println(cls3);
        System.out.println(cls3.hashCode());
        Object object3 = cls3.newInstance();
        System.out.println(object3);
        System.out.println(object3.getClass().getClassLoader());
        /*
        我们看到了这就是"双亲委托机制",
        这里还是由MyLoader1去加载的,而且因为之前已经加载了,
        所以这里直接获取就好了,这就是为什么findClass()没有被再次调用,
        这也是为什么我们打印出了和第一个完全一样的HashCode...
         */
        System.out.println("----------------------");
        // 我们再做一点拓展:
        MyClassLoader2 myClassLoader2_3 =
                new MyClassLoader2("MyLoader2_3",myClassLoader2_2);
        myClassLoader2_3.set_PATH("/Users/mingyuexu/Desktop/");
        Class<?> cls4 = myClassLoader2_3
                .loadClass("Java.JVM.TestApp");
        System.out.println(cls4);
        System.out.println(cls4.hashCode());
        Object object4 = cls4.newInstance();
        System.out.println(object4);
        System.out.println(object4.getClass().getClassLoader());
        // 注意只要不删除字节码文件TestApp.class,那么打出的结果将会完全一样...
        // 删除了字节码文件,那么理解的重点就转移到了命名空间上!
    }
}

class MyClassLoader2 extends ClassLoader {

    private String DIY_NAME;
    private final String INDEX_EXTENSION = ".class";
    private String LOAD_PATH;

    public void set_PATH(String LOAD_PATH) {
        this.LOAD_PATH = LOAD_PATH;
    }

    public String getDIY_NAME() {
        return DIY_NAME;
    }

    public MyClassLoader2(String diy_name) {
        super();
        this.DIY_NAME = diy_name;
    }

    public MyClassLoader2(String diy_name,ClassLoader parent) {
        super(parent);
        this.DIY_NAME = diy_name;
    }

    @Override
    protected Class<?> findClass(String name)
            throws ClassNotFoundException {

        // 用于检验findClass有没有被执行...
        System.out.println("findClass invoked: "+name);
        System.out.println("MyLoaderName: "+this.DIY_NAME);

        InputStream inputStream = null;
        byte[] classData = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        String SYS_NAME = name.replace(".","/");
        // 变成系统文件路径格式,其中Mac是"/",Windows是"\"

        try {
            inputStream = new FileInputStream(
                    new File(this.LOAD_PATH
                                    +SYS_NAME
                                    +this.INDEX_EXTENSION)
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
