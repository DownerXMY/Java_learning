package Java.DesignPattern.AOP_Clear;
/*
In this .java file, We will finish all the actions
we would like to do in the corresponding aspects...
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AspectActions {

    // The annotation for aspects
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Aspect {
        Class<?>[] value();
    }

    /*
    What are the annotations of those classes doing for?
     */
    @Aspect({MyService.MyService1.class, MyService.MyService2.class})
    public static class LogForAspect {

        public LogForAspect() {
            super();
        }

        public static void logBefore(
                Object object, Method method,Object[] args) {
            System.out.println("Entering:"+
                    method.getDeclaringClass().getSimpleName()+
                    "::"+method.getName()+",params:"+
                    Arrays.toString(args));
        }

        public static void logAfter(
                Object object,Method method,
                Object[] args,Object result) {
            System.out.println("Finishing:"+
                    method.getDeclaringClass().getSimpleName()+
                    "::"+method.getName()+",result:"+result);
        }
    }

    @Aspect({MyService.MyService2.class})
    public static class ExceptionForAspect {

        public ExceptionForAspect() {
            super();
        }

        public static void exceptionThrough(
                Object object,Method method,
                Object[] args,Throwable throwable) {
            System.err.println("Exception when calling: "
                    +method.getName()+
                    ","+Arrays.toString(args));
        }
    }
}
