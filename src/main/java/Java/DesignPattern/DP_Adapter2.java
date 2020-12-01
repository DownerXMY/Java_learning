package Java.DesignPattern;

/*
我们想要实现的静态代理是什么样的需求:
实际上很简单,就是我们希望为某个类的方法前后加上打印日志,
但是不希望去改变类本身(甚至是不能改变,比如是某公司的保密代码)
*/

public class DP_Adapter2 {

    public static void main(String[] args) {
        /*
        那么这个时候我们想要计算两个数的乘法
        然后我们还想要判断乘法是不是由意义(打印出对应的判断日志)
        但同时比如说Unchangeable服务是从别的公司引进来的,
        那么我们没有办法改变源码,就只能走"静态代理"的方式
        另外一点我们之前也反复强调过,
        那就是我们想要改什么就直接去newMultiple()中改
        之前的Unchangeable类中的一切就当他消失了吧...
        让我们执行下面的代码看看会发生什么事情
        */
        Proxy2Unchangeable proxy2Unchangeable =
                new Proxy2Unchangeable(3,5);
        proxy2Unchangeable.newMultiple();
        System.out.println("--------------------");
        Proxy2Unchangeable proxy2Unchangeable1 =
                new Proxy2Unchangeable(0,7);
        proxy2Unchangeable1.newMultiple();
    }
}

class Unchangeable {

    private int a;
    private int b;

    public Unchangeable(int a,int b) {
        this.a = a;
        this.b = b;
    }

    // 就比如说最简单的乘法的业务
    public void multiple(int a,int b) {
        int result =  a * b;
        System.out.println(result);
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}

interface Static_Proxy {
    void newMultiple();
}

class Proxy2Unchangeable implements Static_Proxy {

    private Unchangeable unchangeable;

    public Proxy2Unchangeable(int num1,int num2) {
        this.unchangeable = new Unchangeable(num1,num2);
    }

    @Override
    public void newMultiple() {
        if (unchangeable.getA() == 0 ||
                unchangeable.getB() == 0) {
            System.out.println("The multiplicaiton is of none significance...");
        } else {
            unchangeable.multiple(
                    unchangeable.getA(),unchangeable.getB());
            System.out.println("Finished...");
        }
    }
}