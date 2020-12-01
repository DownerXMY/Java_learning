package Java.JVM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClassLoaderFinalApp {

    public static void main(String[] args)
            throws ClassNotFoundException,
            SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection =
                DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/mytestdb",
                        "username",
                        "passsword");
        /*
        分析一下这两行代码,在底层发生了哪些事情?
        ----------------------
        我们先来说一下第一句代码的"动作":
        1.默认参数为true,所以是会将"com.mysql.jdbc.Driver"初始化
        2.初始化会执行类中的静态成员变量,静态代码块...
            我们会看到其中就会有一个静态代码块:
                static {
                    try {
                        DriverManager.registerDriver(new Driver());
                    } catch (SQLException var1) {
                        throw new RuntimeException("Can't register driver!");
                    }
                }
            注意这里的registerDriver是一个静态的线程安全的方法,
            这意味着DriverManager同样也会被加载并初始化,
            那么这件事情就复杂了,
            我们可以看到DriverManager中有一大堆的静态变量和静态代码块...
            我们尤其关注其中的静态代码块:
                static {
                    loadInitialDrivers();
                    println("JDBC DriverManager initialized");
                }
            等到loadInitialDrivers()执行完成之后,基本就完成了对第一句代码的解读...
        ------------------------
        第二句代码也可以按照类似的方式分析...
         */
    }
}
