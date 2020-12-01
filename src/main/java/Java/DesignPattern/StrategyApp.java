package Java.DesignPattern;
/*
使用Strategy模式的目的是:
    为了让我们能够整体地替换算法的实现部分,
    使得一种很综合的算法能够根据实际需要简便地做出变化以解决问题...
 */

import java.util.Random;

public class StrategyApp {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage:Java.DesignPattern.StrategyApp randomSeed1 randomSeed2");
            System.out.println("try again...");
        }
        int seed1 = Integer.parseInt(args[0]);
        int seed2 = Integer.parseInt(args[1]);
        Player player1 =
                new Player("MingyueXu",new WinningStrategy(seed1));
        Player player2 =
                new Player("QidongSu",new ProbStrategy(seed2));
        for (int item = 1;item <= 100;item++) {
            Hand nextHand1 = player1.nextHand();
            Hand nextHand2 = player2.nextHand();
            if (nextHand1.isStrongerThan(nextHand2)) {
                System.out.println("Winner:"+player1);
                player1.win();
                player2.lose();
            } else if (nextHand1.isWeakerThan(nextHand2)) {
                System.out.println("Winner:"+player2);
                player1.lose();
                player2.win();
            } else {
                System.out.println("Tied...");
                player1.even();
                player2.even();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Final result:");
        System.out.println(player1.toString());
        System.out.println(player2.toString());
        /*
        当然程序本身实现的是否很漂亮我们并不关心,
        主要要去理解这里的两个不同的策略的实现:
        其实我们也没有用到什么很特殊的内置接口,也没有特别值得说明的继承技巧
        主要呢还是这种设计思想...
         */
    }
}

class Hand {

    private int handValue;

    public static final int GUU = 0;
    public static final int CHO = 1;
    public static final int PAA = 2;

    public Hand(int HANDVALUE) {
        this.handValue = HANDVALUE;
    }

    public static final Hand[] hands = {
            new Hand(GUU),
            new Hand(CHO),
            new Hand(PAA)
    };

    private static final String[] names = {
            "石头","剪刀","布"
    };

    public static Hand getHand(int handValue) {
        return hands[handValue];
    }

    public boolean isStrongerThan(Hand hand) {
        return fight(hand) == 1;
    }

    public boolean isWeakerThan(Hand hand) {
        return fight(hand) == -1;
    }

    private int fight(Hand hand) {
        if(this.handValue == hand.handValue) {
            // 这里有一个很重要的问题,那就是"Design Pattern"的作者说
            // 这里也可以直接写成 this==hand 为什么可以这样?
            // 看你是不是真正理解了final关键字的含义...
            return 0;
        } else if((this.handValue+1)%3 == hand.handValue) {
            return 1;
        } else {
            return -1;
        }
    }

    public String toString () {
        return names[handValue];
    }
}

interface Strategy {
    Hand nextHand();
    void study(boolean win);
}

class WinningStrategy implements Strategy {

    private Random random;
    private boolean won = false;
    private Hand prevHand;

    public WinningStrategy(int seed) {
        random = new Random(seed);
    }


    @Override
    public Hand nextHand () {
        if (!won) {
            prevHand = Hand.getHand(random.nextInt(3));
        }
        return prevHand;
    }

    @Override
    public void study ( boolean win){
        won = win;
    }
}

class ProbStrategy implements Strategy {

    private Random random;
    private int prevHandValue;
    private int currentHandValue;
    private int[][] history = {
            {1,1,1},{1,1,1},{1,1,1}
    };

    public ProbStrategy(int seed) {
        random = new Random(seed);
    }

    @Override
    public Hand nextHand() {
        int bet = random.nextInt(getSum(currentHandValue));
        int handValue = 0;
        if (bet < history[currentHandValue][0]+history[currentHandValue][1]) {
            handValue = 1;
        } else {
            handValue = 2;
        }
        prevHandValue = currentHandValue;
        currentHandValue = handValue;
        return Hand.getHand(handValue);
    }

    private int getSum(int hv) {
        int sum = 0;
        for (int i=0;i<=2;i++) {
            sum += history[hv][i];
        }
        return sum;
    }

    @Override
    public void study(boolean win) {
        if (win) {
            history[prevHandValue][(currentHandValue+1)%3]++;
            history[prevHandValue][(currentHandValue+2)%3]++;
        }
    }
}

class Player {

    private String name;
    private Strategy strategy;
    private int wincount;
    private int losecount;
    private int gamecount;

    public Player(String name, Strategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    public Hand nextHand() {
        return strategy.nextHand();
    }

    public void win() {
        strategy.study(true);
        wincount++;
        gamecount++;
    }

    public void lose() {
        strategy.study(false);
        losecount++;
        gamecount++;
    }

    public void even() {
        gamecount++;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", strategy=" + strategy +
                ", wincount=" + wincount +
                ", losecount=" + losecount +
                ", gamecount=" + gamecount +
                '}';
    }
}