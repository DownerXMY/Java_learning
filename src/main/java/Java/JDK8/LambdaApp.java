package Java.JDK8;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

import java.util.*;

public class LambdaApp {

    // 在理解什么是Lambda(匿名函数)之前，我们先来回顾一下什么是匿名内部类
    public void test() {
        Comparator<Integer> comparator =
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return Integer.compare(01,02);
                    }
                };
        TreeSet<Integer> treeSet = new TreeSet<>(comparator);
    }
    /*
    以上是我们用匿名内部类的实现
    但是实际上我们会发现其中真正有用的代码只是
    Integer.compare(o1,o2);
    而我们却要写这么复杂的代码...
    现在让我们来看看Lambda匿名函数的实现
     */
    public void test1() {
        Comparator<Integer> comparator1 =
                (x,y) -> Integer.compare(x,y);
        TreeSet<Integer> treeSet1 = new TreeSet<>(comparator1);
    }
    // 非常简洁的代码表示，这就是Lambda的优势...
    // 如果还不是很明显，我们可以再来看一个例子
    Employee employee = new Employee();
    public List<Employee> test2() {
        return employee.filteremployees(Employee.employees);
    }
    public List<Employee> testGen() {
        return employee.filteremployeeGen(Employee.employees);
    }
    // 最终版本
    public List<Employee> testGenNew() {
        return employee.filteremployeeGenNew(
                Employee.employees,
                (e) -> e.salary >= 5000);
    }

    public static void main(String[] args) {
        List<Employee> result = new LambdaApp().testGenNew();
        for (Employee item : result) {
            System.out.println(item.name+" "+item.age+" "+item.salary);
        }
    }
}

// 比如我们现在想要提取员工的年龄大于35岁的员工...
// 让我们来看看用Lambda和不用之间的差距...
class Employee {
    public String name;
    public int age;
    public double salary;

    Employee() {}

    Employee(String name1,int age1,double salary1) {
        this.name = name1;
        this.age = age1;
        this.salary = salary1;
    }
    public static List<Employee> employees =
            Arrays.asList(
                    new Employee("Zhangsan",18,9999.99),
                    new Employee("Lisi",38,6666.66),
                    new Employee("Wangwu",28,5555.55),
                    new Employee("Zhaoliu",16,7777.77),
                    new Employee("Tianqi",50,3333.33)
            );
    public List<Employee> filteremployees(List<Employee> list) {
        List<Employee> emps = new ArrayList<>();
        for (Employee emp : list) {
            if (emp.age >= 35) {
                emps.add(emp);
            }
        }
        return emps;
    }
    /*
    这是不用Lambda的方式...
    现在这么来考虑这件事:
    我现在如果变成要求工资高于5000的员工，
    那么可以想象在上面的方法中唯一的变化将是if语句中的判断条件...
     */
    public List<Employee> filteremployees1(List<Employee> list) {
        List<Employee> emps = new ArrayList<>();
        for (Employee emp : list) {
            if (emp.salary >= 5000.00) {
                emps.add(emp);
            }
        }
        return emps;
    }
    // 那么这个事情就不是那么理想了，
    // 用专业的话说，就是出现了相当一部分的"冗余代码"!!!
    // 我们用interface可以解决这样的问题
    public List<Employee> filteremployeeGen(List<Employee> list) {
        List<Employee> emps = new ArrayList<>();
        for (Employee emp : list) {
            if (new generaljudge2().test(emp)) {
                emps.add(emp);
            }
        }
        return emps;
    }

    // 将接口传入方法...
    public List<Employee> filteremployeeGenNew(List<Employee> list,
                                               judge<Employee> jd) {
        List<Employee> emps = new ArrayList<>();
        for (Employee emp : list) {
            if (jd.test(emp)) {
                emps.add(emp);
            }
        }
        return emps;
    }
}

class generaljudge1 implements judge<Employee> {
    @Override
    public boolean test(Employee employee) {
        return employee.salary >= 5000;
    }
}
// 以后我们每一次只要去新创建一个借口的实现就可以了
// 不再需要重写方法了...
// 比如说:
class generaljudge2 implements judge<Employee> {

    @Override
    public boolean test(Employee employee) {
        return employee.age>=30;
    }
}
// 然后到filteremployeeGen中去把generaljudge1()改成generaljudge2()就好了
// 但是还是嫌麻烦，那就是每一次都要去创建一个新的类实现接口，大大降低了代码的可读性
// 有没有更加简便直接的方式呢?
// 实际上我们只需要对filteremployeeGen做一点改变即可...

interface judge<T> {
    public boolean test(T t);
}
