package Java;

public class generic_paradigm2 extends getType{

    public static void main(String[] args) {
        // 让我们不声明泛型的数据类型
        Point_1 p1 = new Point_1(); // 类型擦除，并向上转型为Object
        p1.setX(10);
        p1.setY("hello");
        System.out.println(getType(p1.x));
        System.out.println(getType(p1.y));
        // 虽然上面打印出来的数据类型是对的，但是下面这样的操作还是不行，因为它是object
//        int res = p1.getX() + 10;
        // 这时候都是Object，所以我们可以进一步装箱
        int x = (Integer)p1.getX();
        String y = p1.getY().toString();
        System.out.println(getType(x));
        System.out.println(getType(y));
        System.out.println("--------------------------------");
        // 类型限制
        generic_paradigm2 ob1 = new generic_paradigm2();
        System.out.println(ob1.todouble(10));
        System.out.println(ob1.Myadd(p1,10));
    }

    // 我们还可以限制泛型的可用类型
    // 比如我们做一个web，我们肯定不希望用户注册的用户名中包含float，这就很奇怪
//    public <T> double todouble(T ini) {
//        double result = ini.doubleValue();
//        return result;
//    }
    // 这个时候是报错的，因为：
    // doubleValue实际上是只适用于Number类的方法，
    // 如果不做限制，很可能是行不通的
    // 我们通过extends做泛型的类型限制
    public <T extends Number> double todouble(T ini) {
        return ini.doubleValue();
    }

    // 通配符"？"
    // 什么叫做通配符，大白话就叫做百搭或者万金油!!!
    // ?可以表示任何数据类型，将代码补充完整.
    // 我们知道一旦类型擦除了，那就相当于没用泛型，这是很糟糕的事情
    // 通配符就能够很好地避免类型擦除的问题
    public String printPoint(Point_1<?,?> p2) {
        return "The cordinate is ("+p2.getX()+","+p2.getY()+")";
    }
    public int Myadd(Point_1<? extends Integer,?> p3, int ori) {
        int res1 = p3.getX() + ori;
        return res1;
    }
}

// There are so many things in GP:
// 类型擦除，就是在使用泛函的时候没有声明类型的情况下发生的事情
class Point_1<T1,T2> {
    T1 x;
    T2 y;
    public T1 getX() {
        return x;
    }
    public T2 getY() {
        return y;
    }
    public void setX(T1 x) {
        this.x = x;
    }
    public void setY(T2 y) {
        this.y = y;
    }
}