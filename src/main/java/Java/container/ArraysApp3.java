package Java.container;

public class ArraysApp3 {

    public static void main(String[] args) {
        /* 数组与泛型
        这部分是非常重要的，
        因为真正的java开发不可能是基本类型数组的使用，那么简单的事情
         */
        Integer[] test1 = new Integer[] {1,2,3,4,5};
        Integer[] aftertest1 =
                new ClassParameter<Integer>().func1(test1);
        Integer[] afterCP = MethodParameter.func2(test1);
    }
}

class ClassParameter<T> {
    public T[] func1(T[] arg) {
        return arg;
    }
}

class MethodParameter {
    public static<T> T[] func2(T[] arg) {
        return arg;
    }
}