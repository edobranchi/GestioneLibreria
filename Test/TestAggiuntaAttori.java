import Libri.Libreria;
import Libri.Libro;
import Libri.Prestito;
import Libri.RichiestaPrestito;
import Persone.Cassiere;
import Persone.CentroClientiPersonale;
import Persone.Cliente;
import Persone.Libraio;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;


public class TestAggiuntaAttori {
    @BeforeAll
    static void purgeDB(){

        try {
            String host = "jdbc:postgresql://[::1]:5432/LIBRERIA?allowMultiQueries=true";  //password edo
            String uName = "postgres";
            String uPass = "edo";
            Connection con = DriverManager.getConnection(host, uName, uPass);
            Statement stmt = con.createStatement();
            String SQL = "DROP SCHEMA public CASCADE;";
            stmt.addBatch(SQL);
            SQL="CREATE SCHEMA public;";
            stmt.addBatch(SQL);
            stmt.executeBatch();
            stmt = con.createStatement();


            String CreateSql = "Create Table Libri.Libro(id_libro int primary key, titolo varchar not null, autore varchar not null, genere varchar not null ,in_prestito boolean not null) ";
            stmt.addBatch(CreateSql);
            String CreateSql2 = "Create Table Libri.Prestito(id_prestito int primary key, libro integer not null, prestante integer not null,ricevente integer, cliente integer not null, data_inizio_prestito timestamp not null,data_fine_prestito timestamp,multa_pagata boolean not null) ";
            stmt.addBatch(CreateSql2);
            String CreateSql3 = "Create Table Persone.Libraio(id_libraio int primary key, numero_scrivania integer not null) ";
            stmt.addBatch(CreateSql3);
            String CreateSql4 = "Create Table Impiegato(id_impiegato int primary key, tipo varchar not null, stipendio double precision) ";
            stmt.addBatch(CreateSql4);
            String CreateSql5 = "Create Table Persone.Cliente(id_cliente int primary key) ";
            stmt.addBatch(CreateSql5);
            String CreateSql6 = "Create Table Persone.Cassiere(id_cassiere int primary key,numero_scrivania integer not null) ";
            stmt.addBatch(CreateSql6);
            String CreateSql7 = "Create Table Persone.Persona(id_persona int primary key, nome_persona varchar not null,password varchar not null,indirizzo varchar not null,numero_telefono integer not null) ";
            stmt.addBatch(CreateSql7);
            String CreateSql8 = "Create Table Libro_prenotato(id_prenotazione int primary key, libro integer not null, cliente integer not null,data_prenotazione timestamp) ";
            stmt.addBatch(CreateSql8);
            String CreateSql9 = "Create Table Libro_in_prestito(id_libro int primary key, cliente integer not null) ";
            stmt.addBatch(CreateSql9);


            String CreateSql10 = "ALTER TABLE Libri.Prestito ADD CONSTRAINT fklibro FOREIGN KEY (libro) REFERENCES Libri.Libro (id_libro); ";
            stmt.addBatch(CreateSql10);
            String CreateSql11 = "ALTER TABLE Libri.Prestito ADD CONSTRAINT fk_prestante FOREIGN KEY (prestante) REFERENCES Impiegato (id_impiegato); ";
            stmt.addBatch(CreateSql11);
            String CreateSql12 = "ALTER TABLE Libri.Prestito ADD CONSTRAINT fk_ricevente FOREIGN KEY (ricevente) REFERENCES Impiegato (id_impiegato); ";
            stmt.addBatch(CreateSql12);
            String CreateSql13 = "ALTER TABLE Libri.Prestito ADD CONSTRAINT fk_cliente FOREIGN KEY (cliente) REFERENCES Persone.Cliente (id_cliente); ";
            stmt.addBatch(CreateSql13);
            String CreateSql14 = "ALTER TABLE Persone.Libraio ADD CONSTRAINT fkid_libraio FOREIGN KEY (id_libraio) REFERENCES Impiegato (id_impiegato); ";
            stmt.addBatch(CreateSql14);
            String CreateSql15 = "ALTER TABLE Persone.Cassiere ADD CONSTRAINT fkid_cassiere FOREIGN KEY (id_cassiere) REFERENCES Impiegato (id_impiegato); ";
            stmt.addBatch(CreateSql15);
            String CreateSql16 = "ALTER TABLE Impiegato ADD CONSTRAINT fk_impiegato FOREIGN KEY (id_impiegato) REFERENCES Persone.Persona (id_persona); ";
            stmt.addBatch(CreateSql16);
            String CreateSql17 = "ALTER TABLE Persone.Cliente ADD CONSTRAINT fk_cliente FOREIGN KEY (id_cliente) REFERENCES Persone.Persona (id_persona); ";
            stmt.addBatch(CreateSql17);
            String CreateSql18 = "ALTER TABLE Libro_prenotato ADD CONSTRAINT fk_cliente FOREIGN KEY (cliente) REFERENCES Persone.Cliente (id_cliente); ";
            stmt.addBatch(CreateSql18);
            String CreateSql19 = "ALTER TABLE Libro_in_prestito ADD CONSTRAINT fk_clente FOREIGN KEY (cliente) REFERENCES Persone.Cliente (id_cliente); ";
            stmt.addBatch(CreateSql19);

            stmt.executeBatch();

        } catch (SQLException var5) {
            System.out.println(var5.getMessage());

        }
    }

    @Test
    void TestAggiuntaCliente() {

        Cliente testcliente=new Cliente(1,"edo","via breddo",2896007);
        assertEquals(1,testcliente.getId());
        assertEquals("edo",testcliente.getNome());
        assertEquals("via breddo",testcliente.getIndirizzo());
        assertEquals(2896007L,testcliente.getNumeroTelefono());
        CentroClientiPersonale.getInstance().aggiungiCliente(testcliente);
        ArrayList listaclienti=CentroClientiPersonale.getInstance().getPersone();
        assertEquals(testcliente,listaclienti.get(listaclienti.size() -1 ));



    }

    @Test
    void TestAggiuntaCassiere() {

        Cassiere testcassiere=new Cassiere(1,"edo","via breddo",2896007,230,5);
        assertEquals(1,testcassiere.getId());
        assertEquals("edo",testcassiere.getNome());
        assertEquals("via breddo",testcassiere.getIndirizzo());
        assertEquals(2896007L,testcassiere.getNumeroTelefono());
        assertEquals(230.0,testcassiere.getSalario());
        assertEquals(5,testcassiere.getNumeroScrivania());
        CentroClientiPersonale.getInstance().aggiungiCassiere(testcassiere);
        ArrayList listaclienti=CentroClientiPersonale.getInstance().getPersone();
        assertEquals(testcassiere,listaclienti.get(listaclienti.size() -1 ));

    }
    @Test
    void testAggiuntaLibraio(){
        Libraio testlibraio=new Libraio(1,"edo","via breddo",2896007,230,5);
        assertEquals(1,testlibraio.getId());
        assertEquals("edo",testlibraio.getNome());
        assertEquals("via breddo",testlibraio.getIndirizzo());
        assertEquals(2896007L,testlibraio.getNumeroTelefono());
        assertEquals(230.0,testlibraio.getSalario());
        assertEquals(5,testlibraio.getNumeroUfficio());
        CentroClientiPersonale.getInstance().aggiungiLibraio(testlibraio);
        ArrayList listaLibraio=CentroClientiPersonale.getInstance().getPersone();
        assertEquals(testlibraio,listaLibraio.get(listaLibraio.size() -1 ));

    }
    @Test
    void testAggiuntaLibro(){
        Libro testLibro=new Libro(1,"IT", "horror","stephen king",false);
        assertEquals(1,testLibro.getIdlibro());
        assertEquals("IT",testLibro.getTitolo());
        assertEquals("horror", testLibro.getGenere());
        assertEquals("stephen king",testLibro.getAutore());
        assertEquals(false, testLibro.isInprestito());
        Libreria.getInstance().aggiungiLibro(testLibro);
        ArrayList listalibri=Libreria.getInstance().getLibri();
        assertEquals(testLibro,listalibri.get(listalibri.size() -1 ));

    }
    @Test
    void testPrestito(){
        Cliente testcliente=new Cliente(1,"edo","via breddo",2896007);
        Cassiere testcassiere=new Cassiere(1,"edo","via breddo",2896007,230,5);
        Libro testLibro=new Libro(1,"IT", "horror","stephen king",false);
        Libreria.getInstance().aggiungiLibro(testLibro);
        CentroClientiPersonale.getInstance().aggiungiCassiere(testcassiere);         //Aggiunta Attori
        CentroClientiPersonale.getInstance().aggiungiCliente(testcliente);
        Date data=new Date();

        Prestito testprestito=new Prestito(testcliente,testLibro,testcassiere,null,data,null,true);
        Libreria.getInstance().aggiungiPrestito(testprestito);           //creazione prestito
        testLibro.libroInPrestito(testcliente,testcassiere);

        ArrayList listaprestiti=Libreria.getInstance().getPrestiti();
        assertEquals(testprestito,listaprestiti.get(0));

        ArrayList listalibri= Libreria.getInstance().getLibri();
        assertEquals(true,((Libro)listalibri.get(0)).isInprestito());

        ArrayList listaprestiticliente=testcliente.getLibriInPrestito();
        assertEquals("stephen king",((Prestito)listaprestiticliente.get(0)).getLibro().getAutore());

    }

    @Test
    void testPrenotazione(){
        Cliente testcliente=new Cliente(1,"edo","via breddo",2896007);
        Libro testLibro=new Libro(1,"IT", "horror","stephen king",false);
        Libreria.getInstance().aggiungiLibro(testLibro);
        CentroClientiPersonale.getInstance().aggiungiCliente(testcliente);

        testLibro.creaPrenotazione(testcliente);

        ArrayList listaprenotazioni= testcliente.getLibriPrenotati();
        assertEquals(((RichiestaPrestito)listaprenotazioni.get(0)).getCliente().getId(),testcliente.getId());
        assertEquals(((RichiestaPrestito)listaprenotazioni.get(0)).getLibro().getIdlibro(),testLibro.getIdlibro());

    }
    @Test
    void testMulta(){
        Libro testLibro=new Libro(1,"IT", "horror","stephen king",false);
        Cliente testcliente=new Cliente(1,"edo","via breddo",2896007);
        Cassiere testcassiere=new Cassiere(1,"edo","via breddo",2896007,230,5);
        Libreria.getInstance().aggiungiLibro(testLibro);
        CentroClientiPersonale.getInstance().aggiungiCassiere(testcassiere);         //Aggiunta Attori
        CentroClientiPersonale.getInstance().aggiungiCliente(testcliente);
        Date data=new Date();

        Prestito testprestito=new Prestito(testcliente,testLibro,testcassiere,null,data,null,true);
        Libreria.getInstance().aggiungiPrestito(testprestito);

        testprestito.setRicevente(testcassiere);
        int mese=data.getMonth();
        Date datafine=new Date();
        datafine.setMonth(mese+2);
        testprestito.setDataFinePrestito(datafine);
        testprestito.setMultaPagata(false);
        double multa= testprestito.calcolaMulta();
        assertFalse(Double.isNaN(multa));



    }

}
