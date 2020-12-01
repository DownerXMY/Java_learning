package Java.concurrent;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

public class ConditionApp2 {

    public static void main(String[] args) {
        Container CT = new Container();
        for (int i : IntStream.rangeClosed(1,10).toArray()) {
            CA2_thread R1 = new CA2_thread(CT,true);
            CA2_thread R2 = new CA2_thread(CT,false);
            new Thread(R1).start();
            new Thread(R2).start();
        }
    }

}

class Container {
    private String[] elements = new String[10];
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
    private int number = 0;
    private int put_index = 0;
    private int take_index = 0;

    public void put(String element) {
        lock.lock();
        try {
            while(number == 10) {
                notFull.await();
            }
            elements[put_index] = element;
            if (++put_index == 10) {
                put_index = 0;
            }
            number++;
            System.out.println("put method: "+ Arrays.toString(elements));
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String take() throws InterruptedException {
        lock.lock();
        try {
            while(number == 0 ) {
                notEmpty.await();
            }
            String result = elements[take_index];
            elements[take_index] = null;
            if (++take_index == 10) {
                take_index = 0;
            }
            number--;
            System.out.println("take method: "+Arrays.toString(elements));
            notFull.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }
}

class CA2_thread implements Runnable {
    private Container C = new Container();
    boolean judge;
    CA2_thread(Container CR,boolean jd) {
        this.C = CR;
        this.judge = jd;
    }
    public void run() {
        if (judge) {
            try {
                Thread.sleep((long)Math.random()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int item : IntStream.rangeClosed(1,30).toArray()) {
                String str = Integer.toString(item);
                C.put(str);
            }
        } else {
            try {
                Thread.sleep((long)Math.random()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int item : IntStream.rangeClosed(1,30).toArray()) {
                try {
                    C.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}