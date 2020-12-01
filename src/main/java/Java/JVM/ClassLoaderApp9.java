package Java.JVM;

/*
我们慢慢深入:
就是说我们之前一直是用我们自定义的类加载器去加载一个类,比如TestApp
那么我们想象一种更为复杂的情况,也是更加接近实际开发的情况:
    Q: TestApp的构造方法中如果有别的类,那么这些类都是由什么加载器加载的呢?
---------------
我们先运行,
然后将TestApp2.class和MySample.class两个字节码文件移动到桌面对应的文件夹中
再次运行,我们发现两个相关联的类都是由自定义的类加载器 loader1 加载的...
---------------
当然可以尝试只删除其中一个看看运行的结果...
我们把例子变得更复杂点,我们让TestApp2和MySample相互引用!
这时候删除MySample.class然后运行程序,程序报错了!
说白了,我们很清楚地知道程序的错误是出在了TestApp2类的构造方法中的这一行代码上
    System.out.println("from TestApp2: "+MySample.class);
    本质原因是:
    我们知道删除了MySample.class字节码文件后,
    这两个类是由不同的类加载器去加载的,
    MySample是由自定义类加载器加载的,而TestApp2是由系统类加载器加载的
    当我们调用MySample.class的时候,因为没有具体申明,所以讲由系统类加载器去加载,
    但是显然是加载不到的,因为已经被我们在工程中删除了.
    那么有人会说了,这个MySample不是已经被加载过了吗?
    但是注意,父加载器是找不到子加载器所加载的类的(考虑命名空间)!
 */

public class ClassLoaderApp9 {

    public static void main(String[] args)
            throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException {
        MyClassLoader2 loader1 =
                new MyClassLoader2("Loader1");
        loader1.set_PATH("/Users/mingyuexu/Desktop/");
        Class<?> cls1 = loader1
                .loadClass("Java.JVM.MySample");
        System.out.println("class: "+cls1.hashCode());
        Object obj1 = cls1.newInstance();
        System.out.println(obj1.getClass().getClassLoader());
    }
}