
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("log4j2.xml");
        if (inputStream != null) {
            System.out.println("log4j2.xml found!");
        } else {
            System.out.println("log4j2.xml not found!");
        }

        javafx.application.Application.launch(StartClient.class, args);
    }
}