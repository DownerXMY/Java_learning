package Java.Dynamic;
/*
动态代理的应用:
AOP(Aspect Oriented Programing)
这是现代开发中极其热门且极其重要的一块方向
现在我们就来简单体验一下利用cglib动态代理实现一个极简的AOP框架...
 */

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class AOP {

    // 切面类的注解
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Aspect {
        Class<?>[] value();
    }

    public class ServiceA {
        @AnnotationApp2.SimpleInject
        private ServiceB serviceB;

        public void setServiceB(ServiceB serviceB) {
            this.serviceB = serviceB;
        }

        public ServiceB getServiceB() {
            return serviceB;
        }

        public void callServiceB() {
            serviceB.sayHello();
        }
    }

    public class ServiceB {

        public ServiceB() {
            super();
        }

        public void sayHello() {
            System.out.println("Hello ServiceB");
        }
    }

    // 切面类可以声明三个方法, before/after/exception
    // 在主类的的方法调用前/调用后/出现异常时分别调用这三个方法...
    @Aspect({ServiceA.class,ServiceB.class})
    public static class ServiceLogAspect {
        // 负责类ServiceA和ServiceB的日志增强功能

        public ServiceLogAspect() {
            super();
        }

        public static void before(
                Object object,
                Method method,
                Object[] args) {
            System.out.println(
                    "Entering "
                    +method.getDeclaringClass().getSimpleName()
                    +"::"+method.getName()+", args: "
                    + Arrays.toString(args)
            );
        }
        public static void after(
                Object object,
                Method method,
                Object[] args,
                Object result) {
            System.out.println(
                    "Leaving "
                    +method.getDeclaringClass().getSimpleName()
                    +"::"+method.getName()
                    +", result: "+result
                    );
        }
    }

    @Aspect({ServiceB.class})
    public static class ExceptionAspect {

        public ExceptionAspect() {
            super();
        }

        public static void exception(
                Object object,
                Method method,
                Object[] args,
                Throwable throwable
        ) {
            System.err.println(
                    "exception when calling: "
                    +method.getName()+
                    ","+Arrays.toString(args));
        }
    }

    public static class CGLIBContainer {

        public enum InterceptPoint {
            BEFORE,AFTER,EXCEPTION
        }

        static Class<?>[] aspects =
                new Class<?>[] {
                        ServiceLogAspect.class,
                        ExceptionAspect.class
        };

        public static Map<Class<?>,Map<InterceptPoint, List<Method>>>
                interceptMethodsMap = new HashMap<>();

        static {init();}

        private static void init() {
            for (Class<?> cls : aspects) {
                Aspect aspect = cls.getAnnotation(Aspect.class);
                if (aspect != null) {
                    Method before = getMethod(cls, "before",
                            new Class<?>[] { Object.class, Method.class, Object[].class });
                    Method after = getMethod(cls, "after",
                            new Class<?>[] { Object.class, Method.class, Object[].class, Object.class });
                    Method exception = getMethod(cls, "exception",
                            new Class<?>[] { Object.class, Method.class, Object[].class, Throwable.class });
                    Class<?>[] intercepttedArr = aspect.value();
                    for (Class<?> interceptted : intercepttedArr) {
                        addInterceptMethod(interceptted, InterceptPoint.BEFORE, before);
                        addInterceptMethod(interceptted, InterceptPoint.AFTER, after);
                        addInterceptMethod(interceptted, InterceptPoint.EXCEPTION, exception);
                    }
                }
            }
        }

        private static Method getMethod(Class<?> cls, String name, Class<?>[] paramTypes) {
            try {
                return cls.getMethod(name, paramTypes);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        private static void addInterceptMethod(Class<?> cls, InterceptPoint point, Method method) {
            if (method == null) {
                return;
            }
            Map<InterceptPoint, List<Method>> map = interceptMethodsMap.get(cls);
            if (map == null) {
                map = new HashMap<>();
                interceptMethodsMap.put(cls, map);
            }
            List<Method> methods = map.get(point);
            if (methods == null) {
                methods = new ArrayList<>();
                map.put(point, methods);
            }
            methods.add(method);
        }

        static List<Method> getInterceptMethods(Class<?> cls,
                                                InterceptPoint point) {
            Map<InterceptPoint, List<Method>> map = interceptMethodsMap.get(cls);
            if (map == null) {
                return Collections.emptyList();
            }
            List<Method> methods = map.get(point);
            if (methods == null) {
                return Collections.emptyList();
            }
            return methods;
        }

        static class AspectInterceptor implements MethodInterceptor {

            public AspectInterceptor() {
                super();
            }

            @Override
            public Object intercept(Object object, Method method,
                                    Object[] args, MethodProxy proxy) throws Throwable {
                //执行before方法
                List<Method> beforeMethods = getInterceptMethods(
                        object.getClass().getSuperclass(), InterceptPoint.BEFORE);
                for (Method m : beforeMethods) {
                    m.invoke(null, new Object[] { object, method, args });
                }

                try {
                    // 调用原始方法
                    Object result = proxy.invokeSuper(object, args);

                    // 执行after方法
                    List<Method> afterMethods = getInterceptMethods(
                            object.getClass().getSuperclass(), InterceptPoint.AFTER);
                    for (Method m : afterMethods) {
                        m.invoke(null, new Object[] { object, method, args, result });
                    }
                    return result;
                } catch (Throwable e) {
                    //执行exception方法
                    List<Method> exceptionMethods = getInterceptMethods(
                            object.getClass().getSuperclass(), InterceptPoint.EXCEPTION);
                    for (Method m : exceptionMethods) {
                        m.invoke(null, new Object[] { object, method, args, e });
                    }
                    throw e;
                }
            }
        }

        private static <T> T createInstance(Class<T> cls)
                throws InstantiationException, IllegalAccessException {
            if (!interceptMethodsMap.containsKey(cls)) {
                return (T) cls.newInstance();
            }
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(cls);
            enhancer.setCallback(new AspectInterceptor());
            return (T) enhancer.create();
        }

        public static <T> T getInstance(Class<T> cls) {
            try {
                T obj = createInstance(cls);
                Field[] fields = cls.getDeclaredFields();
                for (Field f : fields) {
                    if (f.isAnnotationPresent(AnnotationApp2.SimpleInject.class)) {
                        if (!f.isAccessible()) {
                            f.setAccessible(true);
                        }
                        Class<?> fieldCls = f.getType();
                        f.set(obj, getInstance(fieldCls));
                    }
                }
                return obj;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        ServiceA a =
                CGLIBContainer.getInstance(ServiceA.class);
        a.callServiceB();
    }
}