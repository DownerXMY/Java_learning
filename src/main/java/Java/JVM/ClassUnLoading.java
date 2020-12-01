package Java.JVM;

/*
只有用户自定义的类加载器所加载的类才有可能被卸载
我们写一个例子清楚地看到这种卸载的过程确实是发生了...
这里是需要一点技巧的,首先当然是之前介绍过的JVM参数
-XX:+TraceClassUnloading
其次我们还要显示调用一次系统垃圾回收,但是注意垃圾回收在正常开发中是很少用的
 */

import java.io.*;

public class ClassUnLoading {

    public static void main(String[] args)
            throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            InterruptedException {

        MyClassLoader3 myClassLoader3_1 =
                new MyClassLoader3("Loader1");
        myClassLoader3_1.setLOAD_PATH("/Users/mingyuexu/Desktop/");
        Class<?> cls1 = myClassLoader3_1
                .loadClass("Java.JVM.TestApp");
        System.out.println(cls1);
        System.out.println(cls1.hashCode());
        Object obj1 = cls1.newInstance();
        System.out.println(obj1);
        System.out.println(obj1.getClass().getClassLoader());
        System.out.println("--------------------");

        Thread.sleep(20000);

        /*
        配合 -XX:+TraceClassUnloading 的小技巧:
        这样一来,我们重新运行就可以看到JVM反馈的:
        [Unloading class Java.JVM.TestApp 0x00000007c0061028]
         */
        myClassLoader3_1 = null;
        cls1 = null;
        obj1 = null;
        System.gc(); // 显示调用系统垃圾回收操作...

        // 当然我们还有别的办法可以看见类卸载是真实发生的
        // 打开jvisualvm,监视-类-已卸载的总数,就能看到我们想要的...
        Thread.sleep(20000);

        // 怎么才能看到它被卸载掉?
        myClassLoader3_1 = new MyClassLoader3("Loader2");
        // 我们让myClassLoader3_1指向一个新的实例
        // 其实我们就是想结束之前那个类对象的声明周期...
        myClassLoader3_1.setLOAD_PATH("/Users/mingyuexu/Desktop/");
        Class<?> cls2 = myClassLoader3_1
                .loadClass("Java.JVM.TestApp");
        System.out.println(cls2);
        System.out.println(cls2.hashCode());
        Object obj2 = cls2.newInstance();
        System.out.println(obj2);
        System.out.println(obj2.getClass().getClassLoader());
    }
}

class MyClassLoader3 extends ClassLoader {

    private String DIY_NAME;
    private final String DEFAULT_EXTENSION = ".class";
    private String LOAD_PATH;

    public void setLOAD_PATH(String LOAD_PATH) {
        this.LOAD_PATH = LOAD_PATH;
    }

    public MyClassLoader3 (String diy_name) {
        super();
        this.DIY_NAME = diy_name;
    }

    public MyClassLoader3 (String diy_name,ClassLoader parent) {
        super(parent);
        this.DIY_NAME = diy_name;
    }

    @Override
    protected Class<?> findClass(String name)
            throws ClassNotFoundException {
        System.out.println("findClass invoked: "+name);
        System.out.println("MyLoaderName: "+this.DIY_NAME);

        InputStream inputStream = null;
        byte[] classData = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        String SYS_NAME = name.replace(".","/");

        try {
            inputStream = new FileInputStream(
                    new File(this.LOAD_PATH
                            +SYS_NAME
                            +this.DEFAULT_EXTENSION)
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