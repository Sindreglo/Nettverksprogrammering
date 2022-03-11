import java.io.*;
import java.net.*;

class SocketTjener {
    public static void main(String[] args) throws IOException {
        int PORTNR = 1000;

        while (true)
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                ServerSocket tjener = new ServerSocket(PORTNR);
                s = tjener.accept();


                System.out.println("Logg for tjenersiden. Nå venter vi...");

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");
                // create a new thread object
                ClientHandler t = new ClientHandler(s, dis, dos, PORTNR);

                PORTNR++;
                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}



class ClientHandler extends Thread {
    final DataInputStream dis;
    final DataOutputStream dos;
    protected Socket s;
    private int PORTNR;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, int PORTNR)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.PORTNR = PORTNR;

    }

    public void run() {
        try {
            InputStreamReader leseforbindelse
                    = new InputStreamReader(s.getInputStream());
            BufferedReader leseren = new BufferedReader(leseforbindelse);
            PrintWriter skriveren = new PrintWriter(s.getOutputStream(), true);

            /* Sender innledning til klienten */
            skriveren.println("Hei, du har kontakt med tjenersiden!");

            boolean go = true;

            while (go) {
                clientRun(skriveren, leseren);
                skriveren.println("1: Nytt regnestykke, 2: Avslutt");
                int response = Integer.parseInt(leseren.readLine());
                System.out.println("klient "+ PORTNR + " skrev: "+response + " (Fortsett/Avslutt)");
                skriveren.println(response);
                if (response == 2) {
                    go = false;
                }
            }

            /* Lukker forbindelsen */

            System.out.println("klient " + PORTNR + " Avslutter...");
            leseren.close();
            skriveren.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void clientRun(PrintWriter s, BufferedReader l) throws IOException {
        s.println("Skriv tall nr.1:");
        try {
            int tall1 = Integer.parseInt(l.readLine());  // mottar en linje med tekst
            System.out.println("klient "+ PORTNR + " skrev: " + tall1);
            s.println("Skriv tall nr.2:");  // sender svar til klienten

            int tall2 = Integer.parseInt(l.readLine());  // mottar en linje med tekst
            System.out.println("klient "+ PORTNR + " skrev: " + tall2);
            s.println("1: Addisjon, 2: Subtraksjon");  // sender svar til klienten

            String valg = l.readLine();  // mottar en linje med tekst
            System.out.println("klient "+ PORTNR + " skrev: " + valg + "(+ eller -)");

            if (valg.equals("1")) {
                s.println(tall1 + "+" + tall2 + "=" + (tall1 + tall2));
            } else if (valg.equals("2")) {
                s.println(tall1 + "-" + tall2 + "=" + (tall1 - tall2));
            }
        } catch (NumberFormatException e) {
            s.println("Feil input");
            System.out.println("Avslutter...");
            l.close();
            s.close();
            s.close();
        }

    }
}

