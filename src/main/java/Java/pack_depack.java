package Java;

public class pack_depack extends getType {
    // deeply understand the "extends".

    // 包装类(装箱)和拆箱
    public static void main(String[] args) {
        // A simple example follows as:
        int m = 100;
        int n = 200;
        // pack
        Integer obj_m = new Integer(m);
        Integer obj_n = new Integer(n);
        System.out.println(obj_m.equals(obj_n));
        System.out.println(obj_m.compareTo(obj_n));
        // depack
        int m1 = obj_m.intValue();
        int n1 = obj_n.intValue();
        System.out.println(m1 + " & " + n1);

        // why one may need pack and depack?
        // In python, we know there exists such transformation:
        // a = str(123) || b = int("123")'
        // One may consider whether it is also possible in JAVA

        // String => Int
        String str_ls[] = {"123","123xyz","xyz"};
        for (String str : str_ls) {
            try {
                int result = Integer.parseInt(str);
                System.out.println("can be transfered to int");
                System.out.println(result);
                System.out.println(getType(result));
            } catch (Exception e) {
                System.out.println("can not do such transformation");
            }
        }
        System.out.println("----------------------------------");
        // One of the important functions of pack is the dataType transformation.
        // Int => String
        int i1 = 100;
        String i12str = Integer.toString(i1);
        System.out.println(i12str);
        System.out.println(getType(i12str));
        System.out.println("----------------------------------");

    }
}
