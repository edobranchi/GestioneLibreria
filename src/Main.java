import Libri.Libreria;
import Libri.Libro;
import Libri.Prestito;
import Persone.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public Main() {

    }

    public static void pulisciSchermo() {
        for (int i = 0; i < 20; ++i) {
            System.out.println();
        }
    }

    public static int inserimento(int min, int max) {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("\nFai una scelta: ");
            String scelta = input.next();
            if (!scelta.matches(".*[a-zA-Z]+.*") && Integer.parseInt(scelta) > min && Integer.parseInt(scelta) < max) {
                return Integer.parseInt(scelta);
            }

            System.out.println("\nInput non valido");
        }
    }

    public static void funzionalita(Persona persona, int scelta) throws IOException {
        Libreria libreria = Libreria.getInstance();
        CentroClientiPersonale ccp= CentroClientiPersonale.getInstance();
        //Ricerca di un libro [INPUT 1]
        if (scelta == 1) {
            libreria.ricercaLibro();
        } else {
            ArrayList<Libro> libri;
            Libro libro;
            Cliente cliente;
            int input;
            //Prenotazione di un libro [INPUT 2]
            if (scelta == 2) {
                libri = libreria.ricercaLibro();
                if (libri != null) {
                    input = inserimento(-1, libri.size());
                    libro = libri.get(input);
                    if (!"Persone.Cassiere".equals(persona.getClass().getSimpleName()) && !"Persone.Libraio".equals(persona.getClass().getSimpleName())) {
                        libro.creaPrenotazione((Cliente) persona);
                    } else {
                        cliente = ccp.trovaCliente();
                        if (cliente != null) {
                            libro.creaPrenotazione(cliente);
                        }
                    }
                }
            } else {
                //Stampa dati personali[INPUT 3]
                if (scelta == 3) {
                    if (!"Persone.Cassiere".equals(persona.getClass().getSimpleName()) && !"Persone.Libraio".equals(persona.getClass().getSimpleName())) {
                        persona.stampaInfo();
                    } else {
                        cliente = ccp.trovaCliente();
                        if (cliente != null) {
                            cliente.stampaInfo();
                        }
                    }
                }
                //Controllo Multe [INPUT 4]
                else if (scelta == 4) {
                    if (!"Persone.Cassiere".equals(persona.getClass().getSimpleName()) && !"Persone.Libraio".equals(persona.getClass().getSimpleName())) {
                        double totalFine = libreria.calcolaMultaTotale((Cliente) persona);
                        if(totalFine!=0){
                        System.out.println("\nLa tua multa totale è: " + totalFine);
                        }else {
                            System.out.println("\nNon ci sono multe da pagare.");
                        }
                        for(int i=0;i<libreria.getPrestiti().size();i++){
                            if(libreria.getPrestiti().get(i).getCliente().getId()== persona.getId()){
                                if(libreria.getPrestiti().get(i).calcolaMulta()>0){
                                    System.out.println("\nMulta : "+ i );
                                }
                                libreria.getPrestiti().get(i).pagaMulta();
                            }
                        }

                    } else {
                        cliente = ccp.trovaCliente();
                        if (cliente != null) {
                            double totalFine = libreria.calcolaMultaTotale(cliente);
                            if(totalFine!=0){
                                System.out.println("\nLa tua multa totale è: " + totalFine);
                            }else {
                                System.out.println("\nNon ci sono multe da pagare.");
                            }
                            for(int i=0;i<libreria.getPrestiti().size();i++){
                            if(libreria.getPrestiti().get(i).getCliente().getId()== cliente.getId()){
                                if(libreria.getPrestiti().get(i).calcolaMulta()>0){
                                    System.out.println("\nMulta : "+ i );
                                }
                                libreria.getPrestiti().get(i).pagaMulta();
                            }
                            }

                        }
                    }
                }
                //Controllo coda di prenotazione di un libro [INPUT 5]
                else if (scelta == 5) {
                    libri = libreria.ricercaLibro();
                    if (libri != null) {
                        input = inserimento(-1, libri.size());
                        libri.get(input).stampaPrenotazioni();
                    }
                }
                //Libri.Prestito libro [INPUT 6], Solo cassiere e libraio
                else if (scelta == 6) {
                    libri = libreria.ricercaLibro();
                    if (libri != null) {
                        input = inserimento(-1, libri.size());
                        libro = libri.get(input);
                        cliente = ccp.trovaCliente();
                        if (cliente != null) {
                            libro.libroInPrestito(cliente, (Impiegati) persona);
                        }
                    }
                } else {
                    ArrayList prestiti;
                    //Rientro libro da un prestito [INPUT 7], Solo cassiere e libraio
                    if (scelta == 7) {
                        cliente = ccp.trovaCliente();
                        if (cliente != null) {
                            cliente.stampaLibriInPrestito();
                            prestiti = cliente.getLibriInPrestito();
                            if (!prestiti.isEmpty()) {
                                input = inserimento(-1, prestiti.size());
                                Prestito l = (Prestito) prestiti.get(input);
                                l.getLibro().restituzioneLibro(cliente, l, (Impiegati) persona);
                            } else {
                                System.out.println("\nIl cliente " + cliente.getNome() + " non ha libri in prestito");
                            }
                        }
                    }
                    //Rinnovo libro [INPUT 8], Solo cassiere e libraio
                    else if (scelta == 8) {
                        cliente = ccp.trovaCliente();
                        if (cliente != null) {
                            cliente.stampaLibriInPrestito();
                            prestiti = cliente.getLibriPrenotati();
                            if (!prestiti.isEmpty()) {
                                input = inserimento(-1, prestiti.size());
                                Prestito l = (Prestito) prestiti.get(input);
                                l.rinnovaPrestito(new Date());
                            } else {
                                System.out.println("\nQuesto cliente  " + cliente.getNome() + " non ha libri da rinnovare.");
                            }
                        }
                    }
                    //Creazione cliente [INPUT 9], Solo cassiere e libraio
                    else if (scelta == 9) {
                        ccp.creaPersona('b');
                    }
                    //Aggiornamento informazioni [INPUT 10], Solo cassiere e libraio
                    else if (scelta == 10) {
                        cliente = ccp.trovaCliente();
                        if (cliente != null) {
                            cliente.aggiornaInformazioniCliente();
                        }
                    }
                    //Aggiunta nuovo libro [INPUT 11], Solo libraio
                    else if (scelta == 11) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        System.out.println("\nInserisci titolo da aggiungere :");
                        String title = reader.readLine();
                        System.out.println("\nInserisci genere del libro da aggiungere: ");
                        String subject = reader.readLine();
                        System.out.println("\nInserisci autore del libro da aggiungere: ");
                        String author = reader.readLine();
                        libreria.creaLibro(title, subject, author);
                    }
                    //Rimozione libro [INPUT 12], Solo libraio
                    else if (scelta == 12) {
                        libri = libreria.ricercaLibro();
                        if (libri != null) {
                            input = inserimento(-1, libri.size());
                            libreria.rimuoviLibro(libri.get(input));
                        }
                    }
                    //Cambio informazioni libro [INPUT 13], Solo libraio
                    else if (scelta == 13) {
                        libri = libreria.ricercaLibro();
                        if (libri != null) {
                            input = inserimento(-1, libri.size());
                            libri.get(input).cambiaInfoLibro();
                        }
                    }
                    //Stampa informazioni cassiere [INPUT 14], Solo libraio
                    else if (scelta == 14) {
                        Cassiere clerk = ccp.trovaCassiere();
                        if (clerk != null) {
                            clerk.stampaInfo();
                        }
                    }
                }
            }
        }
        System.out.println("\nPremi Invio per continuare\n");
        System.in.read();
    }

    public static void main(String[] args) throws SQLException {
        try {
            Scanner admin = new Scanner(System.in);
            Libreria lib = Libreria.getInstance();
            CentroClientiPersonale ccp=CentroClientiPersonale.getInstance();
            lib.setMulta_al_giorno(20);
            lib.setScadenza_prestiti(5);
            lib.setScadenza_prenotazioni(7);

            Connection con = lib.makeConnection();
            if (con == null) {
                System.out.println("\nErrore nella connessione al DB");
            } else {
                try {
                    lib.populateLibrary(con, lib,ccp);
                    boolean stop = false;

                    while (!stop) {
                        pulisciSchermo();
                        System.out.println("--------------------------------------------------------");
                        System.out.println("\tGestione Libri.Libreria");
                        System.out.println("--------------------------------------------------------");
                        System.out.println("Scegli cosa vuoi fare: \n");
                        System.out.println("1- Login");
                        System.out.println("2- Esci");
                        System.out.println("3- Funzioni amministrative");
                        System.out.println("-----------------------------------------\n");

                        int scelta = inserimento(0, 4);
                        if (scelta == 3) {
                            System.out.println("\nInserisci password: ");
                            String aPass = admin.next();
                            if (aPass.equals("libraio")) {
                                while (true) {
                                    pulisciSchermo();
                                    System.out.println("--------------------------------------------------------");
                                    System.out.println("\tAmministrazione");
                                    System.out.println("--------------------------------------------------------");
                                    System.out.println("Scegli cosa vuoi fare: \n");
                                    System.out.println("1- Aggiungi cassiere");
                                    System.out.println("2- Aggiungi libraio");
                                    System.out.println("3- Stampa storico prestiti");
                                    System.out.println("4- Stampa inventario Libri");
                                    System.out.println("5- Esci");
                                    System.out.println("---------------------------------------------");
                                    scelta = inserimento(0, 6);
                                    if (scelta == 5) {
                                        break;
                                    }

                                    if (scelta == 1) {
                                        ccp.creaPersona('c');
                                    } else if (scelta == 2) {
                                        ccp.creaPersona('l');
                                    } else if (scelta == 3) {
                                        lib.stampaStorico();
                                    } else if (scelta == 4) {
                                        lib.stampaTuttiILibri();
                                    }

                                    System.out.println("\nPremi invio per continuare\n");
                                    System.in.read();
                                }
                            } else {
                                System.out.println("\nPassword errata");
                            }
                        } else if (scelta == 1) {
                            Persona persona = ccp.login();
                            if (persona != null) {
                                if (persona.getClass().getSimpleName().equals("Persone.Cliente")) {
                                    while (true) {
                                        pulisciSchermo();
                                        System.out.println("--------------------------------------------------------");
                                        System.out.println("\tPersone.Cliente:");
                                        System.out.println("--------------------------------------------------------");
                                        System.out.println("Scegli cosa vuoi fare: \n");
                                        System.out.println("1- Ricerca un libro");
                                        System.out.println("2- Prenota un libro");
                                        System.out.println("3- Stampa i tuoi dati");
                                        System.out.println("4- Controlla le tue multe");
                                        System.out.println("5- Controlla prenotazioni per un libro");
                                        System.out.println("6- Esci");
                                        System.out.println("--------------------------------------------------------");
                                        scelta = inserimento(0, 7);
                                        if (scelta == 6) {
                                            break;
                                        }

                                        funzionalita(persona, scelta);
                                    }
                                } else if (persona.getClass().getSimpleName().equals("Persone.Cassiere")) {
                                    while (true) {
                                        pulisciSchermo();
                                        System.out.println("--------------------------------------------------------");
                                        System.out.println("\tPersone.Cassiere");
                                        System.out.println("--------------------------------------------------------");
                                        System.out.println("Scegli cosa vuoi fare: \n");
                                        System.out.println("1- Cerca un libro");
                                        System.out.println("2- Prenota un libro");
                                        System.out.println("3- Controlla informazioni cliente");
                                        System.out.println("4- Controlla multe cliente");
                                        System.out.println("5- Controlla coda prenotazioni di un libro");
                                        System.out.println("6- Dai in prestito un libro");
                                        System.out.println("7- Ricevi dal prestito un libro");
                                        System.out.println("8- Rinnova un libro");
                                        System.out.println("9- Aggiungi nuovo cliente");
                                        System.out.println("10- Cambia informazioni cliente");
                                        System.out.println("11- Esci");
                                        System.out.println("--------------------------------------------------------");
                                        scelta = inserimento(0, 12);
                                        if (scelta == 11) {
                                            break;
                                        }

                                        funzionalita(persona, scelta);
                                    }
                                } else if (persona.getClass().getSimpleName().equals("Persone.Libraio")) {
                                    while (true) {
                                        pulisciSchermo();
                                        System.out.println("--------------------------------------------------------");
                                        System.out.println("\tPersone.Libraio");
                                        System.out.println("--------------------------------------------------------");
                                        System.out.println("Scegli cosa vuoi fare: \n");
                                        System.out.println("1- Cerca un libro");
                                        System.out.println("2- Prenota un libro");
                                        System.out.println("3- Stampa informazioni cliente");
                                        System.out.println("4- Controlla multa cliente");
                                        System.out.println("5- Controlla coda prenotazione di un libro");
                                        System.out.println("6- Dai in prestito un libro");
                                        System.out.println("7- Ricevi dal prestito un libro");
                                        System.out.println("8- Rinnova un libro");
                                        System.out.println("9- Aggiungi nuovo cliente");
                                        System.out.println("10- Aggiorna informazioni cliente");
                                        System.out.println("11- Aggiungi un libro");
                                        System.out.println("12- Rimuovi un libro");
                                        System.out.println("13- Cambia informazioni libro");
                                        System.out.println("14- Stampa informazioni cassiere");
                                        System.out.println("15- Esci");
                                        System.out.println("--------------------------------------------------------");
                                        scelta = inserimento(0, 16);
                                        if (scelta == 15) {
                                            break;
                                        }

                                        funzionalita(persona, scelta);
                                    }
                                }
                            }
                        } else {
                            stop = true;
                        }

                        System.out.println("\nPremi invio per continuare...\n");
                        System.in.read();
                    }

                    lib.riempiDB(con, lib,ccp);
                } catch (Exception var7) {
                    var7.printStackTrace();
                    System.out.println("\nEsco...\n");
                }
            }
        }catch (NullPointerException ex){
    }finally {
            Libreria lib= Libreria.getInstance();
            Connection con = lib.makeConnection();
            CentroClientiPersonale ccp=CentroClientiPersonale.getInstance();
            lib.riempiDB(con,lib,ccp);
        }

    }
    
    
    
}
