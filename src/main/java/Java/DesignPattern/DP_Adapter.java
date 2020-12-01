package Java.DesignPattern;

public class DP_Adapter {

    public static void main(String[] args) {
        /*
        如果想要额定工作电压是12V的笔记本电脑在交流100V的电源下工作
        我们通常的做法是会使用AC适配器,这就能引入我们接下去要介绍的设计模式了:
        Adapter模式,它位于实际情况与需求之间,填补两者之间的空白
        "Adapter模式"也被称为"Wrapper模式"(包装器的意思)
        Adapter模式有两种,后面都会依次学习到:
            类适配器模式(使用继承的适配器)
            对象适配器模式(使用委托的适配器)
        */
        Print print = new PrintBanner("Hello world");
        print.printWeak();
        print.printStrong();
        /*
        那么这就有意思了,我们好像没看到什么特别惊奇的操作
        而且正常人都会想,干这件事情还需要这么麻烦?根本不需要额外搞出别的类和接口
        那么和之前一样,我们要说明Adapter接口的优势和意义:
            深入思考一下,我们就会发现实际上
            在main方法中,我们压根不知道有Banner的存在
            类比过去就是笔记本电脑并不知道实际上它现在工作的12V电压
            实际上是由AC适配器将100V的电压转换过去得到的
            或者反过来说,我们不需要改变main中的动作,
            只要改变Banner中的方法就能实现print的变化,
            这样的委托或者说隐藏总是有意义的...
            还有一点值得强调的是:使用Adapter模式创建的程序,
            一旦发生故障,我们只需要检查扮演了"被委托者"角色的类有没有BUG即可
            因为那些"委托者"肯定是适用于很多场景的服务,肯定是经过多方验证不可能出错的
            这样一来就大大降低了维护的成本...
         */
        System.out.println("----------------------");
        /*
        我们要说的第二种Adapter模式就是采用"委托"的机制
        当然不要跟双亲委托扯上关系,实际上这和静态代理可能更加相近
        比如我们看到下面的Banner,Print1和PrintBanner1
        我们看到这个时候PrintBanner1所有要做的事情都委托给了Banner去做
        换句话说,这个时候Banner实际上就成了PrintBanner1的静态代理
        下一次我们想要改变PrintBanner1的行为,只需要去改变代理就好...
        当然这个我们会在DP_Adapter2.java中重写一下静态代理的内容
        方便我们真正理解Adapter模式的意义...
         */
        PrintBanner1 printBanner1 =
                new PrintBanner1("Hello world");
        printBanner1.printWeak();
        printBanner1.printStrong();
    }
}

class Banner {

    // Banner类表示现在的实际情况

    private String string;

    public Banner(String string) {
        this.string = string;
    }

    public void showWithParen() {
        System.out.println("("+string+")");
    }

    public void showWithAster() {
        System.out.println("*"+string+"*");
    }
}


interface Print {

    // Print接口表示的是我们的需求

    void printWeak();
    void printStrong();
}

class PrintBanner extends Banner implements Print {

    /*
    我们再回顾一下,接口的实现我们已经很熟悉了,
    但是类的继承其实很少用,不顾我们还是得记住,
    类的继承必须要实现一个构造方法将父类的成员变量继承下来
     */
    public PrintBanner(String string) {
        super(string);
    }

    @Override
    public void printWeak() {
        showWithParen();
    }

    @Override
    public void printStrong() {
        showWithAster();
    }
}

abstract class Print1 {

    abstract void printWeak();
    abstract void printStrong();
}

class PrintBanner1 extends Print1 {

    private Banner banner;

    public PrintBanner1(String string) {
        this.banner = new Banner(string);
    }
    @Override
    void printWeak() {
        banner.showWithParen();
    }

    @Override
    void printStrong() {
        banner.showWithAster();
    }
}