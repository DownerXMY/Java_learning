package Java;

public class Student2 {
    // 变量作用域: static(类级),default(对象实例级),
    public static String name;
    public int age;
    protected float score;

    // 构造方法
    Student2(String name1, int age1, float score1) {
        name = name1;
        age = age1;
        score = score1;
    }
    // 普通方法
    void name2score() {
        int num = 0; // 方法级
        if (num == 0) {
            int id = num; // 块级
            System.out.println(id);
        }
        System.out.println(name+" is of age "+age+" and gets the score "+score);
    }

    // What is "this"?
    // How many functions do "this" have?
    void standard() {
        // 1.代表对象
        float score_final = this.score*101/99;
        System.out.println(score_final);
    }
    public Student2() {
        // 2.作为方法名来初始化对象
        this("Downer",20,100.0f);
    }
    // 这里我们还发现了一个有趣的事实，JAVA的一个类下可以有多个名字相同的方法，只要他们的参数列表不同就行
    // 这成为"方法重载"，这一点非常重要，我们单独写一个例子文件。

    public static void main(String[] args) {
//        System.out.println(age);
//        System.out.println(age1);
//        The above languages are wrong, one cannot read the var in class
        System.out.println(name);
//        注意了，name是可以访问的，而它和age唯一的区别就是在于有没有声明是static.
//        One does not need to realize an class to read the static var.
        Student2 MingyueXu = new Student2("MingyueXu",20,100.0f);
        MingyueXu.standard();
    }
}
