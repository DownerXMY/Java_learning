package Java.JVM.ClassFile;

import java.util.Date;

public class DynamicDispatch {

    public static void main(String[] args) {
        Animal animal = new Animal();
        Animal dog = new Dog();
        animal.demo("Hello");
        animal.demo(new Date());
        dog.demo("World");
        dog.demo(new Date());
    }
}


class Animal {

    public void demo(String str) {
        System.out.println(
                "Animal "+str
        );
    }

    public void demo(Date date) {
        System.out.println("Animal "+date);
    }
}

class Dog extends Animal {
    @Override
    public void demo(String str) {
        System.out.println("Dog "+str);
    }

    @Override
    public void demo(Date date) {
        System.out.println("Dog "+date);
    }
}