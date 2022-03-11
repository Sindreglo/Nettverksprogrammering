import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTjener {

    public static void main(String[] args){
        boolean running = true;
        int port = 60000;

        try{
            while (running){
                System.out.println("port: " + port);
                ServerSocket ss = new ServerSocket(port);
                Socket s = ss.accept();
                port++;
                System.out.println("tilkoblet");

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                ClientHandler ch = new ClientHandler(dis, dos);
            }
        }catch(Exception e){System.out.println(e);}
    }
}

