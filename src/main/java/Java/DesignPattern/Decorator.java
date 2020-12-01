package Java.DesignPattern;
/*
Decorator设计模式,顾名思义,我们将要介绍的是一种装饰物,
装饰物并没有直接影响或改变事情的本质,
就好像草莓蛋糕,奶油蛋糕,...这些本质上都是蛋糕
 */

import java.io.*;
import java.net.Socket;
import java.util.stream.IntStream;

public class Decorator {

    public static void main(String[] args)
            throws IOException {
        MyDisplay myDisplay1 =
                new StringMyDisplay("Hello world");
        MyDisplay myDisplay2 =
                new SideBorder(myDisplay1,'#');
        MyDisplay myDisplay3 =
                new FullBorder(myDisplay2);
        myDisplay1.show();
        myDisplay2.show();
        myDisplay3.show();
        MyDisplay myDisplay4 = new SideBorder(
                new FullBorder(
                        new FullBorder(
                                new SideBorder(
                                        new FullBorder(
                                                new StringMyDisplay("Hello world")
                                        ),'#'
                                )
                        )
                ),'/'
        );
        myDisplay4.show();
        /*
        我们看到上面最后一个例子最好地说明了Decrator设计模式的基本思想
        实际上我们应该这么看这件事情:
            首先我们说MyDisplay是本质,我们看到后面不论是
            SideBorder,FullBorder都是继承了Border,进而继承了MyDisplay
            为什么装饰物能够被"装饰"上去呢?
            这是因为每一个子类的构造器中,MyDisplay都是作为一个入参被传入
            我们这里要注意,StringMyDisplay作为抽象类Display的实现
                作用是我们之前说过的"实现层次结构"
            而我们可以将Border看做装饰器的大类
                后面的SideBorder和FullBorder都可以看做是装饰器
        那么Decorator设计模式在我们日常开发中最常见的作用当然是:
        java.io
        比如,我们可以看看下面这个文件读写的例子:
         */
        System.out.println("-----------------");
        Socket socket = new Socket("localhost",9060);
        Reader reader = new LineNumberReader(
                new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                )
        );
        reader.close();
        // 或者说下面这种,在java.io中Decorator设计模式是非常常见的...
        Reader reader1 = new BufferedReader(
                new FileReader("/usr/local/Cellar/zeppelin/zeppelin-0.9.0-preview2-bin-all/explanation.txt")
        );
        reader1.close();
    }
}

abstract class MyDisplay {

    public abstract int getColumns();
    public abstract int getRows();
    public abstract String getRowText(int row);

    public final void show() {
        IntStream.rangeClosed(1,getRows()).forEach(i -> {
            System.out.println(getRowText(i));
        });
    }
}

class StringMyDisplay extends MyDisplay {

    private String string;

    public StringMyDisplay(String string) {
        this.string = string;
    }

    @Override
    public int getColumns() {
        return string.getBytes().length;
    }

    @Override
    public int getRows() {
        return 1;
    }

    @Override
    public String getRowText(int row) {
        if(row == 0) {
            return string;
        } else {
            return null;
        }
    }
}

abstract class Border extends MyDisplay {

    protected MyDisplay myDisplay;

    protected Border(MyDisplay myDisplay) {
        this.myDisplay = myDisplay;
    }
}

class SideBorder extends Border {

    private char borderChar;

    public SideBorder(MyDisplay myDisplay,char c) {
        super(myDisplay);
        this.borderChar = c;
    }

    @Override
    public int getColumns() {
        return myDisplay.getColumns()+2;
    }

    @Override
    public int getRows() {
        return myDisplay.getRows();
    }

    @Override
    public String getRowText(int row) {
        return borderChar+myDisplay.getRowText(row)+borderChar;
    }
}

class FullBorder extends Border {

    public FullBorder(MyDisplay myDisplay) {
        super(myDisplay);
    }

    @Override
    public int getColumns() {
        return myDisplay.getColumns()+2;
    }

    @Override
    public int getRows() {
        return myDisplay.getRows()+2;
    }

    @Override
    public String getRowText(int row) {
        if (row == 0) {
            return "+"+makeLine('-',myDisplay.getColumns())+"+";
        } else if (row == myDisplay.getRows()+1) {
            return "+"+makeLine('-',myDisplay.getColumns())+"+";
        } else {
            return "|"+myDisplay.getRowText(row-1)+"|";
        }
    }

    private String makeLine(char ch,int count) {
        StringBuffer stringBuffer = new StringBuffer();
        IntStream.rangeClosed(1,count).forEach(i -> {
            stringBuffer.append(ch);
        });
        return stringBuffer.toString();
    }
}
