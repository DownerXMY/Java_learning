package Java.container;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ArraysApp {

    public static void main(String[] args) {
        /* 数组是效率最高的存储和访问的方式
         以下例子总结了初始化数组的各种方式
         重点在于要理解对象数组和基本类型数组的区别
         */
        System.out.println("Object Arrays");
        // -----------------------------------------
        Obj_arrays[] a;
        /* 一个类加上[]表示什么意思呢？
        实际上就表示，我们创建了一个数组，
        且数组中的每一个元素类型是这个类的一个实例
         */
        // -----------------------------------------
        Obj_arrays[] b = new Obj_arrays[5];
        System.out.println(b + Arrays.toString(b));
        // -----------------------------------------
        Obj_arrays[] c = new Obj_arrays[4];
        for (int i = 0; i < c.length; i++) {
            if (c[i] == null) {
                c[i] = new Obj_arrays();
            }
        }
        // -----------------------------------------
        Obj_arrays[] d = {new Obj_arrays(),
                new Obj_arrays(),
                new Obj_arrays()};
        // -----------------------------------------
        Obj_arrays[] e = new Obj_arrays[] {
          new Obj_arrays(),new Obj_arrays(),new Obj_arrays()
        };
        // 当然你会发现其中有部分创建方式是重复且低效的...
        System.out.println("length of b = "+b.length);
        System.out.println("length of c = "+c.length);
        System.out.println("length of d = "+d.length);
        System.out.println("length of e = "+e.length);

        System.out.println("------------------------------");
        System.out.println("Primitive Arrays");
        // 这里我们就举一个简单的例子展示一下
        int f[] = new int[10];
        // 注意这时候默认的元素不再是null,而是0
        System.out.println(f.length);

        IntStream.rangeClosed(1,f.length).forEach(i -> {
            f[i-1] = i*i;
            // 所以数组的索引其实和python中是一样的...
        });
        // 注意Array的打印方式...
        System.out.println(Arrays.toString(f));

        System.out.println("------------------------------");
        Obj_arrays obj_arrays = new Obj_arrays();
        int[] result = obj_arrays.return_arrays();
        System.out.println(Arrays.toString(result));

        // 以上这些其实就是关于一位数组的全部最为基本的内容...
    }
}

class Obj_arrays {
    private static long counter;
    private final long id = counter++;
    public String toString() {
        return "Sphere " + id;
    }

    // 在C++中，想要让函数返回一个数组是相当困难的
    // 但是在java中，这件事情就是理所当然的...
    public int[] return_arrays() {
        int[] result = new int[5];
        IntStream.rangeClosed(1,result.length)
                .forEach(i -> {
                    result[i-1] = i*2;
                });
        return result;
    }
}
