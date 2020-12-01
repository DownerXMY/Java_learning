package Java;

import com.sun.tools.javac.util.ListBuffer;

import java.awt.font.NumericShaper;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class demo {

    public static void main(String[] args) {
        // To see all the dataType in JAVA:
        int x = 22;
        char y = 'h';
        short z = 22;
        float w = 123.54f;
        double u = 124.32d;
        // float and double are followed by f or d.
        System.out.println(x * z + w / u);

        // JAVA dataType transformation:
        // byte,short,char => long => float => double
        // In above, the => means the transformation order.
        // Sometimes, one can also transform the dataType mandatory.
        // For instances:
        int s;
        double t;
        s = (int)34.56 + (int)12.32; // lose accuracy
        t = (double)11 + (double)13; // enhance accuracy
        System.out.println(s);
        System.out.println(t);

        // Let us see the loop and judgement in JAVA
        int i, j;
        for (i=1; i<=9; i++) {
            for (j=i;j<=9;j++) {
                System.out.println(i + "*" + j + "=" + i*j);
            }
        }
        int e;
        for (e = 1; e <= 2; e++) {
            if  (e % 2 == 0) {
                System.out.println("even");
            } else {
                System.out.println("odd");
            }
        }

//        int days = 0;
//        Scanner sc = new Scanner(System.in);
//        System.out.print("The year is: ");
//        int year = sc.nextInt();
//        System.out.print("The month is: ");
//        int month = sc.nextInt();
//
//        switch(month) {
//            case 1:
//            case 3:
//            case 5:
//            case 7:
//            case 8:
//            case 10:
//            case 12:
//                days = 31;
//                break;
//            case 4:
//            case 6:
//            case 9:
//            case 11:
//                days = 30;
//                break;
//            case 2:
//                if (year % 4 == 0) {
//                    days = 29;
//                } else {
//                    days = 28;
//                }
//                break;
//            default:
//                System.out.println("Error with the month");
//                System.exit(0);
//        }
//        System.out.println("days: "+days);

        // Array:
        int[] array1 = {1,2,3,4}; //static-build
        System.out.println("max: "+Arrays.stream(array1).max());
        float[] array2 = new float[3]; //dynamic-build
        array2[0] = 4f;
        array2[1] = 5f;
        array2[2] = 6f;
        System.out.println(array1.length);
        System.out.println(array2.length);
        float total = 0;
        for (i=1;i<=3;i++) {
            total += array2[i-1];
        }
        System.out.println("sum= "+total);

        // multi-dimensions:
        int[][] array3 = {{1,2},{3,4},{5,6,7}};
        int[][] array4 = new int[2][];
        array4[0] = new int[3];
        array4[1] = new int[5];
        // Compute the multiplication of matrix:
        float[][] m1 = {{1f,2f},{3f,4f}};
        float[][] m2 = {{3f,4f},{1f,2f}};
        float mul = 0f;
        int[] explain = {0,1};
        for (int item1 : explain) {
            for (int item2 : explain) {
                mul += m1[item1][item2] * (m2[item2][0] + m2[item2][1]);
            }
        }
        System.out.println("The multiplication of matrices:");
        System.out.println("The result desired to be 46");
        System.out.println(mul);
        System.out.println("---------------------------------");

        // String
        String name = "MingyueXu";
        // According to the OO language principle, this definition should be:
        String string_name = new String("MingyueXu");
        int Age = 20;
        float Score = 100f;
        System.out.println(name + " is of age " + Age + " and gets the score " + Score);
        System.out.println("");
        System.out.println(name.length());
        // The foreach loop method, we have already applied it on the problem of matrices multiplication.
        for (char item : name.toCharArray()) {
            System.out.print(item);
        }
        System.out.print("\n");
        for (int num : IntStream.rangeClosed(0,name.length()-1).toArray()) {
            System.out.print(name.charAt(num));
        }
        System.out.print("\n");

        // Something interesting is that there are something that
        // Combining the API of Python.
        ListBuffer ls = new ListBuffer();
        for (int item : IntStream.rangeClosed(1,9).toArray()) {
            ls.append(item);
        }
        System.out.println(ls);
        // We have see the API append, what a surprise!
        // To see the efficiency of String and StringBuffer,
        // StringBuffer is much more better.
        System.out.println("---------------------------------");

    }
}
