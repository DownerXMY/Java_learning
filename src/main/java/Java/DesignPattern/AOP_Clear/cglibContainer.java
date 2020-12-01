package Java.DesignPattern.AOP_Clear;

import Java.Dynamic.AnnotationApp2;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class cglibContainer {
    /*
    在写之前我们先来想想看我们已经做了什么,然后后面还要做什么?
    首先,等待被处理的对象MyService1和MyService2已经被生成了,
    紧接着,我们处理的时候调用的方法,
        像比如logBefore,logAfter,...都已经写好了
    那么我们还要什么事情没有做呢?
        第一就是事情在哪里做,我们还没有说明,
        第二就是做事情的动态代理我们还没有创建,
        那么这两件事情都将在这个.java文件中完成...
     */

    public enum InterceptPoint {
            BEFORE,AFTER,EXCEPTION
    }

    public static Class<?>[] aspects = {
            AspectActions.LogForAspect.class,
            AspectActions.ExceptionForAspect.class
    };

    public static Map<Class<?>,Map<InterceptPoint,List<Method>>>
            frameMap = new HashMap<>();
    /*
    我们为什么建了一个这么复杂的Map,大致思路是这么样的?
    其实我们想做的事情是:
        一个类对应一个Map,而这个Map中的元素是:一个切点对应一种方法
        比如对应MyService2这个类,对应的Map长度就是3,其中
        BEFORE,AFTER,EXCEPTION三个切点分别对应各自名字的方法...
        在这里将会大量用到Java反射的内容...
     */

    static {init();}
    /*
    我们知道静态代码快会在类被首次使用(初始化)的时候被执行,
    那么init()究竟干了什么事情呢?
     */

    public static void init() {

        for (Class<?> cls : aspects) {
            AspectActions.Aspect aspect =
                    cls.getAnnotation(AspectActions.Aspect.class);
            if (aspect != null) {
                /*
                为什么要做这个判断?
                因为我们写Aspect的注解的目的就是为了标明我们要写AOP的地方
                换句话说,这个判断就是帮助我们帅选出我们要写AOP的那些Class
                 */
                // 所以可想而知下面我们要写的内容就是AOP

                Method beforeMethod =
                        getMethod(cls,"logBefore",new Class<?>[] {
                                Object.class,Method.class,Object[].class});
                Method afterMethod =
                        getMethod(cls,"logAfter",new Class<?>[] {
                                Object.class,Method.class,Object[].class,Object.class});
                Method exceptionMethod =
                        getMethod(cls,"exceptionThrough",new Class<?>[] {
                                Object.class,Method.class,Object[].class,Throwable.class});

                Class<?>[] interceptionObjects = aspect.value();
                for (Class<?> interceptionObject : interceptionObjects) {
                    addInterceptMethod(
                            interceptionObject,InterceptPoint.BEFORE,beforeMethod);
                    addInterceptMethod(
                            interceptionObject,InterceptPoint.AFTER,afterMethod);
                    addInterceptMethod(
                            interceptionObject,InterceptPoint.EXCEPTION,exceptionMethod);
                }
            }
        }
    }

    private static Method getMethod(
            Class<?> cls, String name, Class<?>[] paramTypes) {
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
        Map<InterceptPoint, List<Method>> map = frameMap.get(cls);
        if (map == null) {
            map = new HashMap<>();
            frameMap.put(cls, map);
        }
        List<Method> methods = map.get(point);
        if (methods == null) {
            methods = new ArrayList<>();
            map.put(point, methods);
        }
        methods.add(method);
    }

    /*
    到此为止,我们之前说的第一件事情已经做完了,
    下面我们来创建代理类:
     */

    public static class AOPProxy implements MethodInterceptor {

        public AOPProxy() {
            super();
        }

        @Override
        public Object intercept(
                Object object, Method method,
                Object[] args, MethodProxy proxy) throws Throwable {

            // 我们就是要慢慢讲之前创建的那个Map建立起来...
            // 然后我们之前一直在疑惑,我们究竟是在哪里实现的打印日志的前后顺序
            // 其实就是在这里...
            List<Method> beforeMethods = getInterceptMethods(
                    object.getClass().getSuperclass(),InterceptPoint.BEFORE);
            for (Method method1 : beforeMethods) {
                method1.invoke(null, object, method, args);
            }

            try {
                Object result = proxy.invokeSuper(object,args);
                List<Method> afterMethods = getInterceptMethods(
                        object.getClass().getSuperclass(),InterceptPoint.AFTER);
                for (Method method1 : afterMethods) {
                    method1.invoke(null,object,method,args,result);
                }
                return result;
            } catch (Throwable e) {
                List<Method> exceptionMethods = getInterceptMethods(
                        object.getClass().getSuperclass(),InterceptPoint.EXCEPTION);
                for (Method method1 : exceptionMethods) {
                    method1.invoke(null,object,method,args,e);
                }
                throw e;
            }
        }
    }

    public static List<Method> getInterceptMethods(Class<?> cls,
                                            InterceptPoint point) {
        Map<InterceptPoint, List<Method>> map = frameMap.get(cls);
        if (map == null) {
            return Collections.emptyList();
        }
        List<Method> methods = map.get(point);
        if (methods == null) {
            return Collections.emptyList();
        }
        return methods;
    }

    /*
    注意,我们通过下面两个方法获取动态代理的实例,
    相当于在下面两个方法中new了一个AOPProxy的实例...
     */
    public static <T> T createInstance(Class<T> cls)
            throws InstantiationException, IllegalAccessException {
        if (!frameMap.containsKey(cls)) {
            return (T) cls.newInstance();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new cglibContainer.AOPProxy());
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
