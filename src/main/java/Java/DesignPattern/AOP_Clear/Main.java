package Java.DesignPattern.AOP_Clear;
/*
Test the cglib-AOP...
 */

public class Main {

    public static void main(String[] args) {
        MyService.MyService1 myService1 =
                cglibContainer.getInstance(
                        MyService.MyService1.class);
        myService1.callMyService2();
    }
}
