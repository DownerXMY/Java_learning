package Java;

import java.util.concurrent.*;

public class Threadpool_SWFD {
    // JAVA内置线程池
    public static void main(String[] args) {
        // 延迟执行任务
        ScheduledExecutorService ses =
                Executors.newSingleThreadScheduledExecutor(
                        new ThreadFactory() {
                            // 采用"线程工厂"造线程
            int m = 1;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "thread " + m);
            }
        });
        ses.scheduleWithFixedDelay(new Mythread(1),
                1,
                2,
                TimeUnit.SECONDS);
        // 初始延迟时间1s,每间隔时间2s,注意任务执行时间是不计入间隔时间之内的
        System.out.println("over");
    }
}

class Mythread implements Runnable {
    private int id;
    public Mythread(int id1) {
        this.id = id1;
    }
    public void run() {
        String name = Thread.currentThread().getName();
        try {
            Thread.sleep(2000);
            // 所以按照之前的分析，每个任务的真正执行时间是4s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name+" has done the task "+id);
    }
}
// 运行的时候可以数数看，确实是每隔4s打印一次输出