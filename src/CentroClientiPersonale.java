import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CentroClientiPersonale {
    private Libraio libraio = null;

    private static final ArrayList<Persona> persone = new ArrayList();
    private static CentroClientiPersonale ccp;

    public static CentroClientiPersonale getInstance() {
        if (ccp == null) {
            ccp = new CentroClientiPersonale();
        }
        return ccp;
    }


    public Libraio getLibraio() {
        return libraio;
    }

    public static ArrayList<Persona> getPersone() {
        return persone;
    }
    public boolean aggiungiLibraio(Libraio libraio) {
        if (this.libraio == null) {
            this.libraio = libraio;
            persone.add(this.libraio);
            System.out.println("Libraio aggiunto con successo");
            return true;
        } else {
            System.out.println("Impossibile aggiungere libraio perchè esiste già");
            return false;
        }
    }
    public void aggiungiCassiere(Cassiere cassiere) {
        persone.add(cassiere);
    }

    public void aggiungiCliente(Cliente cliente) {
        persone.add(cliente);
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
        for (int i = 0; i < persone.size(); ++i) {
            if (( persone.get(i)).getId() == idcliente && ( persone.get(i)).getClass().getSimpleName().equals("Cliente")) {
                return (Cliente) persone.get(i);
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
        for (int i = 0; i < persone.size(); ++i) {
            if (persone.get(i).getId() == idcassiere && persone.get(i).getClass().getSimpleName().equals("Cassiere")) {
                return (Cassiere) persone.get(i);
            }
        }

        System.out.println("\nNon esiste nessun cassiere con quell'ID.");
        return null;
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

        for (int i = 0; i < persone.size(); ++i) {
            if (persone.get(i).getId() == id && persone.get(i).getPassword().equals(password)) {
                System.out.println("\nBenvenuto " + persone.get(i).getNome());
                return persone.get(i);
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
}
