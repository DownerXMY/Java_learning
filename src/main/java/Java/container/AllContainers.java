package Java.container;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class AllContainers {

    public static void main(String[] args) {
        /*
        首先,必须要明白容器与数组的区别
        数组的定义方式类似 int[],然后数组的打印必须依赖Arrays.toString()
        但是容器包括 List,Set,Map,Queue,Hash,...
        构造的方法不尽相同，而且打印是可以直接进行的
         */
        System.out.println("List");
        // Method1
        List<String> list1 = new ArrayList<>();
        list1.add("MingyueXu");
        list1.add("Downer");
        list1.add("SJTU");
        System.out.println(list1);
        // Method2
        List<String> list2 =
                new ArrayList<>(Arrays.asList("Hello","world"));
        System.out.println(list2);
        // Method3
        Integer[] array1 = new Integer[10];
        IntStream.rangeClosed(1,10).forEach(i -> {
            array1[i-1] = (Integer)i*i;
        });
        List<Integer> list3 =
                new LinkedList<>(Arrays.asList(array1));
        System.out.println(list3);
        // 事实上,构造方法数不胜数，也没有必要全部都看过...
        System.out.println("----------------------------");

        System.out.println("Iterator");
        /*
        首先要明白"迭代器"究竟是干什么的
        "迭代器"的创造，是为了避免变成收到容器类型的限制
        比如说用List，那么你只能用.add()添加元素
        而"迭代器"的工作是遍历序列中的元素，不关心底层的结构...
         */
        List<String> list4 =
                new ArrayList<>(Arrays.asList(
                        "MingyueXu","HaoyuShi","QidongSu"
                ));
        // 创建"迭代器"...
        Iterator<String> iterator1 = list4.iterator();
        while (iterator1.hasNext()) {
            String element = iterator1.next();
            System.out.print(element + " ");
        }
        System.out.println();
        iterator1.remove();
        System.out.println(list4);
        // 接下来我们来展示之前说的Iterator的优势...
        LinkedList<String> linkedList1 = new LinkedList<>(list4);
        HashSet<String> hashSet1 = new HashSet<>(list4);
        TreeSet<String> treeSet1 = new TreeSet<>(list4);
        CrossContainer.display(linkedList1.iterator());
        CrossContainer.display(hashSet1.iterator());
        CrossContainer.display(treeSet1.iterator());
        // 根本不需要去关心底层的容器类型，
        // 我们总是可以通过.next()获取，通过.remove()删除
        System.out.println("--------------------------");

        // 还有类似LinkedList,ListIterator这样的内容我们这里就忽略了
        // 接下去我们要说一个特别总要的容器: stack(栈)
        System.out.println("stack");
        /*
        "栈"通常是指"后进先出"(LIFO)的容器,后被压入的元素先弹出...
        可以形象地理解成"自助餐托盘",最后放进的都先被吃掉
        这里我们还要强调一点:
        那就是LinkedList实际上能够直接实现"栈"的所有功能!!!
         */
        LinkedListStack<String> linkedListStack =
                new LinkedListStack<>(new LinkedList<>(
                        Arrays.asList("Hello","World","Girl")
                ));
        System.out.println(linkedListStack);
        linkedListStack.push1("Love");
        System.out.println(linkedListStack);
        System.out.println(linkedListStack.peek1());
        linkedListStack.pop1();
        System.out.println(linkedListStack);
        System.out.println(linkedListStack.empty1());
        // 关于stack,我们暂时就说这么多...
        System.out.println("-------------------------");

        System.out.println("set");
        // set最重要的属性，就是元素不重复
        Random random = new Random(47);
        Set<Integer> set1 = new HashSet<>();
        IntStream.rangeClosed(1,1000).forEach(i -> {
            set1.add(random.nextInt(20));
        });
        System.out.println(set1);
        System.out.println("-------------------------");

        System.out.println("map");
        // Map可以看成是一种"映射容器"
        // 比如我们现在想来看看Random的随机性，
        // 那么就是要将seed产生的值映射到其出现的次数上去
        Map<Integer,Integer> map = new HashMap<>();
        for (int item=0;item<=1000;item++) {
            int r = random.nextInt(20);
            // 通过.get()获取到Map中出现的次数
            Integer frequency = map.get(r);
            // 注意下面这个表达很高级，要看懂并掌握...
            // .put()是构建Map的函数
            map.put(r,frequency == null ? 1 : frequency+1);
        }
        System.out.println(map);
        // 实际上Map可以很容易扩展到高维
        // Map的值可以是其他容器，甚至是其他Map
        Map<String,List<Integer>> map1 = new HashMap<>();
        map1.put("MingyueXu",
                new ArrayList<>(Arrays.asList(100,98,84)));
        map1.put("QidongSu",
                new ArrayList<>(Arrays.asList(88,100,95)));
        map1.put("HaoyuShi",
                new ArrayList<>(Arrays.asList(99,80)));
        System.out.println(map1);
        System.out.println("--------------------------");

        System.out.println("queue");
        /*
        queue和stack是形成对比的
        队列是"先进先出"(FIFO)的
        又要来强调一点:
        LinkedList是Queue的一种实现
        我们可以看到LinkedList实际上是一种非常强大的容器
         */
        Queue<Integer> queue1 = new LinkedList<>();
        IntStream.rangeClosed(1,10).forEach(i -> {
            int result = random.nextInt(i+10);
            System.out.print(result+" ");
            queue1.offer(result);
        });
        System.out.println();
        System.out.println(queue1);
        // 看到打印出来的结果，就验证了FIFO
        System.out.println("-------------------------");

        System.out.println("Foreach");
        /*
        foreach的语法可以应用于任何Collection对象
        这里有一个point很重要，那就是为什么可以在任何Collection对象上工作？
        答案是因为Java5引入了Iterable接口...
        换句话说任何实现了Iterable接口的类都可以被foreach语句调用
         */
        new IterableClass().forEach(item -> {
            System.out.print(item+" ");
        });
        System.out.println();
        // 再比如下面的代码可以显示操作系统的所有环境变量
        System.getenv().entrySet().forEach(entry -> {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        });
        System.out.println("--------------------------");
    }
}

class CrossContainer {
    public static void display(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.print(element+" ");
        }
        System.out.println();
    }
}

class LinkedListStack<T> {
    private LinkedList<T> storage = new LinkedList<>();
    LinkedListStack (LinkedList<T> linkedList2) {
        this.storage = linkedList2;
    }
    public void push1(T v) {
        storage.addFirst(v);
    }
    public T peek1() {return storage.getFirst();}
    public T pop1() {return storage.removeLast();}
    public boolean empty1() {return storage.isEmpty();}
    public String toString() {return storage.toString();}
}

class IterableClass implements Iterable<String> {
    String[] words =
            ("we have faster access to the tail").split(" ");
    // 实现以后就要重载抽象方法...
    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < words.length;
            }

            @Override
            public String next() {
                return words[index++];
            }
        };
    }
}