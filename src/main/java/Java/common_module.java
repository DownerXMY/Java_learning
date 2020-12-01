package Java;

import com.sun.tools.javac.util.ListBuffer;

import java.util.*;
import java.text.SimpleDateFormat;

import java.util.stream.IntStream;

public class common_module {

    public static void main(String[] args) {
        // JAVA常用库
        // 其实很多之前都已经接触过了
        // 我们也没必要系统地都学一遍，不过倒是可以玩一玩
        System.out.println("Date");
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sm = new SimpleDateFormat();
        System.out.println(sm.format(d));
        SimpleDateFormat sm2 = new SimpleDateFormat("yyyy年MM月dd天E");
        System.out.println(sm2.format(d));
        System.out.println("--------------------------");
        System.out.println("Vector");
        // 向量必须要先创建后使用
        Vector v1 = new Vector();
        ListBuffer ls = new ListBuffer();
        for (int item : IntStream.rangeClosed(1,5).toArray()) {
            ls.append(item);
        }
        v1.addElement(ls);
        System.out.println(v1);
        vec2();
        // 当然Vector还要很多别的操作
        System.out.println("---------------------------");
        System.out.println("Hashtable");
        Hashtable has = new Hashtable();
        has.put("one",new Integer(1));
        has.put("two",new Integer(2));
        has.put("three",new Double(3.333d));
        Set s = has.keySet();
        for (Iterator<String> i=s.iterator();i.hasNext();) {
            System.out.println(has.get(i.next()));
        }
    }

    static public void vec2() {
        Vector v = new Vector();
        for (int item : IntStream.rangeClosed(1,5).toArray()) {
            Object obj = item;
            String str = "'"+obj.toString()+"'";
            v.addElement(str);
        }
        System.out.println(v);
    }
}
