package Java;

// 串行硬盘接口
public interface interfaceApp extends A,B{
    // 接口可以看做是一个特殊的抽象类
    // 接口中只能定义抽象方法
    // 注意，像下面的CONNECT_LINE是静态常量，必须被初始化(=4)
    // 而在接口中的静态常量默认都是public static final的，可以不声明
    // 接口中只能定义抽象方法，这些方法默认都是public abstract的
    // 接口相比于类，具有多继承的特性
    // 最后，可与把JAVA中的interface看做是Scala中的trait.
    public static final int CONNECT_LINE = 4;
    public void writeData(String data);
    public String readData();
}
interface A {
    public void a();
}
interface B {
    public void b();
}

// Why we have to use interface?
// 假设以下是class的继承：
// A => B => C => D => E
// 想给C加一些功能，可以让C继承一个类C1，但问题是JAVA是单继承的语言，
// 所以如果你还想给C加功能，这时候只能从A或者B下手了。
// 我们在interface2App中来看一下这个问题的"接口"解决！