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
        Scanner scanner = new Scanner(System.in);
        if (scelta == 1) {
            libreria.ricercaLibro();
        } else {
            ArrayList libri;
            Libro libro;
            Cliente cliente;
            int input;
            if (scelta == 2) {
                libri = libreria.ricercaLibro();
                if (libri != null) {
                    input = inserimento(-1, libri.size());
                    libro = (Libro) libri.get(input);
                    if (!"Cassiere".equals(persona.getClass().getSimpleName()) && !"Libraio".equals(persona.getClass().getSimpleName())) {
                        libro.creaPrenotazione((Cliente) persona);
                    } else {
                        cliente = libreria.trovaCliente();
                        if (cliente != null) {
                            libro.creaPrenotazione(cliente);
                        }
                    }
                }
            } else {
                if (scelta == 3) {
                    if (!"Cassiere".equals(persona.getClass().getSimpleName()) && !"Libraio".equals(persona.getClass().getSimpleName())) {
                        persona.stampaInfo();
                    } else {
                        cliente = libreria.trovaCliente();
                        if (cliente != null) {
                            cliente.stampaInfo();
                        }
                    }
                } else if (scelta == 4) {
                    if (!"Cassiere".equals(persona.getClass().getSimpleName()) && !"Libraio".equals(persona.getClass().getSimpleName())) {
                        double totalFine = libreria.calcolaMultaTotale((Cliente) persona);
                        System.out.println("\nLa tua multa totale è: " + totalFine);
                    } else {
                        cliente = libreria.trovaCliente();
                        if (cliente != null) {
                            double totalFine = libreria.calcolaMultaTotale(cliente);
                            System.out.println("\nLa tua multa totale è: " + totalFine);
                        }
                    }
                } else if (scelta == 5) {
                    libri = libreria.ricercaLibro();
                    if (libri != null) {
                        input = inserimento(-1, libri.size());
                        ((Libro) libri.get(input)).stampaPrenotazioni();
                    }
                } else if (scelta == 6) {
                    libri = libreria.ricercaLibro();
                    if (libri != null) {
                        input = inserimento(-1, libri.size());
                        libro = (Libro) libri.get(input);
                        cliente = libreria.trovaCliente();
                        if (cliente != null) {
                            libro.libroInPrestito(cliente, (Impiegati) persona);
                        }
                    }
                } else {
                    ArrayList prestiti;
                    if (scelta == 7) {
                        cliente = libreria.trovaCliente();
                        if (cliente != null) {
                            cliente.stampaLibriInPrestito();
                            prestiti = cliente.getLibriPrenotati();
                            if (!prestiti.isEmpty()) {
                                input = inserimento(-1, prestiti.size());
                                Prestito l = (Prestito) prestiti.get(input);
                                l.getLibro().restituzioneLibro(cliente, l, (Impiegati) persona);
                            } else {
                                System.out.println("\nIl cliente " + cliente.getNome() + " non ha libri in prestito");
                            }
                        }
                    } else if (scelta == 8) {
                        cliente = libreria.trovaCliente();
                        if (cliente != null) {
                            cliente.stampaLibriInPrestito();
                            prestiti = cliente.getLibriPrenotati();
                            if (!prestiti.isEmpty()) {
                                input = inserimento(-1, prestiti.size());
                                ((Prestito) prestiti.get(input)).rinnovaPrestito(new Date());
                            } else {
                                System.out.println("\nQuesto cliente  " + cliente.getNome() + " non ha libri da rinnovare.");
                            }
                        }
                    } else if (scelta == 9) {
                        libreria.creaPersona('b');
                    } else if (scelta == 10) {
                        cliente = libreria.trovaCliente();
                        if (cliente != null) {
                            cliente.aggiornaInformazioniCliente();
                        }
                    } else if (scelta == 11) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        System.out.println("\nInserisci titolo ");
                        String title = reader.readLine();
                        System.out.println("\nInserisci genere: ");
                        String subject = reader.readLine();
                        System.out.println("\nInserisci autore");
                        String author = reader.readLine();
                        libreria.creaLibro(title, subject, author);
                    } else if (scelta == 12) {
                        libri = libreria.ricercaLibro();
                        if (libri != null) {
                            input = inserimento(-1, libri.size());
                            libreria.rimuoviLibro((Libro) libri.get(input));
                        }
                    } else if (scelta == 13) {
                        libri = libreria.ricercaLibro();
                        if (libri != null) {
                            input = inserimento(-1, libri.size());
                            ((Libro) libri.get(input)).cambiaInfoLibro();
                        }
                    } else if (scelta == 14) {
                        Cassiere clerk = libreria.trovaCassiere();
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
            lib.setMulta_al_giorno(20);
            lib.setScadenza_prestiti(5);
            lib.setScadenza_prenotazioni(7);
            lib.setNome("Libreria");
            Connection con = lib.makeConnection();
            if (con == null) {
                System.out.println("\nErrore nella connessione al DB");
            } else {
                try {
                    lib.populateLibrary(con, lib);
                    boolean stop = false;

                    while (!stop) {
                        pulisciSchermo();
                        System.out.println("--------------------------------------------------------");
                        System.out.println("\tGestione Libreria");
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
                                        lib.creaPersona('c');
                                    } else if (scelta == 2) {
                                        lib.creaPersona('l');
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
                            Persona persona = lib.login();
                            if (persona != null) {
                                if (persona.getClass().getSimpleName().equals("Cliente")) {
                                    while (true) {
                                        pulisciSchermo();
                                        System.out.println("--------------------------------------------------------");
                                        System.out.println("\tCliente:");
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
                                } else if (persona.getClass().getSimpleName().equals("Cassiere")) {
                                    while (true) {
                                        pulisciSchermo();
                                        System.out.println("--------------------------------------------------------");
                                        System.out.println("\tCassiere");
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
                                } else if (persona.getClass().getSimpleName().equals("Libraio")) {
                                    while (true) {
                                        pulisciSchermo();
                                        System.out.println("--------------------------------------------------------");
                                        System.out.println("\tLibraio");
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

                    lib.riempiDB(con, lib);
                } catch (Exception var7) {
                    var7.printStackTrace();
                    System.out.println("\nEsco...\n");
                }
            }
        }catch (NullPointerException ex){
    }finally {
            Libreria lib= Libreria.getInstance();
            Connection con = lib.makeConnection();
            lib.riempiDB(con,lib);
        }

    }
    
    
    
}
