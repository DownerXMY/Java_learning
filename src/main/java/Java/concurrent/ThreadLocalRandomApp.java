package Java.concurrent;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ThreadLocalRandomApp {

    public static void main(String[] args) {
        Random random = new Random();
        IntStream.rangeClosed(1,10).forEach(i -> {
            int result = random.nextInt(100);
            System.out.print(result+" ");
        });
        System.out.println("\n");

        // 看看ThreadLocalRandom是怎么实现的...
        System.out.println("--------------------------------");
        ThreadLocalRandom threadLocalRandom =
                ThreadLocalRandom.current();

        IntStream.rangeClosed(1,10).forEach(i -> {
            System.out.println(threadLocalRandom.nextInt(100));
        });
    }
}
