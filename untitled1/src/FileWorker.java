import java.io.*;

public class FileWorker {

    public static void write(String filename, String text) {

        try (FileWriter writer = new FileWriter(String filename, false)) {
            writer.write(text);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String readStringToPosition(String filename, Integer position) {

        try {
            FileInputStream fstream = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            Integer lines = 0;
            String stringToPosition = "";
            while (lines < position) {
                stringToPosition = br.readLine();
                lines += 1;
            }
            return stringToPosition;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
