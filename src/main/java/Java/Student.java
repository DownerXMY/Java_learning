package Java;

public class Student {
    // 访问控制: private, protected, public
    public String name; // 任意包的所有类可见
    protected String sexure; // 只有在同一包内的类和子类可见
    int age;
    private float score; // 只要在同一类内可见

    // 构造方法，可以没有返回值
    // 当实例化(new)的时候，就是需要用到构造方法
    Student(String name1,String sexure1,int age1,float score1) {
        name = name1;
        sexure = sexure1;
        age = age1;
        score = score1;
    }
    // 普通方法，必须要有返回值
    void name2score() {
        System.out.println(name + " is of age " + age + " and gets the score " + score);
    }
    public static void main(String[] args) {
        Student MingyueXu = new Student("MingyueXu","Male",20,100f);
        System.out.println(MingyueXu.name);
        MingyueXu.name2score();
    }
}
