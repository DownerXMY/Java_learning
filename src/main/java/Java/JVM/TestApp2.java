package Java.JVM;

public class TestApp2 {

    public TestApp2() {
        System.out.println(
                "TestApp2 is loaded by: "
                        +this.getClass().getClassLoader());
        System.out.println("from TestApp2: "+MySample.class);
    }
}
