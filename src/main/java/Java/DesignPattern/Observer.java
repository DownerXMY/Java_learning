package Java.DesignPattern;
/*
Observer设计模式,顾名思义,就是称为观察者的模式:
一旦观察的对象发生了变化,就会通知观察者
这种设计模式适用于根据对象进行相应场景的处理...
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

public class Observer {

    public static void main(String[] args) {
        NumberGenerator numberGenerator =
                new RandomNumberGenerator();
        MyObserver myObserver1 = new DigitObserver();
        MyObserver myObserver2 = new GraphObserver();
        numberGenerator.addObserver(myObserver1);
        numberGenerator.addObserver(myObserver2);
        numberGenerator.execute();
        /*
        那么我们来看看所谓的"观察者"是怎么回事?
        关键就是在这个number,
        我们要是能够理解为什么数字打印和图打印打出来的个数会是一样的,
        那么Observer模式就基本没什么问题了,
        注意这里的number实际上是一个类的成员变量
        myObserver.update(this);
        这句话就表明两次不同的观察者的打印针对的是同一个生成器对象
        在这个过程中number仅仅只变化了一次,...
        那么这就是Observer设计模式,因为一旦对象有所变化(number的变化)
        两个观察者都会马上察觉到,并对自己的输出做出相应的变化...
         */
    }
}

interface MyObserver {
    void update(NumberGenerator generator);
}

abstract class NumberGenerator {

    private ArrayList observers = new ArrayList();

    public void addObserver(MyObserver myObserver) {
        observers.add(myObserver);
    }

    public void deleteObserver(MyObserver myObserver) {
        observers.remove(myObserver);
    }

    public void notifyObserver() {
        Iterator iterator = observers.iterator();
        while (iterator.hasNext()) {
            MyObserver myObserver
                    = (MyObserver) iterator.next();
            myObserver.update(this);
        }
    }

    public abstract int getNumber();
    public abstract void execute();
}

class RandomNumberGenerator extends NumberGenerator {

    private Random random = new Random();
    private int number;

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public void execute() {
        IntStream.rangeClosed(1,10).forEach(i -> {
            number = random.nextInt(50);
            notifyObserver();
        });
    }
}

class DigitObserver implements MyObserver {
    @Override
    public void update(NumberGenerator generator) {
        System.out.println("DigitObserver:"+generator.getNumber());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class GraphObserver implements MyObserver {
    @Override
    public void update(NumberGenerator generator) {
        System.out.println("GraphObserver:");
        int count = generator.getNumber();
        IntStream.rangeClosed(1,count).forEach(i -> {
            System.out.print("*");
        });
        System.out.println();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}