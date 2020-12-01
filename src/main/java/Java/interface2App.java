package Java;

public class interface2App {

    // 这里接口(demo_A)就是作为类型在使用
    public int test(demo_A a) {
        int result = a.doSth();
        return result;
    }

    public static void main(String[] args) {
        SeagateHdd sh1 = new SeagateHdd();
//        SataHdd sh_test = new SataHdd(); ----接口不能被实例化!!!
        SataHdd sh2 = new SeagateHdd(); // 初始化希捷硬盘
        SataHdd sh3 = new SamSungHdd(); // 初始化三星硬盘
        System.out.println(sh1.readData());
        sh1.writeData("Hello");
        System.out.println(sh1.CONNECT_LINE);
        System.out.println(sh1.address);
        System.out.println(sh1.doFix());
        // 但是sh2就没有address的属性，因为我们new的时候是用的SataHdd
        interface2App test = new interface2App();
        demo_A a = new demo_B();
        int result = test.test(a);
        System.out.println(result);
    }
}

// 串行硬盘接口
interface SataHdd {
    // 连接线的数量
    public static final int CONNECT_LINE = 4;
    // 读写文件
    public void writeData(String data);
    public String readData();
}

// 维修硬盘接口
interface fixHdd {
    public String address = "北京市海定区";
    // 开始维修
    public boolean doFix();
}

// 希捷硬盘
class SeagateHdd implements SataHdd, fixHdd {
    // 这就实现了class的多继承，实际上这更加方便适应了实际中的开发
    // 在Scala中，我们采用extends...with...来实现多继承trait
    public String readData() {
        return "数据";
    }
    public void writeData(String data) {
        System.out.println("写入成功");
    }
    public boolean doFix() {
        return true;
    }
}

// 三星硬盘
class SamSungHdd implements SataHdd {
    public String readData() {
        return "数据";
    }
    public void writeData(String data) {
        System.out.println("写入成功");
    }
}

// 除此之外，接口还可以作为类型使用
interface demo_A {
    public int doSth();
}

class demo_B implements demo_A {
    public int doSth() {
        System.out.println("Now in B");
        return 123;
    }
}

// 最后，抽象类和接口相比较，有以下比较重要的区别:
// 接口可以多继承，但是抽象类只能单继承
// 抽象类中的抽象方法可以被实例化，但是接口中的抽象方法只能被声明
// 实际中，我们多数肯定会采用单继承和多继承混合的方式:
// classA extends classB implements interfaceC, interfaceD, ...