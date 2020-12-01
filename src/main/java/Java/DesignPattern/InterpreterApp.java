package Java.DesignPattern;

/*
导语:语法规则也是类!
我们说设计模式的目的就是提高类的可复用性,什么叫做"可复用性":
就是指不需要对类做太多的修改,就能够用到不同的业务需求中去
那么我们说在Interpreter设计模式中,
程序要解决的问题会被用非常简单的"迷你语言"表述出来,
然后我们还需要一个称为"解释器"的东西,将这种"迷你语言"翻译成可执行的语言...
那么什么叫做"迷你",怎么样设计"解释器",我们将在具体的程序实例中看到
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class InterpreterApp {

    public static void main(String[] args) {
        /*
        什么叫做"迷你语言":
            比如我们想让某一辆玩具车向前走然后右转两次再向前走最后停止
            迷你语言的表达就是这样的:
            program go right right go end
        当然机器是看不懂这样的指令的,除非我们加上语法解析器,
        这就是所谓的"解释器",整个过程有点类似"语法描述"
         */
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(
                            new FileReader(args[0]));
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                System.out.println("text = \""+text+"\"");
                Node node = new ProgramNode();
                node.parse(new Context1(text));
                /*
                --------------------------
                首先新建了一个Context1的实例
                    那么在构造器中,调用了nextToken()
                    将currentToken变成了"program"
                然后就是ProgramNode.parse(Context1)
                    再次调用nextToken(),
                    将currentToken变成了"end"
                然后就是CommandListNode.parse(Context1)
                    这时候因为currentToken是"end",所以程序break了
                --------------------------
                当然上面说的是针对program.txt的第一行 program end
                的运行过程,别的"行"也同理可以分析...
                你可以看到解析parse实际上是一层套着一层的...
                 */
                System.out.println("node = "+node);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyParseException e) {
            e.printStackTrace();
        }
        /*
        我们看到这个程序其实并不简单,所以是值得分析的:
        我们看到在main()方法中实际上没有多少语句,
        但是有的语句实际上都做了很多事情,比如这句:
        node.parse(new Context1(text));
         */
    }
}

abstract class Node {
    public abstract void parse(Context1 context) throws MyParseException;
}

class ProgramNode extends Node {

    private Node commandListNode;

    @Override
    public void parse(Context1 context) throws MyParseException {
        context.skipToken("program");
        commandListNode = new CommandListNode();
        commandListNode.parse(context);
    }

    @Override
    public String toString() {
        return "[Program " +
                commandListNode +
                ']';
    }
}

class CommandListNode extends Node {

    private ArrayList<Node> list = new ArrayList<>();

    @Override
    public void parse(Context1 context) throws MyParseException {
        while (true) {
            if (context.currentToken() == null) {
                throw new MyParseException("Missing 'end'");
            } else if (context.currentToken().equals("end")) {
                context.skipToken("end");
                break;
            } else {
                Node commandNode = new CommandNode();
                commandNode.parse(context);
                list.add(commandNode);
            }
        }
    }

    @Override
    public String toString() {
        return list.toString();
    }
}

class CommandNode extends Node {

    private Node node;

    @Override
    public void parse(Context1 context) throws MyParseException {
        if (context.currentToken().equals("repeat")) {
            node = new RepeatCommandNode();
            node.parse(context);
        } else {
            node = new PrimitiveCommandNode();
            node.parse(context);
        }
    }

    @Override
    public String toString() {
        return node.toString();
    }
}

class RepeatCommandNode extends Node {

    private int number;
    private Node commandListNode;

    @Override
    public void parse(Context1 context) throws MyParseException {
        context.skipToken("repeat");
        number = context.currentNumber();
        context.nextToken();
        commandListNode = new CommandListNode();
        commandListNode.parse(context);
    }

    @Override
    public String toString() {
        return "[Repeat " + number +
                " " + commandListNode +
                ']';
    }
}

class PrimitiveCommandNode extends Node {

    private String name;

    @Override
    public void parse(Context1 context) throws MyParseException {
        name = context.currentToken();
        context.skipToken(name);
        if (!name.equals("go")
                && !name.equals("right")
                && !name.equals("left")) {
            throw new MyParseException(name+" is undefined...");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}

class Context1 {

    private StringTokenizer tokenizer;
    private String currentToken;

    public Context1(String text) {
        this.tokenizer = new StringTokenizer(text);
        nextToken();
    }

    public String nextToken() {
        if (tokenizer.hasMoreTokens()) {
            currentToken = tokenizer.nextToken();
        } else {
            currentToken = null;
        }
        return currentToken;
    }

    public String currentToken() {
        return currentToken;
    }

    public void skipToken(String token)
            throws MyParseException {
        if (!token.equals(currentToken)) {
            throw new MyParseException("Warning: "+token+" is expected, but "+currentToken+" is found");
        }
        nextToken();
    }

    public int currentNumber ()
            throws MyParseException {
        int number = 0;
        try {
            number = Integer.parseInt(currentToken);
        } catch (NumberFormatException e) {
            throw new MyParseException("Warning: "+e);
        }
        return number;
    }
}

class MyParseException extends Exception {
    public MyParseException(String msg) {
        super(msg);
    }
}