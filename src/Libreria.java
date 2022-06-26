import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Libreria extends DatabaseQuery{
    private String nome = null;
    private Libraio libraio = null;
    private final ArrayList<Persona> persone = new ArrayList();
    private final ArrayList<Libro> Libri = new ArrayList();
    private final ArrayList<Prestito> prestiti = new ArrayList();
    public int scadenza_prestiti=15;
    public int multa_al_giorno=30;
    public int scadenza_prenotazioni;

    //Singleton per libreria
    private static Libreria libreria;

    public static Libreria getInstance() {
        if (libreria == null) {
            libreria = new Libreria();
        }
        return libreria;
    }

    public void setScadenza_prestiti(int scadenza_prestiti) {
        this.scadenza_prestiti = scadenza_prestiti;
    }

    public void setMulta_al_giorno(int multa_al_giorno) {
        this.multa_al_giorno = multa_al_giorno;
    }

    public void setScadenza_prenotazioni(int scadenza_prenotazioni) {
        this.scadenza_prenotazioni = scadenza_prenotazioni;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public Libraio getLibraio() {
        return libraio;
    }

    public ArrayList<Persona> getPersone() {
        return persone;
    }

    public ArrayList<Libro> getLibri() {
        return Libri;
    }

    public ArrayList<Prestito> getPrestiti() {
        return prestiti;
    }

    public int getScadenza_prenotazioni() {
        return scadenza_prenotazioni;
    }

    public boolean aggiungiLibraio(Libraio libraio) {
        if (this.libraio == null) {
            this.libraio = libraio;
            this.persone.add(this.libraio);
            System.out.println("Libraio aggiunto con successo");
            return true;
        } else {
            System.out.println("Impossibile aggiungere libraio perchè esiste già");
            return false;
        }
    }

    public void aggiungiCassiere(Cassiere cassiere) {
        this.persone.add(cassiere);
    }

    public void aggiungiCliente(Cliente cliente) {
        this.persone.add(cliente);
    }

    public void aggiungiPrestito(Prestito prestito) {
        this.prestiti.add(prestito);
    }

    public Cliente trovaCliente() {
        System.out.println("\nInserisci l'ID del cliente da trovare: ");
        int idcliente = 0;
        Scanner scanner = new Scanner(System.in);
        try {
            idcliente = scanner.nextInt();
        } catch (InputMismatchException var4) {
            System.out.println("\nInput non valido");
        }
        for (int i = 0; i < this.persone.size(); ++i) {
            if (( this.persone.get(i)).getId() == idcliente && ( this.persone.get(i)).getClass().getSimpleName().equals("Cliente")) {
                return (Cliente) this.persone.get(i);
            }

        }
        System.out.println("\nNon sono presenti clienti con quell' ID");
        return null;
    }

    public Cassiere trovaCassiere() {
        System.out.println("\nInserisci ID cassiere: ");
        int idcassiere = 0;
        Scanner scanner = new Scanner(System.in);

        try {
            idcassiere = scanner.nextInt();
        } catch (InputMismatchException var4) {
            System.out.println("\nInput non valido");
        }
        for (int i = 0; i < this.persone.size(); ++i) {
            if (this.persone.get(i).getId() == idcassiere && this.persone.get(i).getClass().getSimpleName().equals("Cassiere")) {
                return (Cassiere) this.persone.get(i);
            }
        }

        System.out.println("\nNon esiste nessun cassiere con quell'ID.");
        return null;
    }

    public void aggiungiLibro(Libro libro) {
        this.Libri.add(libro);
    }

    public void rimuoviLibro(Libro libro) {
        boolean delete = true;
        for (int i = 0; i < this.persone.size() && delete; ++i) {
            if (this.persone.get(i).getClass().getSimpleName().equals("Cliente")) {
                ArrayList<Prestito> libriPrestito = ((Cliente) this.persone.get(i)).getLibriInPrestito();
                for (int j = 0; i < libriPrestito.size() && delete; ++j) {
                    if (libriPrestito.get(j).getLibro() == libro) {
                        delete = false;
                        System.out.println("Questo libro è attualmente in prestito e non può essere rimosso");
                    }
                }
            }
        }
        if (delete) {
            ArrayList<RichiestaPrestito> richiestaPrestito = libro.getLibriPrenotati();
            if (!richiestaPrestito.isEmpty()) {
                System.out.println("\nQuesto libro potrebbe essere prenotato cancellare comunque?");
                Scanner sc = new Scanner(System.in);

                while (true) {
                    while (true) {
                        String input = sc.next();
                        if (!input.equals("y") && !input.equals("n")) {
                            System.out.println("Input non valido (y/n): ");
                        } else {
                            if (input.equals("n")) {
                                System.out.println("\nRimozione libro annullata.");
                                return;
                            }
                            for (int i = 0; i < richiestaPrestito.size() && delete; ++i) {
                               RichiestaPrestito hr = richiestaPrestito.get(i);
                                hr.getCliente().rimuoviPrenotazione(hr);
                                libro.rimuoviPrenotazione();
                            }
                        }
                    }
                }
            }

            this.Libri.remove(libro);
            System.out.println("Libro rimosso con successo");
        } else {
            System.out.println("\nCancellazione annullata");
        }

    }

    public ArrayList<Libro> ricercaLibro() throws IOException {
        String titolo = "";
        String genere = "";
        String autore = "";
        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("\nInserisci 1 per cercare per titolo, 2 per cercare per genere,3 per cercare per autore:");
            String choice = sc.next();
            if (choice.equals("1") || choice.equals("2") || choice.equals("3")) {
                if (choice.equals("1")) {
                    System.out.println("\nInserisci il titolo:");
                    titolo = reader.readLine();
                } else if (choice.equals("2")) {
                    System.out.println("\nInserisci il genere: ");
                    genere = reader.readLine();
                } else {
                    System.out.println("\nInserisci l'autore: ");
                    autore = reader.readLine();
                }
                ArrayList<Libro> libriCorrispondenti = new ArrayList();

                int i;
                for (i = 0; i < this.Libri.size(); ++i) {
                    Libro libro = this.Libri.get(i);
                    if (choice.equals("1")) {
                        if (libro.getTitolo().equals(titolo)) {
                            libriCorrispondenti.add(libro);
                        }
                    }
                    if (choice.equals("2")) {
                        if (libro.getGenere().equals(genere)) {
                            libriCorrispondenti.add(libro);
                        }
                    }
                    if (choice.equals("3")) {
                        if (libro.getAutore().equals(autore)) {
                            libriCorrispondenti.add(libro);
                        }
                    }
                }
                if (libriCorrispondenti.isEmpty()) {
                    System.out.println("\nNon sono stati trovati titoli corrispondenti alla ricerca");
                    return null;
                } else {
                    System.out.println("\nSono stati trovati i seguenti titoli:");
                    for (i = 0; i < libriCorrispondenti.size(); ++i) {
                        System.out.print(i + "-\t\t");
                        libriCorrispondenti.get(i).stampaInfo();
                        System.out.print("\n");
                    }
                    return libriCorrispondenti;
                }
            }
            System.out.println("\nInput errato");


        }
    }

    public void stampaTuttiILibri() {
        if (!this.Libri.isEmpty()) {
            System.out.println("Libri presenti nel catalogo");
            for (int i = 0; i < this.Libri.size(); ++i) {
                System.out.print(i + "-\t\t");
                this.Libri.get(i).stampaInfo();
                System.out.print("\n");
            }
        } else {
            System.out.println("\nNon sono presenti libri");
        }
    }

    public double calcolaMultaTotale(Cliente cliente) {
        double multaTotale = 0.0;
        double multaPerLibro = 0.0;
        for (int i = 0; i < this.prestiti.size(); ++i) {
            Prestito prestito = this.prestiti.get(i);
            if (prestito.getCliente() == cliente) {
                multaPerLibro = prestito.calcolaMulta();
                System.out.print(i + "-\t\t" + this.prestiti.get(i).getLibro().getTitolo() + "   " + this.prestiti.get(i).getCliente().getNome() + "\t\t" + this.prestiti.get(i).getDataInizioPrestito() + "\t\t\t" + this.prestiti.get(i).getDataFinePrestito() + "\t\t\t\t" + multaPerLibro + "\n");
                multaTotale += multaPerLibro;
            }
        }
        return multaTotale;
    }

    public void creaPersona(char x) {
        Scanner scan = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nInserisci nome persona da creare: ");
        String nome = "";
        try {
            nome = reader.readLine();
        } catch (IOException var14) {
           System.out.println("\nInput non valido");
        }
        System.out.println("Inserisci indirizzo ");

        String indirizzo = "";

        try {
            indirizzo = reader.readLine();
        } catch (IOException var13) {
            System.out.println("\nInput non valido");
        }

        long numerotelefono = 0;

        try {
            System.out.println("Inserisci numero di telefono: ");
            numerotelefono = scan.nextLong();
        } catch (InputMismatchException var12) {
            System.out.println("\nInput non valido");
        }
        double salario;
        if (x == 'c') {
            salario = 0.0;
            try {
                System.out.println("Inserisci stipendio: ");
                salario = scan.nextDouble();
            } catch (InputMismatchException var11) {
                System.out.println("\nInput non valido");
            }
            Cassiere cassiere = new Cassiere(-1, nome, indirizzo, numerotelefono, salario, -1);
            this.aggiungiCassiere(cassiere);
            System.out.println("\nCreato cassiere di nome " + nome + " con successo.");
            System.out.println("\nIl tuo ID è : " + cassiere.getId());
            System.out.println("Your Password is : " + cassiere.getPassword());
        } else if (x == 'l') {
            salario = 0.0;

            try {
                System.out.println("Inserisci stipendio:");
                salario = scan.nextDouble();
            } catch (InputMismatchException var10) {
                System.out.println("\nInput non valido");
            }

            Libraio libraio = new Libraio(-1, nome, indirizzo, numerotelefono, salario, -1);
            if (this.aggiungiLibraio(libraio)) {
                System.out.println("\nLibraio con nome " + nome + " creato con successo.");
                System.out.println("\nIl tuo ID è: " + libraio.getId());
                System.out.println("La tua password è : " + libraio.getPassword());
            }
        } else {
            Cliente cliente = new Cliente(-1, nome, indirizzo, numerotelefono);
            this.aggiungiCliente(cliente);
            System.out.println("\nCliente con nome " + nome + " creato con successo.");
            System.out.println("\nIl tuo id è : " + cliente.getId());
            System.out.println("La tua password è : " + cliente.getPassword());
        }

    }

    public void creaLibro(String titolo, String genere, String autore) {
        Libro libro = new Libro(-1, titolo, genere, autore, false);
        this.aggiungiLibro(libro);
        System.out.println("Libro con titolo " + libro.getTitolo() + " di " + libro.getAutore() + " e genere " + libro.getGenere() + " aggiunto alla libreria");
    }

    public Persona login() {
        Scanner input = new Scanner(System.in);
        int id = 0;
        String password = "";
        System.out.println("\nInserisci il tuo ID: ");
        try {
            id = input.nextInt();
        } catch (InputMismatchException var5) {
            System.out.println("\nInput non valido");
        }
        System.out.println("Inserisci la password ");
        password = input.next();

        for (int i = 0; i < this.persone.size(); ++i) {
            if (this.persone.get(i).getId() == id && this.persone.get(i).getPassword().equals(password)) {
                System.out.println("\nBenvenuto " + this.persone.get(i).getNome());
                return this.persone.get(i);
            }
        }
        if (this.libraio != null && this.libraio.getId() == id && this.libraio.getPassword().equals(password)) {
            System.out.println("\nBenvenuto " + this.libraio.getNome());
            return this.libraio;
        } else {
            System.out.println("\nID o Password non corrette");
            return null;
        }

    }

    public void stampaStorico() {
        if (!this.prestiti.isEmpty()) {
            System.out.println("\nLibri in prestito: ");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Numero libro.\tTitotlo\tNome cliente\t Nome cassiere prestito\t\tData inizio prestito\t\t\tNome cassiere ritiro\t\tData restituzione\t\tMulta pagata");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (int i = 0; i < this.prestiti.size(); ++i) {
                if (this.prestiti.get(i).getPrestante() != null) {
                    System.out.print(i + "-\t" + this.prestiti.get(i).getLibro().getTitolo() + "\t\t\t" + this.prestiti.get(i).getCliente().getNome() + "\t\t" + this.prestiti.get(i).getPrestante().getNome() + "\t    " + this.prestiti.get(i).getDataInizioPrestito());
                }

                if (this.prestiti.get(i).getRicevente() != null) {
                    System.out.print("\t" + this.prestiti.get(i).getRicevente().getNome() + "\t\t" + this.prestiti.get(i).getDataFinePrestito() + "\t   " + this.prestiti.get(i).isMultaPagata() + "\n");
                } else {
                    System.out.print("\t\t--\t\t\t--\t\t--\n");
                }
            }
        } else {
            System.out.println("\nNon ci sono libri in prestito");
        }
    }
}




