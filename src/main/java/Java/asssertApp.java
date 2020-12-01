package Java;

import java.util.stream.IntStream;

public class asssertApp {

    public static void main(String[] args) {
        // JAVA断言
        for (int item : IntStream.rangeClosed(1,2).toArray()) {
            int x = item;
            if (args.length > 0) {
                try {
                    x = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.out.println("Exception:"+e);
                }
            }
            System.out.println("testing assertion:");
            assert x == 2: "Our assertion failed";
            System.out.println("test passed");
        }
        // assert 布尔表达式: "断言错了的描述"
        // assert 如果错误，将抛出"AssertionError"
    }
}
