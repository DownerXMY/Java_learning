package Java.container;

import net.mindview.util.*;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.lang.reflect.*;

public class ArraysApp4 {
    public static int size = 10;
    public static void test(Class<?> surroundingClass) {
        for(Class<?> type: surroundingClass.getClasses()) {
            System.out.println(type.getSimpleName()+": ");
            try {
                Generator<?> g =
                        (Generator<?>)type.newInstance();
                IntStream.rangeClosed(1,size).forEach(i -> {
                    System.out.print(g.next()+" ");
                });
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        /* 数据生成器
            Generator的引入是为了以更加灵活的方式创建更有意义的数组
         */
        test(CountingGenerator.class);
        System.out.println("-----------------------------");
        // 到这里为止，我们只是介绍了怎么使用Generator,
        // 但是还没跟数组建立关系
        Integer[] demo1 = Generated.array(
                new Integer[]{1,1,1,1},
                new CountingGenerator.INTEGER());
        Integer[] demo2 = Generated.array(
                Integer.class,
                new CountingGenerator.INTEGER(),
                15);
        // 根据我们编写的Generated类，有以上这两种方法...
        /*
        这里的 new CountingGenerator.INTEGER() 真的是一个generator吗？
        实际上，这里可以这么调用，是因为它继承了Generator接口...
         */
        System.out.println(Arrays.toString(demo1));
        System.out.println(Arrays.toString(demo2));
    }
}


class CountingGenerator {
    public static class INTEGER implements Generator<Integer> {
        // 注意这个Generator是在net.mindview.util里的...
        private int value = 0;

        // Generator只是一个借口，其中的next()方法是需要重载的...
        public Integer next() {
            return value++;
        }
    }

    public static class STRING implements Generator<String> {
        private char[] ori =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        private int stringlength = 7;
        public String next() {
            StringBuffer stringBuffer = new StringBuffer();
            IntStream.rangeClosed(1,stringlength)
                    .forEach(i -> {
                Random random = new Random();
                stringBuffer.append(
                        ori[random.nextInt(25)]);
            });
            return String.valueOf(stringBuffer);
        }
    }
}

class Generated {
    public static <T> T[] array(T[] a, Generator<T> generator) {
        return new CollectionData<T>(generator,a.length)
                .toArray(a);
        // CollectionData将创建一个Collection对象，
        // 其中的元素全部都有generator生成...
    }

    public static <T> T[] array(Class<T> type,
                                Generator<T> generator,
                                int size) {
        T[] a = (T[]) Array.newInstance(type,size);
        return new CollectionData<T>(generator,size)
                .toArray(a);
    }
}

// 总的说来，相当于引进了两个新的东西:
// Generator和CollectionData