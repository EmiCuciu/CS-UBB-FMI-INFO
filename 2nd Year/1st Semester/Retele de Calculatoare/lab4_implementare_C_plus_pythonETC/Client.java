import java.io.*;
import java.net.*;


public class Client {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
            System.out.print("Introduceti IP-ul serverului: ");
            String ip = reader.readLine();
            
            System.out.print("Introduceti portul: ");
            int port = Integer.parseInt(reader.readLine());

            Socket socket = new Socket(ip, port);

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());

            System.out.print("Introduceti sirul de caractere: ");
            String sir = reader.readLine();

            System.out.print("Introduceti caracterul de cautat: ");
            char caracter = reader.readLine().charAt(0);

            outputStream.writeShort(sir.length());
            outputStream.writeBytes(sir);
            outputStream.writeByte((byte) caracter);

            int nrPozitii = inputStream.readShort();
            System.out.println("Numarul de aparitii: " + nrPozitii);

            for (int i = 0; i < nrPozitii; i++) {
                int pozitie = inputStream.readShort();
                System.out.println("Pozitie: " + pozitie);
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Eroare la conectarea sau comunicarea cu serverul: " + e.getMessage());
        }
    }
}
