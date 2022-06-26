import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente extends Persona {
    private ArrayList<Prestito> libriInPrestito = new ArrayList();
    private ArrayList<RichiestaPrestito> libriPrenotati = new ArrayList();

    public Cliente(int id, String nome, String indirizzo, long numeroTelefono) {
        super(id, nome, indirizzo, numeroTelefono);
    }

    public void stampaInfo() {
        super.stampaInfo();
        this.stampaLibriInPrestito();
        this.stampaLibriPrenotati();
    }

    public void stampaLibriInPrestito() {
        if (this.libriInPrestito.isEmpty()) {
            System.out.println("L'utente non ha libri in prestito: ");
        } else {
            for (int i = 0; i < this.libriInPrestito.size(); i++) {
                System.out.println(i);
                ((Prestito) this.libriInPrestito.get(i)).getLibro().stampaInfo();
            }
        }
    }

    public void stampaLibriPrenotati() {
        if (this.libriPrenotati.isEmpty()) {
            System.out.println("L'utente non ha libri prenotati ");
        } else {
            for (int i = 0; i < this.libriPrenotati.size(); i++) {
                System.out.println(i);
                ((RichiestaPrestito) this.libriPrenotati.get(i)).getLibro().stampaInfo();
            }
        }
    }

    public void aggiornaInformazioniCliente() throws IOException {
        Scanner scan = new Scanner(System.in);
        BufferedReader buffreader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Vuoi cambiare il nome di " + this.getNome() + " ?");
        String scelta = scan.next();
        if (scelta.equals("s")) {
            System.out.println("Inserire nuovo nome");
            this.setNome(buffreader.readLine());
            System.out.println("Nome aggiornato con successo!");
        }
        System.out.println("Vuoi cambiare il l'indirizzo di " + this.getNome() + " ?");
        scelta = scan.next();
        if (scelta.equals("s")) {
            System.out.println("Inserire nuovo indirizzo");
            this.setIndirizzo(buffreader.readLine());
            System.out.println("Indirizzo aggiornato con successo!");

        }
        System.out.println("Vuoi cambiare il numero telefonico di " + this.getNome() + " ?");
        scelta = scan.next();
        if (scelta.equals("s")) {
            System.out.println("Inserire nuovo numero telefonico");
            this.setNumeroTelefono(Integer.parseInt(buffreader.readLine()));
            System.out.println("Numero telefonico aggiornato con successo!");
        }
        System.out.println("Le informazioni del cliente sono state aggiornate");
    }
    public void aggiungiLibroInPrestito(Prestito prestito){
        this.libriInPrestito.add(prestito);
    }
    public void rimuoviLibroInPrestito(Prestito prestito){
        this.libriInPrestito.remove(prestito);
    }
    public void aggiungiPrenotazione(RichiestaPrestito prenotazione){
        this.libriPrenotati.add(prenotazione);
    }
    public void rimuoviPrenotazione(RichiestaPrestito prenotazione){
        this.libriPrenotati.remove(prenotazione);
    }
    public ArrayList<Prestito> getLibriInPrestito(){
        return this.libriInPrestito;
    }
    public ArrayList<RichiestaPrestito> getLibriPrenotati(){
        return this.libriPrenotati;
    }
}
