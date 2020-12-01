package Java.Dynamic;
/*
之前我们一直是在看注解的创建以及信息获取
最后我们还是要来看看注解究竟能有什么实质性的作用...
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AnnotationApp1 {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Label {
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Format {
        String pattern() default "yyyy-MM-dd:mm:ss";
        String timezone() default "GMT+8";
    }

    static class Girl {
        // 其实这里我们会看到最终注解的信息会被Field应用
        /*
        一定要明白,为什么我们反复说这是反射带来的意义:
        ----------------
        反射帮助我们做的就是获得一切"字段"的信息...
        Class<?> cls = Girl.getClass();
        for (Field field : cls.getDeclaredFields()) {...}
        ----------------
        注解帮助我们做的就是是的我们能够利用API改变字段的一些信息...
        我们用这里的 "Date birthday" 举例:
        因为程序是很多人写出来的,
        我们需要把输出的结果转换为一个固定的格式,
        比如有的人在main中写的SimpleDateFormat格式是"yyyy-MM-dd"
        另外还有的格式是"yyyy~MM~dd",这些都是无法预料的
        但是现在因为有了
        @Format {String pattern() default "XXX"}
        Format format = field.getAnnotation(Format.class);
        SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat(format.pattern());
        这就使得我们能够提取字段注解中的信息,
        并在代码中重新利用,做到输出结果的统一...
        ----------------
        总结起来,
        1.注解之所以能够被利用,原则上是Java反射机制在起作用
        2.注解本身没有任何功能,我们只是提取注解中的信息,并将功能在代码中实现
        3.由此可见Java反射的操作是很重要的,Java Reflection时候面Java-SSH的重要基础!
         */
        @Label("name")
        String name;

        @Label("Birthday")
        @Format(pattern="yyyy-MM-dd")
        Date birthday;

        @Label("score")
        double score;

        Girl(String name1,Date birthday1,double score1) {
            this.name = name1;
            this.birthday = birthday1;
            this.score = score1;
        }
    }

    public static String format(Object object) {
        try {
            Class<?> cls = object.getClass();
            StringBuilder stringBuilder =
                    new StringBuilder();
            for (Field field : cls.getDeclaredFields()) {
                if (! field.isAccessible()) {
                    field.setAccessible(true);
                }
                Label label =
                        field.getAnnotation(Label.class);
                String name =
                        label != null ? label.value() : field.getName();
                Object value = field.get(object);
                if (value != null && field.getType() == Date.class) {
                    value = formatDate(field,value);
                }
                stringBuilder.append(name+": "+value+"\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static Object formatDate(
            Field field, Object value) {
        Format format = field.getAnnotation(Format.class);
        if (format != null) {
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat(format.pattern());
            simpleDateFormat.setTimeZone(
                    TimeZone.getTimeZone(format.timezone()));
            return simpleDateFormat.format(value);
        }
        return value;
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat1 =
                new SimpleDateFormat("yyyy+MM+dd");
        Girl girl1 =
                new Girl(
                        "QianruYu",
                        simpleDateFormat1.parse("1999+04+12"),
                        98.0d
                );
        System.out.println(AnnotationApp1.format(girl1));
    }
}
