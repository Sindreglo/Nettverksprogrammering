import java.io.IOException;
import java.net.DatagramSocket;

public class Server {

    public static void main(String[] args) throws IOException {
        int port = 1000;
            DatagramSocket socket = new DatagramSocket(port);
            System.out.println("New port: " + port);
            ServerThread sThread = new ServerThread(socket);
            sThread.start();
    }

}

