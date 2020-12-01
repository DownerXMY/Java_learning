package Java.JVM.ClassFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

public class ExceptionClass {

    public void demo1() throws IOException {
        try {
            InputStream inputStream =
                    new FileInputStream("demotest.txt");
            ServerSocket serverSocket =
                    new ServerSocket(9999);
            serverSocket.accept();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Finally...");
        }
    }
}
