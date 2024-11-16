import java.net.*;
import java.io.*;
 
public class Client2 {
 
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 1234;
    private static final int UNSIGNED_SHORT_MAX_VALUE = 65535;
    private static final int UNSIGNED_SHORT_MIN_VALUE = 0;
 
    public static void main(String args[]) {
        Socket socket = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            int a = readUnsignedShort("a = ", reader);
            int b = readUnsignedShort("b = ", reader);
            writeIntegersToSocket(a, b, socket);
            readIntegersSumFromSocket(socket);
        } catch (IOException e) {
            System.err.println("Cautgh exception " + e.getMessage());
        } finally {
            closeStreams(socket,reader);
        }
    }
 
    private static void readIntegersSumFromSocket(Socket c) throws IOException {
        DataInputStream socketIn = new DataInputStream(c.getInputStream());
        int s = socketIn.readUnsignedShort();
        System.out.println("s = " + s);
    }
 
    private static void writeIntegersToSocket(int a, int b, Socket c) throws IOException {
        DataOutputStream socketOut = new DataOutputStream(c.getOutputStream());
        socketOut.writeShort(a);
        socketOut.writeShort(b);
        socketOut.flush();
    }
 
    private static int readUnsignedShort(String message, BufferedReader reader) throws IOException {
        int unsignedShortNumber = 0;
        System.out.print(message);
        try {
            unsignedShortNumber = Integer.parseInt(reader.readLine());
            if (unsignedShortNumber < UNSIGNED_SHORT_MIN_VALUE || unsignedShortNumber > UNSIGNED_SHORT_MAX_VALUE) {
                throw new IllegalArgumentException("The given number must be unsigned short [0..65535]!");
            }
        } catch (NumberFormatException e) {
            System.err.println("The given input is not an integer!");
        }
        return unsignedShortNumber;
    }
 
    private static void closeStreams(Socket socket, BufferedReader reader) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Could not close socket!");
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Could not close reader!");
            }
        }
    }
 
}