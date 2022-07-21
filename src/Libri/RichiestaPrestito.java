package Libri;
import Persone.Cliente;
import java.util.Date;

public class RichiestaPrestito {
    Cliente cliente;
    Libro libro;
    Date dataRichiesta;

    public RichiestaPrestito(Cliente cliente,Libro libro,Date dataRichiesta){
        this.cliente=cliente;
        this.libro=libro;
        this.dataRichiesta=dataRichiesta;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public Libro getLibro() {
        return libro;
    }
    public Date getDataRichiesta() {
        return dataRichiesta;
    }
    public void stampa(){
        System.out.println(this.libro.getTitolo()+"  "+this.cliente.getNome()+"  "+this.dataRichiesta);
    }
}
