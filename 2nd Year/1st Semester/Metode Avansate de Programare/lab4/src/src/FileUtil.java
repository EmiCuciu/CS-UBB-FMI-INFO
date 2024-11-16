package src;

import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static void clearFile(String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName, false)) {
            // Writing an empty string to clear the file
            fileWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}