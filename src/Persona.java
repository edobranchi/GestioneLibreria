public abstract class Persona {
    protected int id;
    protected String password;
    protected String nome;
    protected String indirizzo;
    protected long numeroTelefono;

    static int numeroIdAttuale=0;

    public Persona(int id,String nome,String indirizzo,long numeroTelefono){
        ++numeroIdAttuale;
        if (id==-1){
            this.id=numeroIdAttuale;
        }else {
            this.id=id;
        }
        this.password= Integer.toString((int) ((Math.random() * (100 - 20)) + 20));
        this.nome=nome;
        this.indirizzo=indirizzo;
        this.numeroTelefono=numeroTelefono;
    }
    public void stampaInfo(){
        System.out.println("Le informazioni richieste sono:\n");
        System.out.println("ID: "+this.id);
        System.out.println("Nome: "+this.nome);
        System.out.println("Indirizzo: " +this.indirizzo);
        System.out.println("Numero di telefono: "+this.numeroTelefono);
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public long getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(int numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public static void setNumeroIdAttuale(int numeroIdAttuale) {
        Persona.numeroIdAttuale = numeroIdAttuale;
    }
}
