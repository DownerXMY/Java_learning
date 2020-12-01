package Java;

public class generic_paradigm extends getType{

    public static void main(String[] args) {
        // 定义一个方法，能够接受所有类型的数据
        // 那么在引入"泛型"之前，我们先来看看一种比较复杂的方式:
        // 那就是之前介绍过的装箱
        Point point = new Point();
        point.setX(10);
        int x = (Integer)point.getX();
        System.out.println(getType(x));
        point.setY(12.34);
        double y = (Double)point.getY();
        System.out.println(getType(y));
        point.setZ(123);
        String z = point.getZ().toString();
        System.out.println(getType(z));
        System.out.println("-------------------------------");

        // 多么复杂啊...
        // 使用"泛型"就是一句话，叫做想干什么就干什么!!!
        // 比如说，我们想搞一个String和一个Double
        System.out.println("");
        GP<String,Double> gp1 = new GP<>();
        gp1.setX("hello");
        gp1.setY(123d);
        System.out.println(getType(gp1.getX()));
        System.out.println(getType(gp1.getY()));
        double d = 123d;
        System.out.println(gp1.getY() + d);
        String s = "world";
        System.out.println(gp1.getX() + " " + s);
        System.out.println(gp1.printPoint(gp1.getX(),gp1.getY()));
        System.out.println("--------------------------------");

        // 泛型接口
        InfoImp infpimp = new InfoImp<>();
        infpimp.var = "hello";
        System.out.println(infpimp.getVar());
        System.out.println(getType(infpimp.getVar()));
    }
}

class Point {
    Object x = 0;
    Object y = 0;
    Object z = 0;
    public Object getX() {
        return x;
    }
    public void setX(Object x) {
        this.x = x;
    }
    public Object getY() {
        return y;
    }
    public void setY(Object y) {
        this.y = y;
    }
    public Object getZ() {
        return z;
    }
    public void setZ(Object z) {
        this.z = z;
    }
}

// 什么叫做"泛型"类？
// 换句话说，这里我们并没有指定T1和T2的类型
class GP<T1,T2> {
    T1 x;
    T2 y;
    public T1 getX() {
        return x;
    }
    public void setX(T1 x) {
        this.x = x;
    }
    public T2 getY() {
        return y;
    }
    public void setY(T2 y) {
        this.y = y;
    }

    // 还可以有泛型方法！
    public <T1,T2> String printPoint(T1 x,T2 y) {
        T1 m = x;
        T2 n = y;
        return "The cordinate is "+"("+m+","+n+")";
    }
}

// All in all, one may consider the generic paradigm as:
// String test(String str) {...}
//            ||
//            ||
//            \/
// String <T> test(T str) {...}
// The type T here will be claimed afterwords.

// 当然你会发现还要泛型接口，这就是意料之中的了
interface Info<T> {
    // 接口中的方法或者属性的类型不声明，做个T标记
    public T getVar();
}
class InfoImp<T> implements Info<T> {
    // 其实这里还有泛型类的使用(InfoImp(T))
    T var;
    public void InfoImp(T var) {
        this.setVar(var);
    }
    public void setVar(T var) {
        this.var = var;
    }
    public T getVar() {
        return this.var;
    }
}
