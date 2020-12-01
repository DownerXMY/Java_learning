package Java.DesignPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Factory_Method {

    public static void main(String[] args) {
        /*
        Factory Method设计模式
        一听名字我们大概就能看出一些意思:
        上次介绍的TM设计模式能够帮助我们实现子类按照模板实现父类规定的流程
        然而这次我们希望子类来帮助我们创建实例,
        这样做的意义是深远的,再很多大型框架中是很常见的,总结起来说就是:
            "将生产实例的框架和实际负责生成实例的类解耦!"
         */
        IDCardFactory factory = new IDCardFactory();
        Product product1 = factory.create("MingyueXu",1);
        Product product2 = factory.create("QidongSu",2);
        Product product3 = factory.create("HaoyuShi",3);
        product1.use();
        product2.use();
        product3.use();
        Map<String,Integer> map = factory.getTable();
        System.out.println(map);
        /*
        这里本身没有什么特别值得再次强调的代码,但是我们可以考虑一下以下这个事实:
            我们可以将Product和Factory放入到别的Java package(package1)中去
            然后下次加入我们要创建别的实例(比如我们打算生产计算机,生产校园卡...)
            我们只要import package1.*;即可
         当然实际上在子类中创建对象,我们在静态代理中也同样实现过了...
         */
    }
}

abstract class Product {

    public abstract void use();
}

abstract class Factory {

    public final Product create(String owner,int idNumber) {
        Product product = createProduct(owner,idNumber);
        registerProduct(product);
        return product;
    }

    protected abstract Product createProduct(String owner,int idNumber);
    protected abstract void registerProduct(Product product);
}

class IDCard extends Product {

    private String owner;
    private int idNumber;

    // 注意这里我们的构造方法不写成public,对实际开发是具有很大意义的
    IDCard(String owner, int idNumber) {
        System.out.println("We are making " + owner + "'s IDCard...");
        this.owner = owner;
        this.idNumber = idNumber;
    }

    @Override
    public void use() {
        System.out.println("Using " + owner + "'s IDCard...");
    }

    public String getOwner() {
        return owner;
    }

    public int getIdNumber() {
        return idNumber;
    }
}

class IDCardFactory extends Factory {

    private List<String> owners = new ArrayList<>();
    private List<IDCard> idCards =
            new ArrayList<>();

    @Override
    protected Product createProduct(String owner,int idNumber) {
        IDCard idCard = new IDCard(owner,idNumber);
        idCards.add(idCard);
        return idCard;
    }

    @Override
    protected void registerProduct(Product product) {
        owners.add(((IDCard)product).getOwner());
    }

    public List getOwners() {
        return owners;
    }

    public Map<String,Integer> getTable() {
        Map<String,Integer> map = new HashMap<>();
        for (IDCard idCard : idCards) {
            map.put(idCard.getOwner(),idCard.getIdNumber());
        }
        return map;
    }
}