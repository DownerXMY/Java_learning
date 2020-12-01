package Java.Dynamic;
/*
最后,我们讲述通过Java反射获取泛型的信息
直接通过一个简单的实例...
 */

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

public class ReflectionApp3 {

    static class Generic<U extends Comparable<U>,V> {

        U u;
        V v;

        List<String> stringList;

        public U test(List<? extends Number> numbers) {
            return null;
        }

    }

    public static void main(String[] args) throws Exception {
        Class<?> cls = Generic.class;
        // 类的类型参数
        for (TypeVariable typeVariable : cls.getTypeParameters()) {
            System.out.println(
                    typeVariable.getName()
                            +" extends "
                            + Arrays.toString(typeVariable.getBounds()));
        }
        // 字段:泛型类型
        Field field = cls.getDeclaredField("u");
        System.out.print(field.getGenericType()+" ");
        Field field2 = cls.getDeclaredField("v");
        System.out.print(field2.getGenericType());
        System.out.println();
        // 字段:参数化的类型
        Field field1 = cls.getDeclaredField("stringList");
        Type type = field1.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType =
                    (ParameterizedType) type;
            System.out.println(
                    "raw type: "
                    +parameterizedType.getRawType()
                    +",type arguments: "
                    +Arrays.toString(parameterizedType.getActualTypeArguments()));
        }
        // 方法的泛型参数
        Method method = cls.getMethod("test", List.class);
        for (Type type1 : method.getGenericParameterTypes()) {
            System.out.println(type1);
        }
    }
}
