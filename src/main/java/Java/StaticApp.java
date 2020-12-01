package Java;

import static java.lang.Math.random; // 静态导入

public class StaticApp extends getType{
    // 静态块
    static {
        int hello = 1;
        System.out.println("This is a static part");
    }

    public static void main(String[] args) {
        // 我们来看一下静态变量的性质
        Friend HaoyuShi = new Friend();
        HaoyuShi.name = "HaoyuShi";
        HaoyuShi.num = 1;
        Friend QidongSu = new Friend();
        System.out.println(QidongSu.name);
        System.out.println(QidongSu.num);
        // 稀奇了，静态变量居然是能够延续到所有实例化对象的
        System.out.println("--------------------------------");
        // 再来看一下静态方法
        // 实际上在python中也有静态方法，当时我们的认知是，这个方法不需要依赖实例
        // 那么更，在JAVA中，static不仅不依赖于实例，而且就是不能用在实例上。
        // 比如说一个很简单的例子就是，static中不能用this.
        int result1 = Friend.sum(4,5);
        System.out.println(result1);
        int result2 = HaoyuShi.sum(4,5);
        System.out.println(result2);
        System.out.println("--------------------------------");
        // 静态导入
        // 一般我们怎么调用类中的方法？
        // 先import类，然后实例化一个obj，最后obj.method()
        // 但是静态导入，允许我们直接使用静态方法：
        System.out.println("random number: "+random());
        // 实际上我们一直在用的System.out.println也是静态导入
    }
}

class Friend {
    static String name;
    int num;

    static int sum(int x,int y) {
        return x + y;
    }
}
