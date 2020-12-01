package Java.DesignPattern;

/*
什么叫做State设计模式?
那么在之前的Memento设计模式中我们已经看到了,
可以用一个MyMemento的类来表示一个当前的状态,
其中类中的成员变量就表示状态的具体信息...
当然这是一个比较特殊的实例,像这种用类来表示状态的模式就称为"State设计模式"
----------------
State设计模式的好处在什么地方,我们后面会说明...
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.IntStream;

public class StateApp {

    public static void main(String[] args) {
        SafeFrame frame =
                new SafeFrame("State Sample");
        for (;;) {
            IntStream.rangeClosed(0,24).forEach(hour -> {
                frame.setClock(hour);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        /*
        我们要做的工作分为两步:
        首先我们要"看懂"这个程序:
          如果你知道了怎么用这个交互窗口,我们也就真正理解了这个程序
        然后,我们要分析其中用到的设计模式:
          这里面有不止一种设计模式...
         */
    }
}

/*
1.我们看到DayState和NightState在类中分别直接新建了自己的实例:
    这显然是 Singleton 设计模式!
2.然后我们之前提到的State设计模式的好处究竟是什么呢?
    一方面我们不需要再去使用if判断语句了
    另外一方面使得代码的逻辑更加清晰,方面与我们的维护...
3.使得注意的是State设计模式往往会和Singleton设计模式相结合,
    这实际上也很好理解,然后还有一点值得说明的是:
    多个States之间的转化实际上是通过"成员变量以及改变成员变量的方法"来实现的
4.为了更加好地理解上面说的事情,我们还是讲的通俗一点:
    DayState和NightState都是实现了State接口,
    假如我们把State的一个实例作为成员变量写入某个类中,
    那么我们就有类似这样的办法实现状态的转变:
        state = new DayState()
        state = new NightState()
    另外"状态的转变"其实是指:
    DayState和NightState中的同样名字的方法具有不同的实现...
*/

interface State {
    void doClock(MyContext context,int hour);
    void doUse(MyContext context);
    void doAlarm(MyContext context);
    void doPhone(MyContext context);
}

class DayState implements State {

    private static DayState singleton = new DayState();
    private DayState() {}

    public static State getInstance() {
        return singleton;
    }

    @Override
    public void doClock(MyContext context, int hour) {
        if (hour < 9 || hour >= 17) {
            context.changeState(NightState.getInstance());
        }
    }

    @Override
    public void doUse(MyContext context) {
        context.recordLog("Use the exchequer during daytime");
    }

    @Override
    public void doAlarm(MyContext context) {
        context.callSecurityCenter("Call SecurityCenter during daytime");
    }

    @Override
    public void doPhone(MyContext context) {
        context.callSecurityCenter("Make a phone normally during daytime");
    }

    @Override
    public String toString() {
        return "[Day]";
    }
}

class NightState implements State {

    private static NightState singleton = new NightState();
    private NightState() {}

    public static NightState getInstance() {
        return singleton;
    }

    @Override
    public void doClock(MyContext context, int hour) {
        if (hour >= 9 && hour < 17) {
            context.changeState(DayState.getInstance());
        }
    }

    @Override
    public void doUse(MyContext context) {
        context.recordLog("Use the exchequer during nighttime");
    }

    @Override
    public void doAlarm(MyContext context) {
        context.callSecurityCenter("Call SecurityCenter during nighttime");
    }

    @Override
    public void doPhone(MyContext context) {
        context.callSecurityCenter("Make a phone during nighttime");
    }

    @Override
    public String toString() {
        return "[Night]";
    }
}

interface MyContext {
    void setClock(int hour);
    void changeState(State state);
    void callSecurityCenter(String meg);
    void recordLog(String msg);
}

class SafeFrame extends Frame
        implements ActionListener, MyContext {

    // 构建窗口的具体组成部分...
    private TextField textClock =
            new TextField(60);
    private TextArea textScreen =
            new TextArea(10,60);
    private Button buttonUse =
            new Button("Use Exchequer");
    private Button buttonAlarm =
            new Button("Call SecurityCenter");
    private Button buttonPhone =
            new Button("Make Phone");
    private Button buttonExit =
            new Button("Exit");

    // 注意,SafeFrame在初始化的时候,默认的State就是DayState
    // 所以后面我们就不用去纠结到底setClock()方法中执行的doClock()是哪一个了
    private State state = DayState.getInstance();

    // 设置窗口的具体样子...
    public SafeFrame (String title) {
        super(title);
        setBackground(Color.lightGray);
        setLayout(new BorderLayout());

        add(textClock,BorderLayout.NORTH);
        textClock.setEditable(false);
        add(textScreen,BorderLayout.CENTER);
        textScreen.setEditable(false);
        Panel panel = new Panel();
        panel.add(buttonUse);
        panel.add(buttonAlarm);
        panel.add(buttonPhone);
        panel.add(buttonExit);
        add(panel,BorderLayout.SOUTH);
        pack();
        show();

        buttonUse.addActionListener(this);
        buttonAlarm.addActionListener(this);
        buttonPhone.addActionListener(this);
        buttonExit.addActionListener(this);
    }

    @Override
    public void setClock(int hour) {
        String clockString = "Now the time is ";
        if (hour < 10) {
            clockString += "0"+hour+":00";
        } else {
            clockString += hour+":00";
        }
        System.out.println(clockString);
        textClock.setText(clockString);
        state.doClock(this,hour);
    }

    @Override
    public void changeState(State state) {
        System.out.println("From"+this.state+"to"+state);
        this.state = state;
    }

    @Override
    public void callSecurityCenter(String meg) {
        textScreen.append("call"+meg+"\n");
    }

    @Override
    public void recordLog(String msg) {
        textScreen.append("record"+msg+"\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.toString());
        if (e.getSource() == buttonUse) {
            state.doUse(this);
        } else if (e.getSource() == buttonAlarm) {
            state.doAlarm(this);
        } else if (e.getSource() == buttonPhone) {
            state.doPhone(this);
        } else if (e.getSource() == buttonExit) {
            System.exit(0);
        } else {
            System.out.println("?");
        }
    }
}
