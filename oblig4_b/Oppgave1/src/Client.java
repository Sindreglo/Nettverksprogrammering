import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static int port;
    private static DatagramPacket packet;
    private static DatagramSocket socket;
    private static InetAddress address;
    private static byte[] buffer;

    private static void init(int portIn) throws SocketException, UnknownHostException {
        port = portIn;
        socket = new DatagramSocket();
        address = InetAddress.getByName("192.168.0.160");
        buffer = new byte[256];
    }
    private static void run(Scanner sc) throws IOException {
        while (true){
            System.out.println("Your equation: ");
            String msg = sc.nextLine();
            packet = new DatagramPacket(msg.getBytes(StandardCharsets.UTF_8), msg.length(), address, port);
            socket.send(packet);
            if (msg.equalsIgnoreCase("exit")){
                socket.close();
                return;
            }


            DatagramPacket receiver = new DatagramPacket(buffer, 256);
            try {
                socket.receive(receiver);
                System.out.println("Server port: " + receiver.getPort());
                String str = new String(receiver.getData(), 0, receiver.getLength());
                System.out.println(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Your port: ");
        init(Integer.parseInt(sc.nextLine()));
        System.out.println("Starting program");
        run(sc);
    }
}
