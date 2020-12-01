package Java.container;

import net.mindview.util.Generator;
import net.mindview.util.MapData;
import net.mindview.util.Pair;
import net.mindview.util.Range;

import java.util.*;
import java.util.stream.IntStream;

public class FillingContainer {

    public static void main(String[] args) {
        // 填充容器
        // to see an easy instance first
        List<StringAddress> list =
                new ArrayList<>(
                        // 本质上，还是Collections的应用...
                        Collections.nCopies(
                                4,
                                new StringAddress("Hello"))
                );
        System.out.println(list);
        Collections.fill(list, new StringAddress("world"));
        System.out.println(list);
        System.out.println("-----------------------------");

        // 接下去，让我们来学习什么叫做map生成器
        // MapData<K,V>
        System.out.println(
                MapData.map(new Letters(),11)
        );
        System.out.println(
                MapData.map(new Letters(),"Hello")
        );
        // 以上两个map(),
        // 一个传入的是Generator,另一个传入的是Iterable
        System.out.println("-----------------------------");
        int[] result = Range.range(10);
        System.out.println(Arrays.toString(result));
        System.out.println(
                Arrays.toString(
                        IntStream.rangeClosed(0,9).toArray()));
        // 其实，我们有很多种方法来实现同一件简单的事情...
    }
}

class StringAddress {
    private String s;
    public StringAddress(String str) {
        this.s = str;
    }

    // 这个地方其实是将StringAddress的object.toString()方法做了重载
    public String toString() {
        return super.toString()+" "+s;
    }
}

class Letters implements
        Generator<Pair<Integer,String>>,Iterable<Integer> {
    private final int size = 9;
    private int number = 1;
    private char letter = 'A';

    @Override
    public Pair<Integer,String> next() {
        return new Pair<Integer,String>
                (number++,""+letter++);
    };

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return number < size;
            }

            @Override
            public Integer next() {
                return number++;
            }
        };
    }
}
