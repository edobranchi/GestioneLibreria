public class Impiegati extends Persona{
    protected double salario;
    public Impiegati(int id,String nome,String indirizzo,long numerotelefono,double salario){
        super(id,nome,indirizzo,numerotelefono);
        this.salario=salario;

    }
    public void stampaInfo(){
        super.stampaInfo();
        System.out.println("Stipendio: "+ this.salario);
    }

    public double getSalario() {
        return salario;
    }
}
