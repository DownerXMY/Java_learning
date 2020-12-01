package Java.DesignPattern;
/*
程序这东西总是会越变越大的,
我们加上的实现越多,程序代码之间的就够会变得越来越复杂
然而,我们要说的是,与其弄清楚程序之间错综复杂的关系,
不如为大型程序准备这样一个"窗口",这就是我们要介绍的Facade设计模式...
Facade设计模式的本质在于为互相关联在一起的复杂的类提供一个高层接口
在下面的实例程序中,我们将会编写简单的Web页面...
 */

import java.io.*;
import java.util.Properties;

public class Facade {

    public static void main(String[] args)
            throws IOException {
        PageMaker.makeWelcomePage(
                "xmy19981214@sina.com",
                "welcome.html");
        /*
        那么我们之前说的Facade设计模式究竟体现在什么地方呢?
        我们可以看到PageMaker的API实际上就是我们提供的简单窗口,
        在Main中我们就用PageMaker执行一条简单的API调用,
        就创建了一个带有超链接的网页,这就使得请求者不会被错综复杂的程序结构所困扰...
        而且有一点值得注意的是,我们甚至能够做到让请求者只能使用PageMaker
        而Database和HtmlWriter都是受到保护的...
         */
    }
}

class Database {

    // 我们要防止在外面新建出数据库...
    private Database() {}

    public static Properties getProperties(String dbname) {
        String filename = dbname+".txt";
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}

class HtmlWriter {

    private static Writer writer;
    private HtmlWriter(Writer writer) {}

    public static void setWriter(Writer writer) {
        HtmlWriter.writer = writer;
    }

    public static void title(String title)
            throws IOException {
        writer.write("<html>");
        writer.write("<head>");
        writer.write("<title>"+title+"</title>");
        writer.write("</head>");
        writer.write("<body>\n");
        writer.write("<h1>"+title+"</h1>\n");
    }

    public static void paragraph(String meg)
            throws IOException {
        writer.write("<p>"+meg+"</p>");
    }

    public static void link(String href,String caption)
            throws IOException {
        paragraph("<a href=\""+href+"\">"+caption+"</a>");
    }

    public static void mailto(String mailaddr,String username)
            throws IOException {
        link("mailto:"+mailaddr,username);
    }

    public static void close()
            throws IOException {
        writer.write("</body>");
        writer.write("</html>\n");
        writer.close();
    }
}

class PageMaker {

    private PageMaker() {}

    public static void makeWelcomePage(String mailaddr,String filename)
            throws IOException {
        Properties mailProperties =
                Database.getProperties("/Users/mingyuexu/IdeaProjects/Java_learning/src/main/java/Java/DesignPattern/maildata");
        String username =
                mailProperties.getProperty(mailaddr);
        HtmlWriter.setWriter(new FileWriter(filename));
        HtmlWriter.title("Welcome to "+username+" 's page");
        HtmlWriter.paragraph(username+" welcome to your own page...");
        HtmlWriter.paragraph("Waiting for your mail...");
        HtmlWriter.mailto(mailaddr,username);
        HtmlWriter.close();
        System.out.println(filename+" is created for "+mailaddr+"("+username+")");
    }
}