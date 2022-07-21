package Persone;

public class Libraio extends Impiegati{
    int numeroUfficio;
    public static int numeroUfficioAttuale=0;

    public Libraio(int id, String nome, String indirizzo, long numerotelefono, double salario,int numeroUfficio) {
        super(id, nome, indirizzo, numerotelefono, salario);
        if (numeroUfficio == -1) {
            this.numeroUfficio = numeroUfficioAttuale;
        } else {
            this.numeroUfficio = numeroUfficio;
        }
        ++numeroUfficioAttuale;
    }
    public void stampaInfo(){
        super.stampaInfo();
        System.out.println("Numero Ufficio: "+ this.numeroUfficio);
    }
    public int getNumeroUfficio() {
        return numeroUfficio;
    }
}
