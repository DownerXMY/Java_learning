package Java.Dynamic;
/*
说了这么多有关Class类中的方法,我们来具体看看反射究竟有多大的用处...
Example1: 利用反射实现一个"通用序列化和反序列化的类"
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflectionApp2 {

    static class Student {
        String name;
        int age;
        Double score;

        Student(String name1,int age1,Double score1) {
            this.name = name1;
            this.age = age1;
            this.score = score1;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", score=" + score +
                    '}';
        }
    }

    public static void main(String[] args){
        Student MingyueXu =
                new Student("MingyueXu",22,100d);
        String result1 = SimpleMapper.toString(MingyueXu);
        System.out.println(result1);
        System.out.println("---------------------");
        Student MingyueXu2 = (Student) SimpleMapper.fromString(result1);
        System.out.println(MingyueXu2);
    }
}

class SimpleMapper {

    public static String toString(Object object) {
        try {
            Class<?> cls = object.getClass();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(cls.getName()+"\n");
            for (Field field : cls.getDeclaredFields()) {
                if (! field.isAccessible()) {
                    field.setAccessible(true);
                }
                stringBuilder.append(
                        field.getName()+" = "+field.get(object).toString()+"\n");
            }
            return stringBuilder.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object fromString(String str) {
        try {
            String[] strings = str.split("\n");
            if (strings.length < 1) {
                throw new IllegalArgumentException(str);
            }
            Class<?> cls = Class.forName(strings[0]);
            Object obj = cls.newInstance();
            if (strings.length > 1) {
                for (int i=1;i<strings.length;i++) {
                    String[] fv = strings[i].split("=");
                    if (fv.length != 2) {
                        throw new IllegalArgumentException(strings[i]);
                    }
                    Field field = cls.getDeclaredField(fv[0]);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    setFieldValue(field,obj,fv[1]);
                }
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValue(
            Field field,Object obj,String value)
            throws Exception {
        Class<?> type = field.getType();
        if (type == int.class) {
            field.setInt(obj,Integer.parseInt(value));
        } else if (type == short.class) {
            field.setShort(obj,Short.parseShort(value));
        } else if (type == float.class) {
            field.setFloat(obj,Float.parseFloat(value));
        } else if (type == double.class) {
            field.setDouble(obj,Double.parseDouble(value));
        } else if (type == boolean.class) {
            field.setBoolean(obj,Boolean.parseBoolean(value));
        } else if (type == String.class) {
            field.set(obj,value);
        } else if (type == char.class) {
            field.setChar(obj,value.charAt(0));
        } else {
            Constructor<?> constructor =
                    type.getConstructor(
                            new Class[] {String.class}
                    );
            field.set(obj,constructor.newInstance(value));
        }
    }
}
