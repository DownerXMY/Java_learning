package Java.DesignPattern;

/*
Flyweight这种设计模式实际上是非常重要的,
其设计到的本质思想概括起来说就是 "共享对象,避免浪费"
Flyweight这个词语的本身意思是 "轻量级",顾名思义就是让对象变"轻"
在计算机系统中,对象是虚拟存在的事物,其轻重实际上是指占据内存的大小
那么我们学过JVM以后,我们就知道每个实例对象都会在Heap中被分配内存,
然后我们也知道了很多手段类看到对内存的分配情况
----------------------
回到Flyweight设计模式中来,怎么样的方式能够达到轻量级呢?
那么实际上这是个很广的问题,有很多解决方案,比如说我们可以设置一套合理的GC算法
我们在这里没有那么深入,我们能做的只是"减少new语句"...
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Flyweight {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Java.DesignPattern.Flyweight digits");
            System.out.println("Examples: java Java.DesignPattern.Flyweight 1212123");
            System.exit(0);
        }
        BigString bigString = new BigString(args[0]);
        bigString.print();
//        System.gc();
        /*
        所以我们可以看到程序本身并不是很难理解
        但是我们还是要看看之前说的"轻量级"究竟体现在什么地方?
        最关键的地方就在于getBigChar()方法中,注意我们要输说的事情大概就是:
        假如我们在terminal中输入的是:
            java Java.DesignPattern.Flyweight 1212123
        那么"共享实例"说的是什么呢,其实就是这里出现的三个1指向了同一个对象
        这件事情其实是在HashMap中被实现的,
        换句话说,如果已经有了一个实例,那么就不会调用new BigChar()的语句
        然而假如我们在IntStream循环中直接使用new语句,
        那么一定会生成新的实例对象...
        --------------------
        所以我们回过头来可以来看看为什么要加上线程安全的语句...
        如果没有确保线程安全的synchronized或者lock,那么想象一下这样的情景:
        一个线程p1去执行getBigChar(),然后发现HashMap中没有对应的对象,
        于是p1尝试去new,然后put进HashMap中,
        但是当p1还没有完成这些事情的时候,
        另外一个线程p2开始执行getBigChar(),
        那么它还是会发现没有HashMap中没有对应的实例,又会调用new语句
        所以可以想象,在高并发的环境中,"轻量级"名存实亡了
        --------------------
        当然,我们总是希望能够验证一下这件事情...
        我们先提供一些思路,然后选择其中一种进行尝试...
            (1)不同对象的HashCode肯定是不同的
            比如说我们让5个线程同时竞争执行getBigChar(),
            先看看没有线程安全的情况下HashCode的结果有多少,
            为了比较得更加明显,我们在terminal中输入:
            java Java.DesignPattern.Flyweight 444444444444444444444
            我们看到我们一共是输入了21个4,
            最终打印出来我们总共生成了11个不同的BigChar实例
            这符合我们对于多线程并发情况的预期...
            那么如果我们加上synchronized或者lock锁呢?
            我们看到输出的结果就是有且只有生成了一个BigChar的实例
            (2)有没有更简单的方法呢?实际上我们还可以输入一些JVM的参数来判断
            比如,我们简单地可以通过堆空间的大小变化来看,
            因为我们知道生成的实例越多,占用堆空间的内存大小也就越大
            又或者可以试试看-XX:+TraceClassLoading
            比如我们强制性加上System.gc();
            然后在terminal输入:
            java -XX:+PrintHeapAtGC Java.DesignPattern.Flyweight 4444444444444444
        那么这个程序的相关事情,我们就说到这里...
         */
        System.out.println("We have "+BigCharFactory.set.size()+" different instances...");
    }
}

class BigChar {

    private char charName;
    private String fontData;

    public BigChar(char charName) {
        this.charName = charName;
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("Demo"+charName+".txt")
            );
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            reader.close();
            this.fontData = stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println(fontData);
    }
}

class BigCharFactory {

    private HashMap<String,BigChar> pool = new HashMap<>();
    // 我们这里还是采用Singleton涉及模式...
    private static BigCharFactory singleton =
            new BigCharFactory();

    private BigCharFactory() {}

    private final Lock lock = new ReentrantLock();

    public static Set<Integer> set = new HashSet<>();

    public static BigCharFactory getInstance() {
        return singleton;
    }

    public synchronized BigChar getBigChar(char charName) {
        BigChar bigChar = (BigChar) pool.get(""+charName);
        if (bigChar == null) {
            bigChar = new BigChar(charName);
            set.add(bigChar.hashCode());
            pool.put(""+charName,bigChar);
        }
        return bigChar;
    }
}

class BigString {

    private BigChar[] bigChars;

    public BigString(String string) {
        bigChars = new BigChar[string.length()];
        BigCharFactory bigCharFactory =
                BigCharFactory.getInstance();

        IntStream.rangeClosed(0,string.length()-1).forEach(item -> new Thread(() -> {
            bigChars[item] =
                    bigCharFactory.getBigChar(string.charAt(item));
        }).start());
    }

    public void print() {
        IntStream.rangeClosed(0, bigChars.length-1).forEach(item -> {
            bigChars[item].print();
        });
    }
}
