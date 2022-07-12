package Libri;

import Persone.Cliente;
import Persone.Impiegati;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Scanner;

public class Prestito {
    private final Cliente cliente;
    private final Libro libro;
    private final Impiegati prestante;
    private Impiegati ricevente;
    private Date dataInizioPrestito;
    private Date dataFinePrestito;
    private boolean multaPagata;

    public Prestito(Cliente cliente, Libro libro, Impiegati prestante,Impiegati ricevente,Date dataInizioPrestito,Date dataFinePrestito, boolean multaPagata){
        this.cliente=cliente;
        this.libro=libro;
        this.prestante=prestante;
        this.ricevente=ricevente;
        this.dataInizioPrestito=dataInizioPrestito;
        this.dataFinePrestito=dataFinePrestito;
        this.multaPagata=multaPagata;
    }

    public Libro getLibro() {
        return libro;
    }
    public Impiegati getPrestante() {
        return prestante;
    }
    public Impiegati getRicevente() {
        return ricevente;
    }
    public void setRicevente(Impiegati ricevente) {
        this.ricevente = ricevente;
    }
    public Date getDataInizioPrestito() {
        return dataInizioPrestito;
    }
    public Date getDataFinePrestito() {
        return dataFinePrestito;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setDataFinePrestito(Date dataFinePrestito) {
        this.dataFinePrestito = dataFinePrestito;
    }
    public boolean isMultaPagata() {
        return multaPagata;
    }

    public void setMultaPagata(boolean multaPagata) {
        this.multaPagata = multaPagata;
    }
     public double calcolaMulta(){
        double multa=0;
        if (!this.multaPagata){
            Date inizio=this.dataInizioPrestito;
            Date fine=this.dataFinePrestito;
            if(fine!=null){
                long giorniritardo = ChronoUnit.DAYS.between(fine.toInstant(), inizio.toInstant());
                giorniritardo =-giorniritardo;
                giorniritardo -= Libreria.getInstance().scadenza_prestiti;
                if (giorniritardo > 0L) {
                multa = (double)giorniritardo * Libreria.getInstance().multa_al_giorno;
                }else{
                    multa=0.0;
                }
            } else {
                multa = 0.0;
            }
        }
        return multa;
     }
     public void pagaMulta(){
        double multaTotale=this.calcolaMulta();
        if(multaTotale>0) {
            System.out.println("Vuoi pagarla?(SI(s)/NO(n)");
            Scanner input = new Scanner(System.in);
            String scelta = input.next();
            if (scelta.equals("s")) {
                this.setMultaPagata(true);
                System.out.println("Multa pagata con Successo.");
            }
            if (scelta.equals("n")) {
                this.setMultaPagata(false);
            }
        }
     }
     public void rinnovaPrestito(Date dataInizioPrestito){
        this.dataInizioPrestito=dataInizioPrestito;
        System.out.println("La data di restituzione per" + this.getLibro().getTitolo() +"è stata spostata");
     }
}
