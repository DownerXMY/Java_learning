package Java.Dynamic;

// 我们来看Java注解的最后一个示例应用:DI容器

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationApp2 {

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SimpleInject {}

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SimpleSingleton {}

    public class ServiceA {

        @SimpleInject
        ServiceB serviceB;
        // 注解"@SimpleInject"用来表示ServiceA对于ServiceB的依赖

        public void callServiceB() {
            serviceB.action();
        }

        public ServiceB getServiceB() {
            return serviceB;
        }
    }

    @SimpleSingleton
    public class ServiceB {

        public void action() {
            System.out.println("Now is in ServiceB...");
        }
    }

    public static void main(String[] args) {
        ServiceA serviceA =
                DIContainer.getInstance(ServiceA.class);
        serviceA.callServiceB();
    }
}

class DIContainer {

    public static Map<Class<?>,Object> instances =
            new ConcurrentHashMap<>();

    public static <T> T getInstance1(Class<T> tClass) {
        try {
            T obj = tClass.newInstance();
            Field[] fields = tClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(
                        AnnotationApp2.SimpleInject.class)) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    Class<?> fieldcls = field.getType();
                    field.set(obj,getInstance1(fieldcls));
                }
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 我们放弃之前写的getInstance1,重写一个新的静态方法,
    // 后面我们可以来想想为什么要这么做...
    public static <T> T getInstance(Class<T> tClass) {
        try {
            boolean singleton =
                    tClass.isAnnotationPresent(AnnotationApp2.SimpleSingleton.class);
            if (!singleton) {
                return createInstance(tClass);
            }
            Object obj = instances.get(tClass);
            if (obj != null) {
                return (T) obj;
            }
            synchronized (tClass) {
                obj = instances.get(tClass);
                if (obj == null) {
                    obj = createInstance(tClass);
                    instances.put(tClass,obj);
                }
            }
            return (T) obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createInstance(Class<T> cls)
            throws Exception {
        T obj = cls.newInstance();
        Field[] fields = cls.getDeclaredFields();
        for (Field field: fields) {
            if (field.isAnnotationPresent(
                    AnnotationApp2.SimpleInject.class)) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Class<?> fieldcls = field.getType();
                field.set(obj,getInstance(fieldcls));
            }
        }
        return obj;
    }
}