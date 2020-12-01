package Java.JVM;

import com.sun.crypto.provider.AESKeyGenerator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DifferClassLoader {

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

    public static void main(String[] args) throws
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException {
        DifferClassLoader differClassLoader =
                new DifferClassLoader();
        differClassLoader.demo1();
        MyClassLoader2 myClassLoader2 =
                new MyClassLoader2("Loader1");
        myClassLoader2.set_PATH("/Library/Java/JavaVirtualMachines/jdk1.8.0_261.jdk/Contents/Home/jre/classes/");
        Class<?> cls1 = myClassLoader2
                .loadClass("Java.JVM.TestApp");
        System.out.println(cls1.hashCode());
        System.out.println(cls1.getClassLoader());
        /*
        当然我们早就知道了这个Class应该是由系统类加载器加载的,
        --------------------
        不过我们现在的问题是,能不能让启动类加载器去加载它呢?
        很显然,我们要做的就是把它放到某个启动类加载器的加载目录下面...
        看到最后一个叫做目录叫做:
        /Library/Java/JavaVirtualMachines/jdk1.8.0_261.jdk/Contents/Home/jre/classes
        但是呢我们在电脑上发现没有这个目录,很奇怪
        不过没关系,没有这个目录,我们就自己创建一个
        然后在里面 sudo mkdir -p Java/JVM
        然后把TestApp.class拷贝到这个目录中...
        来了来了,成功了,我们成功看见了ClassLoader的名字变成了null
        --------------------
        那么最后我们还是先试一下能不能被扩展类加载器所加载,
        那么根据"双亲委托机制",我们应该删除之前新建的classes文件夹
        这里的做法会有点不太一样,我们会创建一个叫做"AESKeyGenerator"的内置实例
         */
        System.out.println("------------------");
        AESKeyGenerator aesKeyGenerator = new AESKeyGenerator();
        System.out.println(aesKeyGenerator.getClass().getClassLoader());
        System.out.println(TestApp.class.getClassLoader());
        /*
        注意这时候我们要在Terminal做一步很惊人的操作:
        我们现在已经知道"扩展类加载器"是根据系统属性"java.ext.dirs"去寻找字节码文件的
        那么如果我们显示修改这个系统属性,扩展类加载器还能不能够找到呢?
        我们要尽到target/classes中,然后输入:
        java -Djava.ext.dirs=./ Java.JVM.DifferClassLoader
        (注意不要自作聪明,在-D后面加上一个空格...)
        注意以上语句其实执行了两个工作,
        1.首先将java.ext.dirs这个系统属性修改成了当前目录
        2.我们同时也是用了java编译器去执行这个java文件
         */
        /*
        当然我们可以雨预料到是会报错的,
        因为我们已经知道这个AESKeyGenerator是通过系统类加载器加载的,
        但是我们现在修改了系统环境属性,那么系统类加载器当然是找不到AESKeyGenerator了...
         */
        System.out.println("----------------------");
        // 最后我们再来看一个例子...
        MyClassLoader2 myClassLoader2_1 =
                new MyClassLoader2("Loader2");
        myClassLoader2_1.set_PATH("/Users/mingyuexu/Desktop/");
        Class<?> cls2_1 = myClassLoader2_1.
                loadClass("Java.JVM.TestApp3");
        System.out.println("loader: "+cls2_1.getClassLoader());
        MyClassLoader2 myClassLoader2_2 =
                new MyClassLoader2("Loader3");
        myClassLoader2_2.set_PATH("/Users/mingyuexu/Desktop/");
        Class<?> cls2_2 = myClassLoader2_2
                .loadClass("Java.JVM.TestApp3");
        System.out.println("loader: "+cls2_2.getClassLoader());
        System.out.print(cls2_1== cls2_2);
        System.out.print(" ");
        System.out.print(cls2_1.hashCode() == cls2_2.hashCode());
        System.out.println();
        Object obj2_1 = cls2_1.newInstance();
        Object obj2_2 = cls2_2.newInstance();
        // 通过反射的方式获取TestApp3中的setTestApp()方法...
        Method method =
                cls2_1.getMethod("setTestApp3",Object.class);
        method.invoke(obj2_1,obj2_2);
        // 这里的obj2表示这个method的调用者,然后obj2_1是表示这个method的入参
        /*
        我们做这样的事情:
        1.为两个自定义的类加载器添加上路径,指向桌面;
        2.将工程中生成的TestApp3.class的字节码文件移动到桌面上去;
        然后我们再去运行程序,会发生让人意想不到的事情...
        --------------
        程序报错了而且报错中有这么一句:
        Java.JVM.TestApp3 cannot be cast to Java.JVM.TestApp3
        其实可以用一句话来解释这件事情:
        "两个没有亲属关系的类加载器所加载的类互相不可见!"
         */
    }
}
