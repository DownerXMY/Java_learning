package Java.JDK8;
/*
记不记得我们之前在并发中学习的ForkJoinPool,
其能够大幅度提高并发效率的关键就在于"线程任务窃取机制"
然而在Java8中,引入了并行流的手段能够同样帮助我们解决这样的事情...
 */

import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

public class ParallelStream {

    public void demo1() {
        Instant start1 = Instant.now();
        int result1 = IntStream.rangeClosed(0,1000000000)
                .reduce(0,Integer::sum);
        Instant end1 = Instant.now();
        System.out.println(result1);
        System.out.println(Duration.between(start1,end1).toMillis());
        System.out.println("----------------------------");
        // 上面这种叫做"顺行流",其运行效率是低下的
        // 我们想要做成并行流
        // 实际上使用.parallel()和.sequential()可是在顺行流和并行流之间切换
        // 但是这里要说明的是.parallel()的底层实现就是ForkJoin框架
        // 我们顺便通过计算时间来比较一下两者效率的差距...
        Instant start2 = Instant.now();
        int result2 = IntStream.rangeClosed(1,1000000000)
                .parallel()
                .reduce(0,Integer::sum);
        Instant end2 = Instant.now();
        System.out.println(result2);
        System.out.println(Duration.between(start2,end2).toMillis());
    }

    public static void main(String[] args) {
        ParallelStream parallelStream = new ParallelStream();
        parallelStream.demo1();
        // 所以可以想象用Java8来处理大数据将是以后很主流的趋势...
    }
}
