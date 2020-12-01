package Java;

public class Inheritance_polymorphism extends getType{

    // OO language has three most important features:
    // respectively, pack / Inheritance / polymorphism
    public static void main(String[] args) {
        // Are there anythings that cannot be inherited?
        // (1).private; (2).构造方法;
        // 虽然private不能被继承，但是用到private的普通方法依旧可以生效
        Students MingyueXu = new Students();
        MingyueXu.name = "MingyueXu";
        MingyueXu.age = 20;
        MingyueXu.score = 100;
        MingyueXu.func2();
        String sex = MingyueXu.getsex();
        System.out.println(sex);

        // What is "super"?
        // Something similar to "this" but has some differences.
        // "this"最常用的功能是表示对象，"super"最常用的功能则是表示父类
        System.out.println("-------------------------------");


        // Moreover, to see something interesting:
        People HaoyuShi = new People();
        HaoyuShi.name = "HaoyuShi";
        HaoyuShi.age = 20;
        System.out.println(HaoyuShi.func1());
        HaoyuShi = new Students();
//        System.out.println(HaoyuShi.getsex()); ---- WRONG!!!
        // 虽然已经new了，但是还是不能调用子类中的方法，
        // 除非，采用同名函数，就能实现覆盖！
        System.out.println(HaoyuShi.func1());
        // HaoyuShi can be of People, also Students
        // This is exactly called "polymorphism".

        // 可以看出，这样子操作的话，HaoyuShi实际上还是一个People对象
        // 那么，怎么转换成Students的对象呢？答案是不可以！！！
    }
}

class People {
    public String name;
    int age;
    private String sexure = "man";
    private String school;

    String func1() {
        return name+" is a "+sexure+" of age "+age;
    }
    String sex() {
        return sexure;
    }
}
class Students extends People {
    protected float score;
    String getsex() {
//        return this.sexure; ---- This is WRONG!!!
//        return super.sexure; ---- Also WRONG!!!
        // What is the true method to get the private attribution?
        // 必须在父类中写一个获取方法，比如这里的sex
        return super.sex();
        // 有趣的事情又来了：
        // 有人发现这里用 return this.sex() 好像也可以...
    }
    String func1() {
        return super.sex();
    }
    void func2() {
        System.out.println(this.func1()+" and gets the score "+score);
    }
}
