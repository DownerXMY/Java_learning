package Java.JVM;

public class MySample {

    public MySample() {
        System.out.println(
                "Sample is loaded by: "
                        +this.getClass().getClassLoader()
        );

        new TestApp2();
    }
}
