import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class ServerThread extends Thread implements Runnable {
    private DatagramSocket socket;
    private byte[] buffer;
    private static InetAddress ip;
    private int port;

    ServerThread(DatagramSocket rds) throws IOException {
        this.socket = rds;
        buffer = new byte[256];
        ip = InetAddress.getByName("192.168.0.169");
    }

    public void run(){

        while (true){
            DatagramPacket packet = new DatagramPacket(buffer, 256);

            try {
                socket.receive(packet);
                port = packet.getPort();
                System.out.println("client port: " + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String equation = new String(packet.getData(), 0, packet.getLength());
            System.out.println(equation);
            if (equation.equalsIgnoreCase("exit")){
                socket.close();
                return;
            }

            equation = calculate(equation);

            packet = new DatagramPacket(equation.getBytes(StandardCharsets.UTF_8), equation.length(), ip, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String calculate(String expression){
        String operators[]=expression.split("[0-9]+");
        String operands[]=expression.split("[+-/*]");
        int agregate = Integer.parseInt(operands[0]);
        for (int i = 1; i < operands.length; i++){
            if (operators[i].equals("+")){
                agregate += Integer.parseInt(operands[i]);
            } else if (operators[i].equals("-")){
                agregate -= Integer.parseInt(operands[i]);
            } else if (operators[i].equals("*")){
                agregate *= Integer.parseInt(operands[i]);
            } else if (operators[i].equals("/")){
                agregate /= Integer.parseInt(operands[i]);
            }
        }
        return String.valueOf(agregate);
    }

}
