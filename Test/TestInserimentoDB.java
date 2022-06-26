import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;


import java.sql.*;

public class TestInserimentoDB {
    String host = "jdbc:postgresql://[::1]:5432/LIBRERIA?allowMultiQueries=true";  //password edo
    String uName = "postgres";
    String uPass = "edo";

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
