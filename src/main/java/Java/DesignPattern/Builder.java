package Java.DesignPattern;

/*
我们漫漫地要将事情变复杂了,
设计模式当然远远不会像之前介绍的几种最基本的模式那么简单
通俗地说,我们看到的城市中的高楼大厦都是逐步搭建起来的,现有地基,然后一层层往上走
我们把这样的"组装复杂结构的实例的设计模式"称为"Builder模式"
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.IntStream;

public class Builder {

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
            System.exit(0);
        }
        if (args[0].equals("plain")) {
            TextBuilder textBuilder = new TextBuilder();
            MyDirector myDirector =
                    new MyDirector(textBuilder);
            myDirector.construct("Hello");
            String result = textBuilder.getResult();
            System.out.println(result);
        } else if (args[0].equals("html")) {
            HTMLBuilder htmlBuilder = new HTMLBuilder();
            MyDirector myDirector =
                    new MyDirector(htmlBuilder);
            myDirector.construct("SelfTitle");
            String filename = htmlBuilder.getResult();
            System.out.println(filename);
        } else {
            usage();
            System.exit(0);
        }

        /*
        针对这个程序,我们要在终端输入
        java Java.DesignPattern.Builder plain
        如果是另外一个,我们是这样操作的:
        java Java.DesignPattern.Builder html
        然后我们会发现同级目录下面生成了一个Greeting.html文件
        当然你可以在终端用cat Greeting.html查看文件在终端的输出
        但另外一种我们可以直接利用默认的浏览器打开查看:
        open Greeting.html
        */
        /*
        当然上面说的是我们对于程序的具体操作实现,
        但是更重要的是我们要说一下"Builder设计模式"!
        形象地说,
            这里的"地基"就是MyBuilder(建造者的总体)
            然后我们在地基上搭了第一层楼TextBuilder/HTMLBuilder(具体的建造者)
            再然后我们又搭建了第二层楼MyDirector(监工)
         */
    }

    public static void usage() {
        System.out.println("Usage: java Builder plain...");
        System.out.println("Usage: java Builder html...");
    }
}

interface MyBuilder {

    void makeTitle(String title);

    void makeString(String string);

    void makeItems(String[] items);

    void close();

}

class MyDirector {

    private MyBuilder myBuilder;

    public MyDirector(MyBuilder myBuilder) {
        this.myBuilder = myBuilder;
    }

    public void construct(String title) {
        myBuilder.makeTitle(title);
        myBuilder.makeString("from morning to the evening...");
        myBuilder.makeItems(
                new String[] {"good morning","good evening"});
        myBuilder.makeString("night");
        myBuilder.makeItems(new String[] {
                        "good night",
                        "goodbye",
                        "see you tomorrow"});
        myBuilder.close();
    }
}

class TextBuilder implements MyBuilder {

    private StringBuffer stringBuffer = new StringBuffer();

    @Override
    public void makeTitle(String title) {
        stringBuffer.append("===================\n");
        stringBuffer.append("["+title+"]\n");
        stringBuffer.append("\n");
    }

    @Override
    public void makeString(String string) {
        stringBuffer.append(string+"\n");
        stringBuffer.append("\n");
    }

    @Override
    public void makeItems(String[] items) {
        IntStream.rangeClosed(1,items.length).forEach(i -> {
            stringBuffer.append(" *"+items[i-1]+"\n");
        });
        stringBuffer.append("\n");
    }

    @Override
    public void close() {
        stringBuffer.append("===================\n");
    }

    public String getResult() {
        return stringBuffer.toString();
    }
}

class HTMLBuilder implements MyBuilder {

    private String filename;
    private PrintWriter writer;

    @Override
    public void makeTitle(String title) {
        filename = title + ".html";
        try {
            writer = new PrintWriter(new FileWriter(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(
                "<html><head><title>"
                +title+
                "</title></head><body>");
        writer.println("<h1>"+title+"</h1>");
    }

    @Override
    public void makeString(String string) {
        writer.println("<p>"+string+"</p>");
    }

    @Override
    public void makeItems(String[] items) {
        writer.println("<ul>");
        IntStream.rangeClosed(1, items.length).forEach(i -> {
            writer.println("<li>"+items[i-1]+"</li>");
        });
        writer.println("</ul>");
    }

    @Override
    public void close() {
        writer.println("</body></html>");
        writer.close();
    }

    public String getResult() {
        return filename;
    }
}