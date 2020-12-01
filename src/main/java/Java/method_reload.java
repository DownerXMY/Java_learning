package Java;

public class method_reload {
    // 方法重载

    void test() {
        System.out.println("No parameters");
    }
    void test(int a) {
        System.out.println("We have added a parameter: "+a);
    }
    void test(int a,int b) {
        System.out.println(a + b);
    }
    double test(double a) {
        System.out.println(a);
        return a*a;
    }
    public static void main(String[] args) {
        method_reload obj = new method_reload();
        obj.test();
        obj.test(2);
        obj.test(2,3);
        double s = obj.test(12d);
        System.out.println(s);
    }
}
