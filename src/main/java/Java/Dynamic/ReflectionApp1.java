package Java.Dynamic;

/*
什么叫做Java反射？
实际上Java反射是一个非常难以理解的概念,我们只能先提供一些Java中比较官方的解释
-----------------------
Java反射是Java中的某一中动态特性,
当然Java的动态特性还包括:注解,动态代理,类加载器...
-----------------------
Java反射是在运行时,而非编译时,动态获取类型的信息
-----------------------
反射的入口是名为Class的类,只有先了解了Class,然后才能去理解反射的基本应用...
 */

import java.lang.reflect.*;
import java.util.*;

public class ReflectionApp1 {

    public void demo1() {
        // 在Java中,类信息对应的类就是java.lang.Class
        // 比如我们可以大致来看几个简单的例子:
        List<Class> classList = new ArrayList<>();
        Class<Date> cls1 = Date.class;
        classList.add(cls1);
        Class<Comparable> cls2 = Comparable.class;
        classList.add(cls2);
        Class<Integer> cls3 = int.class;
        classList.add(cls3);
        Class<Double> cls4 = double.class;
        classList.add(cls4);
        Class<String> cls5 = String.class;
        classList.add(cls5);
        Class<Void> cls6 = void.class;
        classList.add(cls6);
        System.out.println(classList);
        /*
        以上我们依次展示了类,接口,以及一些常用的基本类的Class
        对于数组,情况会稍微复杂一点...
        有几个"["表示是几维数组,然后I表示int,L表示类或接口,
        其余的表示还有比如
        Z表示boolean,C表示char,D表示Double,F表示Float,...
         */
        String[] strings = new String[10];
        Class<? extends String[]> cls7 = strings.getClass();
        System.out.println(cls7);
        int[] ints = new int[5];
        Class<? extends int[]> cls8 = ints.getClass();
        System.out.println(cls8);
        int[][] ints1 = new int[3][2];
        Class<? extends int[][]> cls9 = ints1.getClass();
        System.out.println(cls9);
        // Class的forName和getName方法:
        try {
            Class<?> cls10 = Class.forName("java.util.HashMap");
            System.out.println(cls10.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void demo2() {
        /*
        getSimpleName()--返回的名称不带包信息
        getName()--返回Java内部使用的真正的名称
        getCanonicalName()--返回的名称更加友好
        getPackage()--返回包信息
         */
        try {
            Class<?> cls1 = Class.forName("java.lang.String");
            System.out.println(cls1.getName());
            System.out.println(cls1.getSimpleName());
            System.out.println(cls1.getCanonicalName());
            System.out.println(cls1.getPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void demo3() throws IllegalAccessException {
        /*
        类中定义的静态和实例变量都被称为"字段(Field)"
        类Field位于java.lang.reflect中,实际上与反射相关的类都位于该包下
         */
        List<String> stringList =
                Arrays.asList(new String[] {"Hello","World"});
        Class<?> cls = stringList.getClass();
        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            System.out.println(field.getName()+" "+field.get(stringList));
        }
        // 比较重要的一点是要去学会如何查看字段的修饰符:
        // 比如我们新建一个类Girl,然后想要查看字段MAX_NAME_LENGTH的修饰符
        try {
            Field field1 =
                    Girl.class.getField("MAX_NAME_LENGTH");
            int mods = field1.getModifiers();
            System.out.println(Modifier.toString(mods));
            // 实际上这样子打印就已经可以获取全部我们需要的信息了...
            System.out.println("is Public: "+Modifier.isPublic(mods));
            System.out.println("is Private: "+Modifier.isPrivate(mods));
            System.out.println("is Static: "+Modifier.isStatic(mods));
            System.out.println("is Final: "+Modifier.isFinal(mods));
            System.out.println("is Volatile: "+Modifier.isVolatile(mods));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void demo4() {
        /*
        类中定义的静态和实例方法都被称为"方法",用类Method表示
         */
        Class<?> cls = Integer.class;
        try {
            Method method =
                    cls.getMethod("parseInt", new Class[]{String.class});
            System.out.println(method.invoke(null,"123"));
            // invoke表示在指定对象上调用Method代表的方法...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demo5() {
        // 创建对象和构造方法...
        // 这是我们比较熟悉的一个方法.newInstance()
        try {
            Map<String,Integer> map =
                    HashMap.class.newInstance();
            map.put("MingyueXu",100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 也可以通过Constructor构造方法创建对象:
        try {
            Constructor<StringBuilder> constructor =
                    StringBuilder.class.getConstructor(new Class[]{int.class});
            StringBuilder stringBuilder =
                    constructor.newInstance(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 当然现在不知道Constructor这类东西没关系，
        // 我们现在还只是简单知道一下,这都是以后java反射的内容...
    }

    public static void main(String[] args) {
        ReflectionApp1 reflectionApp1 = new ReflectionApp1();
        reflectionApp1.demo1();
        System.out.println("---------------------");
        reflectionApp1.demo2();
        System.out.println("---------------------");
        try {
            reflectionApp1.demo3();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("---------------------");
        reflectionApp1.demo4();
        reflectionApp1.demo5();
        // 当然还有很多Class中的方法,实际上根据之前"反射的定义",
        // 其实他都是反射方法...
    }
}

class Girl {
    public static final int MAX_NAME_LENGTH = 20;
    Girl() {}
}
