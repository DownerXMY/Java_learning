package Java.JVM;

import java.util.Random;

public class ClassLoaderApp7 {

    public static void main(String[] args) {
        System.out.println(Children.b);
    }
}

interface Parents {
    int a = 5;
}

interface Children extends Parents {
    public static int b = new Random().nextInt(5);
}

/*
运行,然后删掉Parents.class,然后再运行,会发生什么?
 */
