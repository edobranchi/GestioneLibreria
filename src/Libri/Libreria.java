package Libri;

import DatabaseUtility.DatabaseQuery;
import Persone.CentroClientiPersonale;
import Persone.Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Libreria extends DatabaseQuery {


    private final ArrayList<Libro> Libri = new ArrayList<>();
    private final ArrayList<Prestito> prestiti = new ArrayList<>();
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
    public ArrayList<Libro> getLibri() {
        return Libri;
    }
    public ArrayList<Prestito> getPrestiti() {
        return prestiti;
    }
    public int getScadenza_prenotazioni() {
        return scadenza_prenotazioni;
    }
    public void aggiungiPrestito(Prestito prestito) {
        this.prestiti.add(prestito);
    }
    public void aggiungiLibro(Libro libro) {
        this.Libri.add(libro);
    }
    public void rimuoviLibro(Libro libro) {
        boolean delete = true;
        for (int i = 0; i < CentroClientiPersonale.getPersone().size() && delete; ++i) {
            if (CentroClientiPersonale.getPersone().get(i).getClass().getSimpleName().equals("Cliente")) {
                ArrayList<Prestito> libriPrestito = ((Cliente) CentroClientiPersonale.getPersone().get(i)).getLibriInPrestito();
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
                System.out.println("\nQuesto libro potrebbe essere prenotato cancellare comunque?(SI(s)/NO(n))");
                Scanner sc = new Scanner(System.in);


                    while (true) {
                        String input = sc.next();
                        if (!input.equals("s") && !input.equals("n")) {
                            System.out.println("Input non valido (SI(s)/NO(n)): ");
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

            this.Libri.remove(libro);
            System.out.println("Libri.Libro rimosso con successo");
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
                if (this.Libri.isEmpty()) {
                    System.out.println("Non ci sono libri in libreria");
                } else {
                    ArrayList<Libro> libriCorrispondenti = new ArrayList<>();

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
        double multaPerLibro;
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
    public void creaLibro(String titolo, String genere, String autore) {
        Libro libro = new Libro(-1, titolo, genere, autore, false);
        this.aggiungiLibro(libro);
        System.out.println("Libri.Libro con titolo : " + libro.getTitolo() + "\n" + "dell' autore : " + libro.getAutore() +"\n"+ "di genere : " + libro.getGenere() +"\n\n " +" AGGIUNTO ALLA LIBRERIA");
    }
    public void stampaStorico() {
        if (!this.prestiti.isEmpty()) {
            System.out.println("\nLibri in prestito: ");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Numero libro.\t\t\tTitolo\t\t\t\tNome cliente\t Prestante\t\t\tData inizio prestito\t\t\tRicevente\t\tData restituzione\t\tMulta pagata");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (int i = 0; i < this.prestiti.size(); ++i) {
                if (this.prestiti.get(i).getPrestante() != null) {
                    System.out.print(i + "-\t\t\t" + this.prestiti.get(i).getLibro().getTitolo() + "\t\t" + this.prestiti.get(i).getCliente().getNome() + "\t\t\t" + this.prestiti.get(i).getPrestante().getNome() + "\t\t\t   " + this.prestiti.get(i).getDataInizioPrestito());
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




