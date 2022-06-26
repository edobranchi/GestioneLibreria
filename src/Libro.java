import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Libro {
    final private int idlibro;
    private String titolo;
    private final String genere;
    private String autore;
    private boolean inprestito;
    private final ArrayList<RichiestaPrestito> libriPrenotati;
    static int idlibroattuale=0;

    public Libro(int idlibro,String titolo,String genere,String autore,boolean inprestito){
        ++idlibroattuale;
        if(idlibro==-1){
            this.idlibro=idlibroattuale;
        }else{
            this.idlibro=idlibro;
        }
        this.titolo=titolo;
        this.autore=autore;
        this.genere=genere;
        this.inprestito=inprestito;
        this.libriPrenotati=new ArrayList();
    }
    public void aggiungiPrenotazione(RichiestaPrestito libpre){

        this.libriPrenotati.add(libpre);
    }
    public void rimuoviPrenotazione(){
        if(!this.libriPrenotati.isEmpty()){
            this.libriPrenotati.remove(0);
        }

    }
    public void stampaPrenotazioni(){
        if(!this.libriPrenotati.isEmpty()) {
            System.out.println("\nI libri prenotati sono:\n");
            System.out.println("----------------------------");
            for (int i = 0; i < this.libriPrenotati.size(); i++) {
                System.out.println(i);
                this.libriPrenotati.get(i).stampa();
            }
        }else{
                System.out.println("\n Non ci sono prenotazioni");
            }
        }
        public void stampaInfo(){
        System.out.println(this.titolo+"   "+this.autore+"   "+this.genere+ "   ");
    }
    public void cambiaInfoLibro() throws IOException {
        Scanner scanner=new Scanner(System.in);
        BufferedReader buffread= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Cambiare autore?");
        String input = scanner.next();
        if(input.equals("y")){
            System.out.println("Inserisci il nuovo autore: ");
            this.autore=buffread.readLine();
        }
        System.out.println("Vuoi cambiare Titolo?");
        input= scanner.next();
        if (input.equals("y")){
            System.out.println("inserisci nuovo titolo:");
            this.titolo=buffread.readLine();
        }
        System.out.println("Vuoi cambiare genere?");
        input= scanner.next();
        if (input.equals("y")){
            System.out.println("inserisci nuovo genere:");
            this.titolo=buffread.readLine();
        }
        System.out.println("\nLibro aggiornato");
    }
    public int getIdlibro() {
        return idlibro;
    }
    public String getTitolo() {
        return titolo;
    }
    public String getGenere() {
        return genere;
    }
    public String getAutore() {
        return autore;
    }
    public boolean isInprestito() {
        return inprestito;
    }
    public ArrayList<RichiestaPrestito> getLibriPrenotati() {
        return libriPrenotati;
    }
    public void setInprestito(boolean inprestito) {
        this.inprestito = inprestito;
    }
    public static void setIdlibroattuale(int idlibroattuale) {
        Libro.idlibroattuale = idlibroattuale;
    }
    public  void creaPrenotazioneLibro(Cliente cliente){
        RichiestaPrestito pre=new RichiestaPrestito(cliente,this, new Date());
        this.aggiungiPrenotazione(pre);
        cliente.aggiungiPrenotazione(pre);
        System.out.println("\n Il libro "+this.titolo+" è stato prenotato da "+ cliente.getNome());
    }
    public void creaPrenotazione(Cliente cliente){
        boolean creaRichiesta=true;
        int i;
        for (i=0;i<cliente.getLibriPrenotati().size();i++){
            if(cliente.getLibriPrenotati().get(i).getLibro()==this){
                System.out.println("Hai gia "+this.getTitolo() + " in prestito");
                return;
            }
        }
        for(i=0;i<this.getLibriPrenotati().size();i++){
            if(this.libriPrenotati.get(i).getCliente()==cliente){
                creaRichiesta=false;
                break;
            }
        }
        if(creaRichiesta){
            this.creaPrenotazioneLibro(cliente);
        }else{
            System.out.println("\n Hai gia una richiesta di prenotazione per "+this.getTitolo());
        }
    }
    public void prenotazioneDipendenti(RichiestaPrestito richiestaPrestito){
        this.rimuoviPrenotazione();
        richiestaPrestito.getCliente().rimuoviPrenotazione(richiestaPrestito);
    }
    public void libroInPrestito(Cliente cliente,Impiegati impiegati){
        ArrayList<RichiestaPrestito> prestito=this.libriPrenotati;
        Date today= new Date();
        for(int i=0;i<prestito.size();i++){
           RichiestaPrestito pren=prestito.get(i);
            long giorni = ChronoUnit.DAYS.between(today.toInstant(), pren.getDataRichiesta().toInstant());
            giorni = -giorni;
            if(giorni>(long)Libreria.getInstance().getScadenza_prenotazioni()){
                this.rimuoviPrenotazione();
                pren.getCliente().rimuoviPrenotazione(pren);
            }

        }
        if(this.inprestito) {
            System.out.println(this.titolo + " è già in prestito.");
            System.out.println("Vuoi prenotare " + this.titolo);
            Scanner scan = new Scanner(System.in);
            String input = scan.next();
            if (input.equals("y")) {
                this.creaPrenotazione(cliente);
            }

        }else{
            if (!this.libriPrenotati.isEmpty()) {
                boolean haPrenotazioni=false;
                for(int i=0;i<this.libriPrenotati.size() && !haPrenotazioni;++i){
                    if(this.libriPrenotati.get(i).getCliente()==cliente){
                        haPrenotazioni=true;
                    }
                }
                if(!haPrenotazioni){
                    System.out.println("\nUn'altro cliente ha gia prenotato questo libro, quindi al momento non è disponibile.");
                    System.out.println("Vuoi creare una prenotazione?");
                    Scanner scan = new Scanner(System.in);
                    String input = scan.next();
                    if(input.equals("y")){
                        this.creaPrenotazione(cliente);
                    }
                    return;
                }
                if(this.libriPrenotati.get(0).getCliente()!=cliente){
                    System.out.println("\nUn'altro utente ha richiesto questo libro prima di te, dovrai aspettare");
                    return;
                }
                this.prenotazioneDipendenti(this.libriPrenotati.get(0));
            }
            this.setInprestito(true);
            Prestito Storico =new Prestito(cliente,this,impiegati,null,new Date(),null,false);
            Libreria.getInstance().aggiungiPrestito(Storico);
            cliente.aggiungiLibroInPrestito(Storico);
            System.out.println("\nHai preso in prestito con successo "+this.titolo +"di "+this.autore);
            System.out.println("Prestito creato da "+impiegati.getNome());
        }

        }public void restituzioneLibro(Cliente cliente,Prestito prestito,Impiegati impiegati){
        prestito.getLibro().setInprestito(false);
        prestito.setDataFinePrestito(new Date());
        prestito.setRicevente(impiegati);
        cliente.rimuoviLibroInPrestito(prestito);
        prestito.pagaMulta();
        long giorni = ChronoUnit.DAYS.between(prestito.getDataInizioPrestito().toInstant(), prestito.getDataFinePrestito().toInstant());
        giorni = 0L - giorni;
        System.out.println("\n Il libro "+prestito.getLibro().getTitolo()+"è stato restituito dopo " + giorni+" da "+cliente.getNome());
        System.out.println("Ricevuto da "+ impiegati.getNome());
        }
}

