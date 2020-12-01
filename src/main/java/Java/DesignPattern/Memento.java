package Java.DesignPattern;

/*
我们知道很多软件都有撤销功能,就比如说IntelliJ-IDEA,
如果我们一不小心删除了正确的代码块,我们可以通过Command+Z来实现撤销
那么撤销的本质实际上是因为我们事先保存了对象的实例信息
而且与此同时,我们需要将保存的信息恢复到原先的实例中去,
那么这种保存并恢复的操作实际上是需要一种能够自由访问实例内部结构的权限
这就是我们即将要介绍的Memento设计模式要完成的事情...
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Memento {

    public static void main(String[] args) {
        Gamer gamer = new Gamer(100);
        MyMemento myMemento = gamer.createMemento();
        for (int item = 0; item < 100; item ++) {
            System.out.println("===="+item);
            System.out.println("Current state:"+gamer);

            gamer.bet();

            System.out.println("Holding money:"+gamer.getMoney());

            if (gamer.getMoney() > myMemento.getMoney()) {
                System.out.println("One holds much more money,hence keep the state...");
                myMemento = gamer.createMemento();
            } else if (gamer.getMoney() < myMemento.getMoney()) {
                System.out.println("One loses much more money,hence change to previous state...");
                gamer.restoreMemento(myMemento);
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*
        那么我们还是要来简单看看这件事情是怎么实现的...
        1.首先注意Memento是一个什么样的类呢?
            我们可以将其看做是一个表示"状态"的类,
            在这个类不仅计入了money,还将水果都放入了一个ArrayList中
        2.我们调用MyMemento myMemento = gamer.createMemento()的时候
            我们相当于创建了一个状态:money==100,ArrayList==null
        3.gamer.bet()中发生了很多事情,不仅往fruits中加入了实例
            还做了两步随机判断...
        4.最关键的步骤当然是在money判断的代码块
            注意createMemento()方法实际上还起到了更新的作用
            而restoreMemento()方法实际上起到了保存并恢复的作用
            这里myMemento实际上相当于我们的记忆...
        所以说,Memento设计模式的本质就在于:
            我们利用另外一个类来作为辅助记忆,
            如果不需要恢复记忆,那么我们就去更新它的变量,
            如果需要恢复,那么我们就不更新,相反我们传回它的变量信息
         */
    }
}

class MyMemento {

    int money;
    ArrayList<String> fruits;

    public int getMoney() {
        return money;
    }

    public MyMemento (int money) {
        this.money = money;
    }

    public void addFruit(String fruit) {
        fruits.add(fruit);
    }

    public List getFruits() {
        return (List) fruits.clone();
    }
}

class Gamer {

    private int money;
    private List<String> fruits = new ArrayList<>();
    private Random random = new Random();
    private static String[] fruitsName = {
            "Apple","Orange","Banana","Grape","Watermelon"
    };

    public Gamer(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public String getFruit() {
        String prefix = "";
        if (random.nextBoolean()) {
            prefix = "delicious ";
        }
        return prefix+fruitsName[random.nextInt(fruitsName.length)];
    }

    public void bet() {
        int dice = random.nextInt(6)+1;
        if (dice == 1) {
            money += 100;
            System.out.println("Money increased...");
        } else if (dice == 2) {
            money /= 2;
            System.out.println("Money halfed...");
        } else if (dice == 6) {
            String f = getFruit();
            System.out.println("Get the fruit ("+f+")");
            fruits.add(f);
        } else {
            System.out.println("Nothing happens...");
        }
    }

    // The following is the key step we need to conduct!
    public MyMemento createMemento() {
        MyMemento myMemento = new MyMemento(money);
        Iterator iterator = fruits.iterator();
        while (iterator.hasNext()) {
            String f = (String)iterator.next();
            if (f.startsWith("delicious")) {
                myMemento.addFruit(f);
            }
        }
        return myMemento;
    }

    public void restoreMemento(MyMemento myMemento) {
        this.money = myMemento.money;
        this.fruits = myMemento.getFruits();
    }

    @Override
    public String toString() {
        return "Gamer{" +
                "money=" + money +
                ", fruits=" + fruits +
                ", random=" + random +
                '}';
    }
}
