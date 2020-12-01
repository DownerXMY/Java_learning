package Java.container;

import com.sun.tools.javac.util.ListBuffer;
import net.mindview.util.Countries;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class CollectionsApp {

    public static void main(String[] args) {
        // Collection接口
        Collection<String> c = new ArrayList<>();

        /* 那么这里就涉及到了怎么创建list的问题...
           注意这里的ArrayList实际上是一个实现了List接口的类
           所以c作为ArrayList的实例，当然也是List
           从而可以看到add是一种最为简单的方式
           当然List接口的实现数不胜数,不过我们常用的并不会有那么多
           ArrayList,LinkedList,HashSet,HashMap,...
         */

        c.addAll(Arrays.asList("Hello","world","MingyueXu"));
        System.out.println(c);
        c.add("SJTU");
        System.out.println(c);
        System.out.println("---------------------------");

        // 接下去让我们来看看List的其余基本操作
        List list = new ArrayList();
        IntStream.rangeClosed(1,9).forEach(i -> {
            list.add(i*i);
        });
        System.out.println(list);

        Iterator iterator = list.iterator();
        boolean b = iterator.hasNext();
        System.out.println(b);
        ListIterator listIterator = list.listIterator();
        listIterator.add(20);
        System.out.println(listIterator.nextIndex());
        System.out.println("----------------------------");
    }
}
