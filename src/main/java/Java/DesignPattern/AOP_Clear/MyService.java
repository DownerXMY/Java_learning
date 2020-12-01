package Java.DesignPattern.AOP_Clear;
/*
In this .java file, we need to establish the service
that we want to add AOP on...
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class MyService {

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SimpleInject {}

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SimpleSingleton {}

    public static class MyService1 {

        public MyService1() {
            super();
        }

        @SimpleInject
        public MyService2 myService2;

        public MyService2 getMyService2() {
            return myService2;
        }

        public void setMyService2(MyService2 myService2) {
            this.myService2 = myService2;
        }

        public void callMyService2() {
            myService2.calling();
        }
    }

    public static class MyService2 {

        public MyService2() {
            super();
        }

        public static void calling() {
            System.out.println("This is a method in MyService2...");
        }
    }
}
