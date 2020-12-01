package Java.container;

import org.w3c.dom.ranges.Range;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class ArraysApp2 {

    public static void main(String[] args) {
        /* 多维数组
        多维数组的创建方式同样有很多,但是我们就介绍其中最为常用的一些...
         */
        System.out.println(
                "一个6行4列的矩阵,其中每一个元素都是长度为2的向量...");
        int[][][] m1 = new int[6][4][2];
        System.out.println(m1.length);
        // 这样一来我们就明白了,高维数组的length实际上就是第一个维度的数值
        /*
        System.out.println(Arrays.toString(m1));
        发现这样子打印就不行了，那么对于这种高维的数组，
        我们要采用deepToString
         */
        System.out.println(Arrays.deepToString(m1));
        System.out.println("-----------------------------");

        /* 那么实际上高维数组的索引也是类似的,
        我们可以来看看怎么给刚刚创建m1赋值...
         */
        Random random = new Random();
        IntStream.rangeClosed(1,m1.length).forEach(i -> {
            IntStream.rangeClosed(1,m1[i-1].length).forEach(j -> {
                m1[i-1][j-1] = new int[]
                        {random.nextInt(9),
                                random.nextInt(9)};
            });
        });
        System.out.println(Arrays.deepToString(m1));
        System.out.println("-----------------------------");

        /* 我们稍微想想看,
        在python中，我们的numpy有一个reshape功能，可以调节数组的维度，
        那么java中有没有呢？
         */
        int[] test = new int[10];
        // 自从JDK8以后，循环有了很多新的写法
        for (int item : IntStream.rangeClosed(0,9).toArray()) {
            test[item] = random.nextInt(20);
        }
        // 答案是没有这样的API,如果想要是实现，最终还是要依靠遍历...
    }
}
