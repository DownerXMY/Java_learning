package Java.JVM.GC;

import javax.swing.*;
import java.sql.*;

/*
内存泄漏的常见问题:
-----------------------
我们看到下面那个Fool类和Fool1类,看到他们俩的区别
其实就是因为我们看到类的成员变量其实只是被doIT这一个方法所应用的,
这样就会导致内存泄漏,所以我们不如将其设置成局部变量...
-----------------------
当然我们还有别的导致内存泄漏的问题:
什么叫做"异常处理不当":
假如我们这么写,那么会发生什么问题呢？
如果说在while中的代码抛出了异常,那么将会直接进入到catch,
这样子的话两个close将不会再被调用,
所以正确的做法是将资源关闭放入到finally代码块中...
-----------------------
什么叫做"集合数据管理不当"?
当我们使用Array-based的数据结构的时候,应该尽量减少resize,这是成本比较高的...
-----------------------
当然这样的导致内存泄漏的问题还有很多
 */
public class MemoryLose {

    public static void main(String[] args)
            throws SQLException {
        Connection connection = DriverManager
                .getConnection("https://localhost:9060");
        try {
            String sql = "do a query on sql";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            ResultSet resultSet =
                    preparedStatement.executeQuery();
            while(resultSet.next()) {
                System.out.println("Hello world");
            }
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Fool {
    private String[] names;
    public void doIt(int length) {
        if (names == null || names.length < length) {
            names = new String[length];
        }
        System.out.println(names);
    }
}

class Fool1 {
    public void doIt(int length) {
        String[] names = new String[length];
        System.out.println(names);
    }
}