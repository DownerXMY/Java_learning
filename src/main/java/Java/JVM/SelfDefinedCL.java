package Java.JVM;

import java.io.*;

public class SelfDefinedCL extends ClassLoader {

    private String DIY_NAME;
    private final String INDEX_EXTENSION = ".class";
    private String LOAD_PATH;

    public void set_PATH(String LOAD_PATH) {
        this.LOAD_PATH = LOAD_PATH;
    }

    public String getDIY_NAME() {
        return DIY_NAME;
    }

    public SelfDefinedCL(String diy_name) {
        super();
        this.DIY_NAME = diy_name;
    }

    public SelfDefinedCL(String diy_name,ClassLoader parent) {
        super(parent);
        this.DIY_NAME = diy_name;
    }

    public SelfDefinedCL(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    protected Class<?> findClass(String name)
            throws ClassNotFoundException {

        // 用于检验findClass有没有被执行...
        System.out.println("findClass invoked: "+name);
        System.out.println("MyLoaderName: "+this.DIY_NAME);

        InputStream inputStream = null;
        byte[] classData = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        String SYS_NAME = name.replace(".","/");
        // 变成系统文件路径格式,其中Mac是"/",Windows是"\"

        try {
            inputStream = new FileInputStream(
                    new File(this.LOAD_PATH
                            +SYS_NAME
                            +this.INDEX_EXTENSION)
            );
            byteArrayOutputStream = new ByteArrayOutputStream();

            int index = 0;
            while (-1 != (index = inputStream.read())) {
                byteArrayOutputStream.write(index);
            }
            classData = byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.defineClass(name,classData,0, classData.length);
    }
}
