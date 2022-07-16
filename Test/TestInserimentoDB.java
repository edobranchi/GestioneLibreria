import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;


import java.sql.*;

public class TestInserimentoDB {
    String host = "jdbc:postgresql://[::1]:5432/LIBRERIA?allowMultiQueries=true";  //password edo
    String uName = "postgres";
    String uPass = "edo";

    @Test
    @BeforeAll
    static void PurgeDB() {

        try {
            String host = "jdbc:postgresql://[::1]:5432/LIBRERIA?allowMultiQueries=true";  //password edo
            String uName = "postgres";
            String uPass = "edo";
            Connection con = DriverManager.getConnection(host, uName, uPass);
            Statement stmt = con.createStatement();
            String SQL = "DROP SCHEMA public CASCADE;";
            stmt.addBatch(SQL);
            SQL = "CREATE SCHEMA public;";
            stmt.addBatch(SQL);
            stmt.executeBatch();
            stmt = con.createStatement();


            String CreateSql = "Create Table Libro(id_libro int primary key, titolo varchar not null, autore varchar not null, genere varchar not null ,in_prestito boolean not null) ";
            stmt.addBatch(CreateSql);
            String CreateSql2 = "Create Table Prestito(id_prestito int primary key, libro integer not null, prestante integer not null,ricevente integer, cliente integer not null, data_inizio_prestito timestamp not null,data_fine_prestito timestamp,multa_pagata boolean not null) ";
            stmt.addBatch(CreateSql2);
            String CreateSql3 = "Create Table Libraio(id_libraio int primary key, numero_scrivania integer not null) ";
            stmt.addBatch(CreateSql3);
            String CreateSql4 = "Create Table Impiegato(id_impiegato int primary key, tipo varchar not null, stipendio double precision) ";
            stmt.addBatch(CreateSql4);
            String CreateSql5 = "Create Table Cliente(id_cliente int primary key) ";
            stmt.addBatch(CreateSql5);
            String CreateSql6 = "Create Table Cassiere(id_cassiere int primary key,numero_scrivania integer not null) ";
            stmt.addBatch(CreateSql6);
            String CreateSql7 = "Create Table Persona(id_persona int primary key, nome_persona varchar not null,password varchar not null,indirizzo varchar not null,numero_telefono integer not null) ";
            stmt.addBatch(CreateSql7);
            String CreateSql8 = "Create Table Libro_prenotato(id_prenotazione int primary key, libro integer not null, cliente integer not null,data_prenotazione timestamp) ";
            stmt.addBatch(CreateSql8);
            String CreateSql9 = "Create Table Libro_in_prestito(id_libro int primary key, cliente integer not null) ";
            stmt.addBatch(CreateSql9);


            String CreateSql10 = "ALTER TABLE Prestito ADD CONSTRAINT fklibro FOREIGN KEY (libro) REFERENCES Libro (id_libro); ";
            stmt.addBatch(CreateSql10);
            String CreateSql11 = "ALTER TABLE Prestito ADD CONSTRAINT fk_prestante FOREIGN KEY (prestante) REFERENCES Impiegato (id_impiegato); ";
            stmt.addBatch(CreateSql11);
            String CreateSql12 = "ALTER TABLE Prestito ADD CONSTRAINT fk_ricevente FOREIGN KEY (ricevente) REFERENCES Impiegato (id_impiegato); ";
            stmt.addBatch(CreateSql12);
            String CreateSql13 = "ALTER TABLE Prestito ADD CONSTRAINT fk_cliente FOREIGN KEY (cliente) REFERENCES Cliente (id_cliente); ";
            stmt.addBatch(CreateSql13);
            String CreateSql14 = "ALTER TABLE Libraio ADD CONSTRAINT fkid_libraio FOREIGN KEY (id_libraio) REFERENCES Impiegato (id_impiegato); ";
            stmt.addBatch(CreateSql14);
            String CreateSql15 = "ALTER TABLE Cassiere ADD CONSTRAINT fkid_cassiere FOREIGN KEY (id_cassiere) REFERENCES Impiegato (id_impiegato); ";
            stmt.addBatch(CreateSql15);
            String CreateSql16 = "ALTER TABLE Impiegato ADD CONSTRAINT fk_impiegato FOREIGN KEY (id_impiegato) REFERENCES Persona (id_persona); ";
            stmt.addBatch(CreateSql16);
            String CreateSql17 = "ALTER TABLE Cliente ADD CONSTRAINT fk_cliente FOREIGN KEY (id_cliente) REFERENCES Persona (id_persona); ";
            stmt.addBatch(CreateSql17);
            String CreateSql18 = "ALTER TABLE Libro_prenotato ADD CONSTRAINT fk_cliente FOREIGN KEY (cliente) REFERENCES Cliente (id_cliente); ";
            stmt.addBatch(CreateSql18);
            String CreateSql19 = "ALTER TABLE Libro_in_prestito ADD CONSTRAINT fk_cliente FOREIGN KEY (cliente) REFERENCES Cliente (id_cliente); ";
            stmt.addBatch(CreateSql19);

            stmt.executeBatch();

        } catch (SQLException var5) {
            System.out.println(var5.getMessage());

        }
    }



    @Test
    @Order(1)
    void testInserimentoLetturaLibro() throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.host, this.uName, this.uPass);
        stmt = con.createStatement();

        String SQL = "INSERT INTO public.libro VALUES ('1','terra','grisham','thriller','false')";
        stmt.executeUpdate(SQL);

        SQL =" SELECT * FROM public.libro";
        ResultSet rs= stmt.executeQuery(SQL);
        rs.next();
        assertNotNull("Ci sono libri nel database",rs);
        assertEquals("1",rs.getString(1));
        assertEquals("terra",rs.getString(2));
        assertEquals("grisham",rs.getString(3));
        assertEquals("thriller",rs.getString(4));
        assertEquals("f",rs.getString(5));



        SQL ="DELETE FROM public.libro WHERE id_libro=1;";
        stmt.executeUpdate(SQL);
    }

    @Test
    @Order(2)
    void testInserimentoLetturaCliente() throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.host, this.uName, this.uPass);
        stmt = con.createStatement();
        String SQL = "INSERT INTO persona VALUES ('1','edo','ciao','via breddo 13','2896007')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO cliente VALUES ('1')";
        stmt.executeUpdate(SQL);

        SQL =" SELECT * FROM cliente";
        ResultSet rs= stmt.executeQuery(SQL);
        rs.next();
        assertNotNull("Ci sono clienti",rs);
        assertEquals("1",rs.getString(1));

        SQL ="DELETE FROM cliente WHERE id_cliente=1;";
        stmt.executeUpdate(SQL);
        SQL = "DELETE FROM Persona WHERE id_persona=1";
        stmt.executeUpdate(SQL);

    }

    @Test
    @Order(3)
    void testInserimentoLetturaCassiere() throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.host, this.uName, this.uPass);
        stmt = con.createStatement();
        String SQL = "INSERT INTO persona VALUES ('1','edo','ciao','via breddo 13','2896007')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO Impiegato VALUES ('1','cassiere','2500')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO cassiere VALUES ('1','26')";
        stmt.executeUpdate(SQL);

        SQL =" SELECT * FROM cassiere";
        ResultSet rs= stmt.executeQuery(SQL);
        rs.next();
        assertNotNull("Ci sono cassieri nel database",rs);
        assertEquals("1",rs.getString(1));
        assertEquals("26",rs.getString(2));

        SQL ="DELETE FROM Cassiere WHERE id_cassiere=1;";
        stmt.executeUpdate(SQL);
        SQL = "DELETE FROM Impiegato WHERE id_impiegato=1";
        stmt.executeUpdate(SQL);
        SQL = "DELETE FROM Persona WHERE id_persona=1";
        stmt.executeUpdate(SQL);
    }

    @Test
    @Order(4)
    void testInserimentoLetturaImpiegato() throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.host, this.uName, this.uPass);
        stmt = con.createStatement();

        String SQL = "INSERT INTO persona VALUES ('1','edo','ciao','via breddo 13','2896007')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO Impiegato VALUES ('1','cassiere','2500')";
        stmt.executeUpdate(SQL);

        SQL =" SELECT * FROM Impiegato";
        ResultSet rs= stmt.executeQuery(SQL);
        rs.next();
        assertNotNull("Ci sono libri nel database",rs);
        assertEquals("1",rs.getString(1));
        assertEquals("cassiere",rs.getString(2));
        assertEquals("2500",rs.getString(3));




        SQL ="DELETE FROM Impiegato WHERE id_impiegato=1;";
        stmt.executeUpdate(SQL);
        SQL = "DELETE FROM Persona WHERE id_persona=1";
        stmt.executeUpdate(SQL);
    }

    @Test
    @Order(5)
    void testInserimentoLetturaLibraio() throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.host, this.uName, this.uPass);
        stmt = con.createStatement();
        String SQL = "INSERT INTO persona VALUES ('1','edo','ciao','via breddo 13','2896007')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO Impiegato VALUES ('1','cassiere','2500')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO libraio VALUES ('1','54')";
        stmt.executeUpdate(SQL);

        SQL =" SELECT * FROM libraio";
        ResultSet rs= stmt.executeQuery(SQL);
        rs.next();
        assertNotNull("Ci sono librai nel database",rs);
        assertEquals("1",rs.getString(1));
        assertEquals("54",rs.getString(2));


        SQL ="DELETE FROM libraio WHERE id_libraio=1;";
        stmt.executeUpdate(SQL);
        SQL ="DELETE FROM Impiegato WHERE id_impiegato=1;";
        stmt.executeUpdate(SQL);
        SQL = "DELETE FROM Persona WHERE id_persona=1";
        stmt.executeUpdate(SQL);
    }

    @Test
    @Order(6)
    void testInserimentoLetturaLibroInPrestito() throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.host, this.uName, this.uPass);
        stmt = con.createStatement();
        String SQL = "INSERT INTO persona VALUES ('24','edo','ciao','via breddo 13','2896007')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO cliente VALUES ('24')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO libro_in_prestito VALUES ('1','24')";
        stmt.executeUpdate(SQL);

        SQL =" SELECT * FROM libro_in_prestito";
        ResultSet rs= stmt.executeQuery(SQL);
        rs.next();
        assertNotNull("Ci sono libri nel database",rs);
        assertEquals("1",rs.getString(1));
        assertEquals("24",rs.getString(2));


        SQL ="DELETE FROM libro_in_prestito WHERE id_libro=1;";
        stmt.executeUpdate(SQL);
        SQL ="DELETE FROM cliente WHERE id_cliente=24;";
        stmt.executeUpdate(SQL);
        SQL = "DELETE FROM Persona WHERE id_persona=24";
        stmt.executeUpdate(SQL);
    }

    @Test
    @Order(7)
    void testInserimentoLetturaLibroPrenotato() throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.host, this.uName, this.uPass);
        stmt = con.createStatement();
        String SQL = "INSERT INTO persona VALUES ('54','edo','ciao','via breddo 13','2896007')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO cliente VALUES ('54')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO libro_prenotato VALUES ('1','23','54','1970-01-01 00:00:01')";
        stmt.executeUpdate(SQL);

        SQL =" SELECT * FROM libro_prenotato";
        ResultSet rs= stmt.executeQuery(SQL);
        rs.next();
        assertNotNull("Ci sono libri nel database",rs);
        assertEquals("1",rs.getString(1));
        assertEquals("23",rs.getString(2));
        assertEquals("54",rs.getString(3));
        assertEquals("1970-01-01 00:00:01",rs.getString(4));



        SQL ="DELETE FROM libro_prenotato WHERE id_prenotazione=1;";
        stmt.executeUpdate(SQL);
        SQL ="DELETE FROM cliente WHERE id_cliente=54;";
        stmt.executeUpdate(SQL);
        SQL = "DELETE FROM Persona WHERE id_persona=54";
        stmt.executeUpdate(SQL);
    }

    @Test
    @Order(8)
    void testInserimentoLetturaPersona() throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.host, this.uName, this.uPass);
        stmt = con.createStatement();

        String SQL = "INSERT INTO persona VALUES ('1','edo','ciao','via breddo 13','2896007')";
        stmt.executeUpdate(SQL);

        SQL =" SELECT * FROM persona";
        ResultSet rs= stmt.executeQuery(SQL);
        rs.next();
        assertNotNull("Ci sono persone nel database",rs);
        assertEquals("1",rs.getString(1));
        assertEquals("edo",rs.getString(2));
        assertEquals("ciao",rs.getString(3));
        assertEquals("via breddo 13",rs.getString(4));
        assertEquals("2896007",rs.getString(5));




        SQL ="DELETE FROM persona WHERE id_persona=1;";
        stmt.executeUpdate(SQL);
    }

    @Test
    @Order(9)
    void testInserimentoLetturaPrestito() throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.host, this.uName, this.uPass);
        stmt = con.createStatement();
        String SQL = "INSERT INTO persona VALUES ('7','edo','ciao','via breddo 13','2896007')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO persona VALUES ('54','edo','ciao','via breddo 13','2896007')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO cliente VALUES ('54')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO Impiegato VALUES ('7','cassiere','2500')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO libro VALUES ('5','terra','grisham','thriller','false')";
        stmt.executeUpdate(SQL);
        SQL = "INSERT INTO prestito VALUES ('1','5','7','7','54','1970-01-01 00:00:01','1970-01-01 00:00:01','f')";
        stmt.executeUpdate(SQL);


        SQL =" SELECT * FROM prestito";
        ResultSet rs= stmt.executeQuery(SQL);
        rs.next();
        assertNotNull("Ci sono persone nel database",rs);
        assertEquals("1",rs.getString(1));
        assertEquals("5",rs.getString(2));
        assertEquals("7",rs.getString(3));
        assertEquals("7",rs.getString(4));
        assertEquals("54",rs.getString(5));
        assertEquals("1970-01-01 00:00:01",rs.getString(6));
        assertEquals("1970-01-01 00:00:01",rs.getString(7));
        assertEquals("f",rs.getString(8));



        SQL ="DELETE FROM prestito WHERE id_prestito=1;";
        stmt.executeUpdate(SQL);
        SQL ="DELETE FROM libro WHERE id_libro=5;";
        stmt.executeUpdate(SQL);
        SQL ="DELETE FROM Impiegato WHERE id_impiegato=7;";
        stmt.executeUpdate(SQL);
        SQL ="DELETE FROM Cliente WHERE id_cliente=54;";
        stmt.executeUpdate(SQL);
        SQL ="DELETE FROM persona WHERE id_persona=7;";
        stmt.executeUpdate(SQL);
        SQL ="DELETE FROM persona WHERE id_persona=54;";
        stmt.executeUpdate(SQL);
    }
}
