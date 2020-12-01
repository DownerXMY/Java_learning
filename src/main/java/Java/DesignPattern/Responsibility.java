package Java.DesignPattern;
/*
我们介绍一种非常有趣的设计模式"Chain of Responsibility"
首先我们来看看什么叫做"推卸责任"(其实更加合适的说法叫做"踢皮球")?
比如我们在上海交通大学打印出国成绩单遇到了问题就是最好的例子
你去A出询问,他们跟你说你应该去B出问问,
等到了B,那边的人有跟你说这件事情其实归C处管,
...
 */

public class Responsibility {

    public static void main(String[] args) {
        Support alice =
                new NoSupport("Alice");
        Support bob =
                new LimitSupport("Bob",100);
        Support charlie =
                new SpecialSupport("Charlie",429);
        Support diana =
                new LimitSupport("Diana",200);
        Support fred =
                new OddSupport("Fred");
        // 形成职责链(皮球链):
        alice.setNextSupport(bob)
                .setNextSupport(charlie)
                .setNextSupport(diana)
                .setNextSupport(fred);
        for (int item=0;item<500;item+=33) {
            alice.support(new Trouble(item));
        }
        /*
        那么这个程序中还是有我们要说的东西的...
        首先我们要看看职责链的形成是怎么办到的?
        我们通过setNextSupport()的方法设置了当前Support的下一个Support是谁
        然后我们看看resolve()的逻辑,就是如果下面有人,我们就踢皮球给她,
        如果下面没有人了,这个Support才尝试自己去解决麻烦
        那么我们看到问题是不是能够被解决实际上取决于那些Support,
        否则默认就是fail...
         */
    }
}

class Trouble {

    private int number;
    public Trouble(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Trouble{" +
                "number=" + number +
                '}';
    }
}

abstract class Support {

    private String name;
    private Support nextSupport;

    public Support(String name) {
        this.name = name;
    }

    public Support setNextSupport(Support nextSupport) {
        this.nextSupport = nextSupport;
        return nextSupport;
    }

    public final void support(Trouble trouble) {
        if (resolve(trouble)) {
            done(trouble);
        } else if (nextSupport != null) {
            nextSupport.support(trouble);
        } else {
            fail(trouble);
        }
    }

    @Override
    public String toString() {
        return "Support{" +
                "name='" + name + '\'' +
                '}';
    }

    protected abstract boolean resolve(Trouble trouble);
    protected void done(Trouble trouble) {
        System.out.println(trouble+" has been resolved by "+name);
    }
    protected void fail(Trouble trouble) {
        System.out.println(trouble+" has failed to be resolved...");
    }
}

class NoSupport extends Support {
    // 表示不能解决...
    public NoSupport (String name) {
        super(name);
    }

    protected boolean resolve(Trouble trouble) {
        return false;
    }
}

class LimitSupport extends Support {
    // 表示只能有局限地解决...
    private int limit;
    public LimitSupport(String name,int limit) {
        super(name);
        this.limit = limit;
    }

    protected boolean resolve(Trouble trouble) {
        if (trouble.getNumber() < limit) {
            return true;
        } else {
            return false;
        }
    }
}

class OddSupport extends Support {
    // 表示只能有条件地解决,其实也是有局限地解决
    // 我们这里的LimitSupport,OddSupport,SpecialSupport
    // 都是指代不同能力的解决问题的部门...
    public OddSupport(String name) {
        super(name);
    }

    protected boolean resolve(Trouble trouble) {
        if (trouble.getNumber() % 2 == 1) {
            return true;
        } else {
            return false;
        }
    }
}

class SpecialSupport extends Support {

    private int number;
    public SpecialSupport(String name,int number) {
        super(name);
        this.number = number;
    }

    protected boolean resolve(Trouble trouble) {
        if (trouble.getNumber() == number) {
            return true;
        } else {
            return false;
        }
    }
}