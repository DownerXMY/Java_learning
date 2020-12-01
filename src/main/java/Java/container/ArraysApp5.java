package Java.container;

import java.util.*;

public class ArraysApp5 {

    public static void main(String[] args) {
        // Other API in Arrays
        int[] demo1 = new int[7];
        int[] demo2 = new int[10];

        // 填充数组
        Arrays.fill(demo1,7);
        Arrays.fill(demo2,10);
        System.out.println(Arrays.toString(demo1));
        System.out.println(Arrays.toString(demo2));
        System.out.println("-------------------------");

        // 复制数组,但是这个"复制"比较有趣且复杂
        System.arraycopy(
                demo1,0,
                demo2,0,
                demo1.length);
        System.out.println(Arrays.toString(demo2));
        // 很好理解，因为demo2是destination，所以应该是demo2被改变了
        /* 那么这样子打印出来的是什么样子的呢？
        [7, 7, 7, 7, 7, 7, 7, 10, 10, 10]
         */
        int[] demo3 = new int[11];
        Arrays.fill(demo3,11);
        System.arraycopy(
                demo1,1,
                demo3,1,
                5);
        System.out.println(Arrays.toString(demo3));
        // 所以说，与其说是数组复制，倒不如说成是数组覆盖...
    }
}
