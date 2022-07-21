package Persone;


public class Cassiere extends Impiegati {
    int numeroScrivania;
    public static int numeroDiScrivaniaAttuale=0;

    public Cassiere(int id, String nome, String indirizzo, long numerotelefono, double salario,int numeroScrivania) {
        super(id, nome, indirizzo, numerotelefono, salario);
        if( numeroScrivania ==-1) {
            this.numeroScrivania = numeroDiScrivaniaAttuale;
        }else{
            this.numeroScrivania=numeroScrivania;
        }
        ++numeroDiScrivaniaAttuale;
    }
    public void stampaInfo(){
        super.stampaInfo();
        System.out.println("Numero Scrivania: " +this.numeroScrivania);
    }
    public int getNumeroScrivania() {
        return numeroScrivania;
    }
}
