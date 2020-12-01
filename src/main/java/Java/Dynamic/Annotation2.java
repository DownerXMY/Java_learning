package Java.Dynamic;

// 这是一个注解和反射结合的例子...

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class Annotation2 {

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    static @interface getParam {
        String value();
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    static @interface DefaultValue {
        String value() default "default-value";
    }

    public void Hello(
            @getParam("param1")
                    String param1,

            @getParam("param2")
            @DefaultValue()
                    String param2
    ) {}

    public static void main(String[] args) throws Exception {
        Class<?> cls = Annotation2.class;
        Method method =
                cls.getMethod("Hello", String.class, String.class);
        Annotation[][] annotations =
                method.getParameterAnnotations();
        for (int item=0;item<annotations.length;item++) {
            System.out.println(
                    "Annotation for parameter "+(item+1)
            );
            Annotation[] annotations1 = annotations[item];
            for (Annotation annotation : annotations1) {
                if (annotation instanceof getParam) {
                    getParam gP = (getParam) annotation;
                    System.out.println(
                            gP.annotationType()
                            .getSimpleName()+":"+gP.value()
                    );
                } else if (annotation instanceof DefaultValue) {
                    DefaultValue DF = (DefaultValue) annotation;
                    System.out.println(
                            DF.annotationType()
                            .getSimpleName()+":"+DF.value()
                    );
                }
            }
        }
    }
}
