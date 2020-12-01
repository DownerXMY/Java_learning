package Java.DesignPattern;

/*
现在考虑什么样的问题呢?
我们一直在做的事情是:类总是在调用自己的方法或者别的类的实例的方法,
虽然方法的调用效果最终确实是会显示出来(程序最终的运行结果)
但是我们没法看到方法是不是被调用了,
这让我们想起以前,我们自定义类加载器的时候,有一个findClass方法
我们一直以为是被调用了,实际上最后发现我们一直在用AppClassLoader(笑哭)
这就给我们DeBug带来很大的麻烦,那么能不能写一个类用来表示"请执行该方法"的命令呢?
这种设计模式就称为"Command设计模式"...
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Stack;

/*
这是一个简单的画画工具,很有趣...
 */

public class CommandApp extends JFrame
        implements ActionListener, MouseMotionListener, WindowListener {

    private MacroCommand history = new MacroCommand();
    private DrawCanvas canvas = new DrawCanvas(400,400,history);
    private JButton clearButton = new JButton("clear");

    public CommandApp(String title) {
        super(title);
        this.addWindowListener(this);
        canvas.addMouseMotionListener(this);
        clearButton.addActionListener(this);
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        buttonBox.add(clearButton);
        Box mainBox = new Box(BoxLayout.Y_AXIS);
        mainBox.add(buttonBox);
        mainBox.add(canvas);
        getContentPane().add(mainBox);
        pack();
        show();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearButton) {
            history.clear();
            canvas.repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        Command command =
                new DrawCommand(canvas,e.getPoint());
        history.append(command);
        command.execute();
    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }
    @Override
    public void windowClosed(WindowEvent e) {

    }
    @Override
    public void windowIconified(WindowEvent e) { }
    @Override
    public void windowDeiconified(WindowEvent e) {

    }
    @Override
    public void windowActivated(WindowEvent e) {

    }
    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    public static void main(String[] args) {
        new CommandApp("Command Pattern Sample");
    }
}

interface Command {
    void execute();
}

class MacroCommand implements Command {

    private Stack commands = new Stack();

    @Override
    public void execute() {
        Iterator iterator = commands.iterator();
        while (iterator.hasNext()) {
            ((Command) iterator.next()).execute();
        }
    }

    // 添加命令...
    public void append(Command command) {
        if (command != this) {
            commands.push(command);
        }
    }

    // 删除最后一条命令...
    public void undo() {
        if (!commands.empty()) {
            commands.pop();
        }
    }

    // 删除所有命令...
    public void clear() {
        commands.clear();
    }
}

interface Drawable {
    void draw(int x,int y);
}

class DrawCommand implements Command {

    protected Drawable drawable;
    private Point position;

    public DrawCommand(Drawable drawable,Point position) {
        this.drawable = drawable;
        this.position = position;
    }

    @Override
    public void execute() {
        drawable.draw(position.x, position.y);
    }
}

class DrawCanvas extends Canvas implements Drawable {

    private Color color = Color.red;
    private int radius = 6;
    private MacroCommand history;

    public DrawCanvas(int width,int height,MacroCommand macroCommand) {
        this.history = macroCommand;
        setSize(width,height);
        setBackground(Color.white);
    }

    public void paint (Graphics graphics) {
        history.execute();
    }

    @Override
    public void draw(int x, int y) {
        Graphics graphics = getGraphics();
        graphics.setColor(color);
        graphics.fillOval(x-radius,y-radius,radius*2,radius*2);
    }
}