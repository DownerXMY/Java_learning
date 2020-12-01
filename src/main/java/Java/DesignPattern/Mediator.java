package Java.DesignPattern;
/*
如果Java开发小组是一个乱糟糟的团队,大家的意见都是不统一的
那么事情就永远都做不好,所以说我们一定是需要一个"仲裁者"
这样以后,工作模式就变成了组员向仲裁者报告,仲裁者给组员派发任务
这种管理模式就称为"Mediator设计模式"...
 */

import java.awt.*;
import java.awt.event.*;

public class Mediator {

    public static void main(String[] args) {
        new LoginFrame("Mediator Sample");
    }
}

interface MyMediator {
    void createColleagues();
    void colleagueChanges();
}

interface Colleague {
    void setMediator(MyMediator mediator);
    void setColleagueEnabled(boolean enabled);
}

class ColleagueButton extends Button implements Colleague {

    private MyMediator myMediator;
    public ColleagueButton(String caption) {
        super(caption);
    }

    public void setMediator(MyMediator mediator) {
        this.myMediator = mediator;
    }

    public void setColleagueEnabled(boolean enabled) {
        setEnabled(enabled);
    }
}

class ColleagueTextField extends TextField
        implements TextListener, Colleague {

    private MyMediator myMediator;
    public ColleagueTextField(String text,int columns) {
        super(text,columns);
    }

    @Override
    public void setMediator(MyMediator mediator) {
        this.myMediator = mediator;
    }

    @Override
    public void setColleagueEnabled(boolean enabled) {
        setEnabled(enabled);
        setBackground(enabled ? Color.white:Color.lightGray);
    }

    @Override
    public void textValueChanged(TextEvent e) {
        myMediator.colleagueChanges();
    }
}

class ColleagueCheckBox extends Checkbox
        implements ItemListener, Colleague {

    private MyMediator myMediator;
    public ColleagueCheckBox(String caption,CheckboxGroup group,boolean state) {
        super(caption,group,state);
    }

    @Override
    public void setMediator(MyMediator mediator) {
        this.myMediator = mediator;
    }

    @Override
    public void setColleagueEnabled(boolean enabled) {
        setEnabled(enabled);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        myMediator.colleagueChanges();
    }
}

class LoginFrame extends Frame
        implements ActionListener, MyMediator {

    private ColleagueCheckBox colleagueCheckBox1;
    private ColleagueCheckBox colleagueCheckBox2;
    private ColleagueTextField colleagueTextField1;
    private ColleagueTextField colleagueTextField2;
    private ColleagueButton colleagueButton1;
    private ColleagueButton colleagueButton2;

    public LoginFrame(String title) {
        super(title);
        setBackground(Color.lightGray);
        setLayout(new GridLayout(4,2));
        createColleagues();
        add(colleagueCheckBox1);
        add(colleagueCheckBox2);
        add(new Label("Username:"));
        add(colleagueTextField1);
        add(new Label("Password:"));
        add(colleagueTextField2);
        add(colleagueButton1);
        add(colleagueButton2);
        pack();
        show();
    }

    @Override
    public void createColleagues() {
        CheckboxGroup group = new CheckboxGroup();
        colleagueCheckBox1 =
                new ColleagueCheckBox("Guest",group,true);
        colleagueCheckBox2 =
                new ColleagueCheckBox("Login",group,false);
        colleagueTextField1 =
                new ColleagueTextField("",10);
        colleagueTextField2 =
                new ColleagueTextField("",10);
        colleagueButton1 =
                new ColleagueButton("OK");
        colleagueButton2 =
                new ColleagueButton("Cancel");
        colleagueCheckBox1.setMediator(this);
        colleagueCheckBox2.setMediator(this);
        colleagueTextField1.setMediator(this);
        colleagueTextField2.setMediator(this);
        colleagueButton1.setMediator(this);
        colleagueButton2.setMediator(this);
        colleagueCheckBox1.addItemListener(colleagueCheckBox1);
        colleagueCheckBox2.addItemListener(colleagueCheckBox2);
        colleagueTextField1.addTextListener(colleagueTextField1);
        colleagueTextField2.addTextListener(colleagueTextField2);
        colleagueButton1.addActionListener(this);
        colleagueButton2.addActionListener(this);
    }

    @Override
    public void colleagueChanges() {
        if (colleagueCheckBox1.getState()) {
            colleagueTextField1.setColleagueEnabled(false);
            colleagueTextField2.setColleagueEnabled(false);
            colleagueButton1.setColleagueEnabled(true);
        } else {
            colleagueTextField1.setColleagueEnabled(true);
            userPassChanged();
        }
    }

    private void userPassChanged() {
        if (colleagueTextField1.getText().length() > 0) {
            colleagueTextField2.setColleagueEnabled(true);
            if (colleagueTextField2.getText().length() > 0) {
                colleagueButton1.setColleagueEnabled(true);
            } else {
                colleagueButton1.setColleagueEnabled(false);
            }
        } else {
            colleagueTextField2.setColleagueEnabled(false);
            colleagueButton1.setColleagueEnabled(false);
        }
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.toString());
        System.exit(0);
    }
}