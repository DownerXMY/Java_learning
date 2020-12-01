package Java.JVM;

/*
深入了解扩展类加载器...
按照我们之前的想法,我们重新在终端尝试:
java -Djava.ext.dirs=./ Java.JVM.ClassLoaderApp10
(./表示当期目录,单单/表示根目录...)
但是结果并没有变化,还是由系统类加载器加载的两个类,
这是为什么呢?要知道我们已经将当前目录变成了扩展类加载器的加载目录,
按照双亲委托机制不应该是这样啊?
---------------------
这里要说明一点,很重要的一点,扩展类加载器是比较特殊的...
扩展类加载器加载的类不能以.class的形式存在
我们必须将其打包成jar,这里学习打成jar包的终端命令:
jar cvf test.jar(打成的名字) Java/JVM/ClassLoaderApp10.class(希望打入的字节码文件)
然后我们再去执行之前的命令...
新的运行结果变成了这样:
ClassLoaderApp10 static block
sun.misc.Launcher$AppClassLoader@2a139a55
sun.misc.Launcher$ExtClassLoader@33909752
这就意味着我们成功实现了用扩展类加载器加载类的效果...
---------------------
 */

public class ClassLoaderApp10 {

    static {
        System.out.println("ClassLoaderApp10 static block");
    }

    public static void main(String[] args) {
        System.out.println(ClassLoaderApp10.class.getClassLoader());
        System.out.println(TestApp.class.getClassLoader());
    }
}
