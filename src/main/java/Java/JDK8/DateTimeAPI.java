package Java.JDK8;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/*
Java8出了一套全新的日期与时间API,完全取代了之前的
最大的变化是,现在的日期和时间是线程安全的,但原来的不是
 */
public class DateTimeAPI {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyyMMdd");
        // 我们来看一下多线程并发状态下时间和日期是不是线程安全的
        ExecutorService executorService =
                Executors.newFixedThreadPool(10);
        List<Future<Date>> futureList = new ArrayList<>();
        Lock lock = new ReentrantLock();
        IntStream.rangeClosed(1,10).forEach(i -> {
            futureList.add(executorService.submit(() -> {
                Date date = null;
                lock.lock();
                try {
                    date = simpleDateFormat.parse("20201017");
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                return date;
            }));
        });

        for (Future<Date> dateFuture : futureList) {
            System.out.println(dateFuture.get());
        }
        executorService.shutdown();
        System.out.println("------------------------------");

        // 上面是我们以前的写法,为了确保线程安全,我们要加锁!!!
        // 那么在JDK8以后,新的一套时间与日期API就不需要我们加锁了...

        ExecutorService executorService1 =
                Executors.newFixedThreadPool(10);
        List<Future<LocalDate>> futureList1 = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyyMMdd");
        IntStream.rangeClosed(1,10).forEach(i -> {
            futureList1.add(executorService1
                    .submit(() -> LocalDate.parse(
                            "20201017",dateTimeFormatter)
            ));
        });

        for (Future<LocalDate> localDateFuture : futureList1) {
            System.out.println(localDateFuture.get());
        }

        executorService1.shutdown();
        // 这个时候就不需要上锁了,这也是LocalDate带来的新优势...
    }
}
