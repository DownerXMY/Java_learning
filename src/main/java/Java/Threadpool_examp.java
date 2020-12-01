package Java;

import java.util.concurrent.*;

// 内置线程池案例:
// 背景:
// 模拟20个人抢10个商品，有人成功，有人失败
public class Threadpool_examp {
    public static void main(String[] args) {
        // 介绍第二种内置的创建线程池的方式
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                3,
                5,
                1,TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(15)
        );
        for (int i=1;i<=20;i++) {
            diliver dl = new diliver("user"+i);
            pool.submit(dl); // 类似的提交方式，可以看到内置的线程池还是非常方便的
        }
        pool.shutdown(); // 关闭线程池
    }
}

class diliver implements Runnable {
    private String username;
    private static int id = 10; // 商品数量只有10个
    public diliver(String name) {
        this.username = name;
    }
    public void run() {
        String thd_name = Thread.currentThread().getName();
        System.out.println(username+" is on thread "+thd_name+" anticipating");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(diliver.class) {
            if (id>0) {
                System.out.println(username+" has got the bless of rest "+id--);
            } else {
                System.out.println(username+" has failed!");
            }
        }
    }
}