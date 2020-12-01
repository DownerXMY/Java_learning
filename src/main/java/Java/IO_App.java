package Java;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileDescriptor;

import java.io.IOException;
import java.io.FileNotFoundException;

public class IO_App {

    static public void readfile(String filename) throws FileNotFoundException,IOException {
        char a[] = new char[100]; //创建可容纳 1000 个字符的数组
        FileReader b = new FileReader(filename);
        int num = b.read(a); //将数据读入到数组 a 中，并返回字符数
        System.out.println("读取的字符个数为："+num+",内容为：\n");
        String str=new String(a); //将字符串数组转换成字符串
        System.out.println(str);
    }
    public static void main(String[] args) throws FileNotFoundException,IOException {
        // JAVA输入输出操作(IO)
        // InputStream,OutStream,Reader,Writer,File

        // 读取纯文本文件
        // 注意你的文件必须放在configuration的工作目录之下
        readfile("testtxt.txt");
        // 综合利用File,FileWriter,Filereader
        char a2[] = new char[100];
        File file = new File("demotest.txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("hello world \n I love you");
        fw.flush();
        fw.close();
        FileReader fr = new FileReader(file);
        int num2 = fr.read(a2);
        System.out.println("How many: " + num2);
        String str2 = new String(a2);
        System.out.println(str2);
        System.out.println("------------------------------");

        // 读取缓冲区中的数据
        String OneLine;
        int count = 0;
        FileReader a = new FileReader("testtxt.txt");
        BufferedReader b = new BufferedReader(a);
        while ((OneLine = b.readLine()) != null) {// 这种写法值得注意并掌握
            // .readLine()表示读取一行
            count++;  //计算读取的行数
            System.out.println(OneLine);
        }
        System.out.println("\n 共读取了" + count + "行");
        b.close();

        // 读取键盘中的输入，当看到"#"的时候停止，然后在屏幕上显示所有读取结果
        char ch;
        int data;
        FileInputStream a3 = new FileInputStream(FileDescriptor.in);  //创建文件输入流对象
        FileOutputStream b3 = new FileOutputStream("output");  //创建文件输出流对象
        System.out.println("请输入字符，以#号结束：");
        while ((ch = (char) a3.read()) != '#') {
            b3.write(ch);
        }
        a3.close();
        b3.close();
        FileInputStream c = new FileInputStream("output");
        FileOutputStream d = new FileOutputStream(FileDescriptor.out);
        while (c.available() > 0) {
            data = c.read();
            d.write(data);
        }
        c.close();
        d.close();

        // 标准的输入和输出
        byte a5[]=new byte[128];  //设置输入缓冲区
        System.out.print("请输入字符串：");
        int count2 =System.in.read(a5);  //读取标准输入输出流
        System.out.println("输入的是：");
        for(int i=0;i<count2;i++)
            System.out.print(a5[i]+"");  //输出数组元素的 ASCII 值
        System.out.println();
        for(int i=0;i<count2-2;i++)  //不显示回车和换行符
            System.out.print((char)a5[i]+"");  //按字符方式输出元素
        System.out.println();
        System.out.println("输入的字符个数为："+count2);
        Class InClass=System.in.getClass();
        Class OutClass=System.out.getClass();
        System.out.println("in 所在的类为："+InClass.toString());
        System.out.println("out 所在的类为："+OutClass.toString());
    }
}
