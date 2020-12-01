package Java;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        class Student {
            String name;
            int age;
            float score;

            void say() {
                System.out.println(name + " is od age " + age + " and gets the score " + score);
            }
        }
        // How to realize the class:
        Student st1 = new Student();
        // Note that this is another thing that are different from Scala.
        st1.name = "MingyueXu";
        st1.age = 20;
        st1.score = 100;
        st1.say();
    }
}
// Note that the public class is called "公共类", which shares the same name with the file.
// Java is also a OO language.
// One of the feature of JAVA is that the lines are all ended with ";".
// Another thing that are different from Scala is that the dataType are in front of the var.