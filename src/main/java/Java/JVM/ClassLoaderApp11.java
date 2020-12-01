package Java.JVM;

/*
实际上,我们通过终端也能够看到这些Java自带加载器的加载目录:
java Java.JVM.ClassLoaderApp11
但是很容易发现不对劲,这打印出来的长度和我们在IDEA中打出来的有差别,
最直观的两点感受是:
1.启动类加载器的加载路径竟然变的最长了;
2.尤其是系统类加载器,在IDEA中我们打出了这么长一大串,但是在终端却只有一个".";
这是为什么呢?
----------------------
这是因为在IDEA中运行的时候,IDEA会自动帮我们加载好系统环境变量中的属性值
而在终端运行的时候,加载路径就简单化为了"."(表示当前目录)
----------------------
那么我们不禁回去猜测,刚刚我们只是将扩展类加载器的加载路径变成了当前目录
那么如果我们将启动类加载器的加载路径变成当前目录,会发生什么呢?
换句话说,也就是:
java -Dsun.boot.class.path=./ Java.JVM.ClassLoaderApp11
运行程序实际上是不行的,报的错误是:
Error occurred during initialization of VM
java/lang/NoClassDefFoundError: java/lang/Object
 */

import sun.misc.Launcher;

public class ClassLoaderApp11 {

    public void demo1() {
        // 实际上我们是可以看到三种Java自带的类加载器的特定加载路径的...
        System.out.println("Bootstrap ClassLoader Loading path: ");
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println("------------");
        System.out.println("Extension ClassLoader loading path: ");
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println("------------");
        System.out.println("Application ClassLoader loading path: ");
        System.out.println(System.getProperty("java.class.path"));
    }

    public static void main(String[] args)
            throws ClassNotFoundException {
        ClassLoaderApp11 classLoaderApp11 = new ClassLoaderApp11();
        classLoaderApp11.demo1();
        /*
        Extension ClassLoader和Application ClassLoader本身作为"类"
        他们也是由Java所写的,他们的作用是去加载别的类,
        可问题是他们本身是由谁去加载的?是怎么加载的呢?
        答案是启动类加载器...
         */
        System.out.println(ClassLoader.class.getClassLoader());
        System.out.println(Launcher.class.getClassLoader());
        // 注意这里Launcher中包含了ExtClassLoader和AppClassLoader,
        // 所以这条打印可以说完全说明了这两个加载器是由启动类加载器来加载的...
        System.out.println("---------------------");

        // 替换AppClassLoader,扶正新的SystemClassLoader
        System.out.println(
                System.getProperty("java.system.class.loader")
        );
        // 这就说明在默认情况下,这个系统属性是没有被定义的,
        // 那么就会指向AppClassLoader
        // 我们接下去要做的事情,
        // 就是扶正我们自己定义的SelfDefinedCL成为新的系统类加载器
        /*
        那么怎么做呢?
        java -Djava.system.class.loader=Java.JVM.SelfDefinedCL Java.JVM.ClassLoaderApp11
        但是报错,提示我们需要去在SelfDefinedCL中创建一个仅仅只接受一个ClassLoader参数的构造方法
        修改好以后,我们就能够尝试去运行程序了,
        为了看到更好的效果,我们在写几条验证代码:
         */
        System.out.println("---------------------");
        System.out.println(ClassLoader.getSystemClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader().getParent());
        MyClassLoader2 myClassLoader2 =
                new MyClassLoader2("myloader1");
        Class<?> clazz1 = myClassLoader2
                .loadClass("Java.JVM.TestApp");
        System.out.println(clazz1.getClassLoader());
        /*
        那么为什么打印出来的还是AppClassLoader呢?
        因为我们自定义的SelfDefinedCL其实是以AppClassLoader为双亲的
        所以根据"双亲委托机制",那么久容易理解了
        那么我们不禁要问,我们费了半天劲,到底和之前有没有什么区别?
        其实还是有的:
        原本我们去加载一个类,默认都是从AppClassLoader开始往上委托父类
        但是现在开始是从SelfDefinedCL开始向上委托的
         */
        /*
        那么如果还是想直观地看出区别,
        我们可以尝试将SelfDefinedCL中的下载路径设为"桌面"
        然后我们去加载仅存在桌面上的字节码文件,
        那么如果说"双亲委托机制"还是从AppClassLoader开始的,
        就会报错,然而如果说是从SelfDefinedCL开始的,就能够正常加载
        并且还能打印出类加载器...
        ------------------------
        那么最终确实是成功了...
        别忘了,改回来,SelfDefinedCL以后还要用的...
        */
    }
}
