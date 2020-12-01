package Java;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

// 自定义线程池
public class Threadpool_test {
    public static void main(String[] args) {
        Thread_pools tp1 = new Thread_pools(2,4,20);
        for (int i=0;i<30;i++) {
            task T1 = new task(i);
            tp1.submit(T1);
        }
    }
}

class Thread_tasks extends Thread {
    // thread class
    private String name;
    private List<Runnable> tasks;

    Thread_tasks(String name1, List<Runnable> tasks1) {
        super(name1);
        this.tasks = tasks1;
    }
    public void run() {
        while(tasks.size() > 0) {
            Runnable r1 = tasks.remove(0);
            r1.run();
        }
    }
}

class Thread_pools {
    // Thread pools class
    private List<Runnable> pools =
            Collections.synchronizedList(new LinkedList<>());
    // Note that "synchronized" method is to ensure the safety of the Thread.
    private int temp_size; // 当前线程数量
    private int core_size; // 核心线程数量
    private int max_size; // 最大线程数量
    private int running_size; // 运行线程数量

    public Thread_pools(int core_size1,int max_size1,int running_size1) {
        this.core_size = core_size1;
        this.max_size = max_size1;
        this.running_size = running_size1;
    }

    public void submit(Runnable r) {
        if (pools.size() >= running_size) {
            System.out.println("task "+r+" has been abandoned!");
        } else {
            pools.add(r);
            exectask(r);
        }
    }
    public void exectask(Runnable r) {
        if (temp_size < core_size) {
            new Thread_tasks("core " + temp_size, pools).start();
            temp_size++;
        } else if (temp_size < max_size) {
            new Thread_tasks("non-core "+temp_size,pools).start();
            temp_size++;
        } else {
            System.out.println("task "+r+" has been load in memory");
        }
    }
}

class task implements Runnable {
    // task class
    private int id;
    task (int id1) {
        this.id = id1;
    }
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println("Thread "+name+" is going to run task "+id);
        try {
            int result = 0;
            for (int item : IntStream.rangeClosed(1,5).toArray()) {
                result += item;
            }
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }
        System.out.println("Thread "+name+" has finished task "+id);
    }
    @Override
    public String toString() {
        return "{ " +
                "id=" + id +
                " }";
    }
}
// 这就是线程池(Thread Pooling),它相当于起到了一个调度的作用:
// 给空着的线程尽可能地分配任务;
// 暂时还没有被执行的任务将会被缓存;
// 但是实在是有太多任务，那么一小部分将会被丢弃;
// 实际上，JAVA也有内置的线程池