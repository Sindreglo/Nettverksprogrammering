import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClientHandler extends Thread implements Runnable{

    private String[] message = {"1 element", "2 element", "3 element", "4 element"};

    ClientHandler(DataInputStream dis, DataOutputStream dos) throws IOException {

        getHeaderToArray(dis);

        dos.write("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
        dos.write("\r\n".getBytes(StandardCharsets.UTF_8));
        dos.write("<HTML><BODY>\r\n".getBytes(StandardCharsets.UTF_8));
        dos.write("<H1> Hilsen. Du har koblet deg opp til min enkle web-tjener </h1>\r\n".getBytes(StandardCharsets.UTF_8));

        for (String m :message) {
            dos.write(("<LI>" + m + "</LI>\r\n").getBytes(StandardCharsets.UTF_8));
        }

        dos.write("</UL>\r\n</BODY></HTML>".getBytes(StandardCharsets.UTF_8));

        dos.flush();
        dos.close();

    }

    public String getHeaderToArray(DataInputStream inputStream) {
        String headerTempData = "";
        Reader reader = new InputStreamReader(inputStream);
        try {
            int c;
            while ((c = reader.read()) != -1) {
                System.out.print((char) c);
                headerTempData += (char) c;

                if (headerTempData.contains("\r\n\r\n"))
                    break;
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        this.message = headerTempData.split("\r\n");

        return headerTempData;
    }
}