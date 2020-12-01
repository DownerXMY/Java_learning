package Java.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class FJPApp {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        number_class nc = new number_class(1,13);
        int result = forkJoinPool.invoke(nc);
        System.out.println(result);
        forkJoinPool.shutdown();
    }
}

class number_class extends RecursiveTask<Integer> {
    private int limit = 4;
    private int ini;
    private int end;
    private Lock lock = new ReentrantLock();

    public number_class(int n1,int n2) {
        this.ini = n1;
        this.end = n2;
    }

    @Override
    protected Integer compute() {
        int result;
        int distance = end - ini;

        if (distance <= limit) {
            // 我们要看看复杂任务是不是由多个线程共同完成...
            int result1 = IntStream.rangeClosed(ini,end).sum();
            StringBuffer str = new StringBuffer();
            IntStream.rangeClosed(ini,end).forEach(i -> {
                str.append(Integer.toString(i)+"+");
            });
            System.out.println(Thread
                    .currentThread().getName()+" "+str.deleteCharAt(str.length()-1)+"="+result1);
            result = result1;
        } else {
            int nextPoint = ini + limit;
            number_class leftnc = new number_class(ini,nextPoint);
            number_class rightnc = new number_class(nextPoint+1,end);

            // 这是最重要的一行,表示进行递归...
            // 换句话说,invokeAll()会一直去调用.compute()
            invokeAll(leftnc,rightnc);

            // 接下去我们要把各部分计算的结果合并...
            int left_result = leftnc.join();
            int right_result = rightnc.join();
            result = left_result + right_result;
            }
        return result;
    }
}
