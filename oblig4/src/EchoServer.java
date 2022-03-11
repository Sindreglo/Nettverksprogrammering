import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    String[] lel = new String[3];
    int nr = 0;
    int sum;
    private byte[] buf2 = new byte[256];

    public EchoServer() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);


            String received
                    = String.valueOf(data(buf));

            lel[nr] = received;
            nr++;

            System.out.println(received);

            if (received.equals("end")) {
                running = false;
                continue;
            }

            if (nr==3){
                int tall1 = Integer.parseInt(lel[0]);
                int tall2= Integer.parseInt(lel[2]);
                if (lel[1].equals("+")) {
                    sum = tall1 + tall2;
                }
                if (lel[1].equals("-")) {
                    sum = tall1 - tall2;
                }

                System.out.println("= " + sum);

                running = false;

                buf2 = String.valueOf(sum).getBytes();

                packet = new DatagramPacket(buf2, buf2.length, address, port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        socket.close();
    }

    public static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }

    public static void main(String[] args) throws SocketException {
        EchoServer e = new EchoServer();
        e.run();
    }
}