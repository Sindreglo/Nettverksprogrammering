
import java.io.*;
import java.net.*;
import java.util.Scanner;
class SocketKlient {
    public static void main(String[] args) throws IOException {
        SocketKlient klient = new SocketKlient();

        /* Bruker en scanner til å lese fra kommandovinduet */
        Scanner leserFraKommandovindu = new Scanner(System.in);
        System.out.print("Oppgi navnet på maskinen der tjenerprogrammet kjører: ");
        String tjenermaskin = leserFraKommandovindu.nextLine();

        /* Setter opp forbindelsen til tjenerprogrammet */
        Socket forbindelse = new Socket("localhost", Integer.parseInt(tjenermaskin));
        System.out.println("Nå er forbindelsen opprettet.");

        /* Åpner en forbindelse for kommunikasjon med tjenerprogrammet */
        InputStreamReader leseforbindelse
                = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        /* Leser innledning fra tjeneren og skriver den til kommandovinduet */
        String innledning1 = "(Fra tjenerprogrammet) " + leseren.readLine();
        System.out.println(innledning1);

        boolean run = true;

        while (run) {
            klient.run(leserFraKommandovindu, skriveren, leseren);
            String respons = leseren.readLine();
            System.out.println(respons);
            String enLinje = leserFraKommandovindu.nextLine();
            skriveren.println(enLinje);
            if (!leseren.readLine().equals("1")) {
                run = false;
            }
        }

        /* Lukker forbindelsen */
        System.out.println("Avslutter...");
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }



    private void run(Scanner leser, PrintWriter skriver, BufferedReader leseren) throws IOException {
        String innledning2 = "(Fra tjenerprogrammet) " + leseren.readLine();
        System.out.println(innledning2);
        String enLinje = leser.nextLine();
        if (enLinje.equals("Feil input")) return;

        skriver.println(enLinje);  // sender teksten til tjeneren
        String respons = leseren.readLine();  // mottar respons fra tjeneren
        System.out.println("(Fra tjenerprogrammet) " + respons);
        enLinje = leser.nextLine();
        if (enLinje.equals("Feil input")) return;

        skriver.println(enLinje);  // sender teksten til tjeneren
        respons = leseren.readLine();  // mottar respons fra tjeneren
        System.out.println("(Fra tjenerprogrammet) " + respons);
        enLinje = leser.nextLine();
        if (enLinje.equals("Feil input")) return;

        skriver.println(enLinje);  // sender teksten til tjeneren
        respons = leseren.readLine();  // mottar respons fra tjeneren
        System.out.println("(Fra tjenerprogrammet) " + respons);
    }


}

