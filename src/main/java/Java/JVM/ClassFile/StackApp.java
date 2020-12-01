package Java.JVM.ClassFile;

/*
    基于栈的指令集与基于寄存器的指令集的关系与区别:
    1.Java执行指令的时候采取的是基于栈的指令
    2.基于栈的指令集有"入栈"和"出栈"两种
    3.基于栈的指令集的优势是"可移植性"
    4.基于寄存器的指令集的优势是与硬件架构相关联
        是直接由CPU来执行的,加快速度
    5.基于栈的指令集数量要比基于寄存器的指令集的多很多
*/

public class StackApp {

    public int demo() {
        int a = 1;
        int b = 2;
        int c = 3;
        int d = 4;

        int result = (a + b - c) * d;
        return result;
    }

    public static void main(String[] args) {
        StackApp stackApp = new StackApp();
        int result = stackApp.demo();
        System.out.println(result);
    }
}
