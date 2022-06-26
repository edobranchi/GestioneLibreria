import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;


public class TestAggiuntaAttori {

    @Test
    void TestAggiuntaCliente() {

        Cliente testcliente=new Cliente(1,"edo","via breddo",2896007);
        assertEquals(1,testcliente.getId());
        assertEquals("edo",testcliente.getNome());
        assertEquals("via breddo",testcliente.getIndirizzo());
        assertEquals(2896007L,testcliente.getNumeroTelefono());
        Libreria.getInstance().aggiungiCliente(testcliente);
        ArrayList listaclienti=Libreria.getInstance().getPersone();
        assertEquals(testcliente,listaclienti.get(0));

    }

    @Test
    void TestAggiuntaCassiere() {

        Cassiere testcassiere=new Cassiere(1,"edo","via breddo",2896007,230,5);
        assertEquals(1,testcassiere.getId());
        assertEquals("edo",testcassiere.getNome());
        assertEquals("via breddo",testcassiere.getIndirizzo());
        assertEquals(2896007L,testcassiere.getNumeroTelefono());
        assertEquals(230.0,testcassiere.getSalario());
        assertEquals(5,testcassiere.numeroScrivania);
        Libreria.getInstance().aggiungiCassiere(testcassiere);
        ArrayList listaclienti=Libreria.getInstance().getPersone();
        assertEquals(testcassiere,listaclienti.get(0));

    }
    @Test
    void testAggiuntaLibraio(){
        Libraio testlibraio=new Libraio(1,"edo","via breddo",2896007,230,5);
        assertEquals(1,testlibraio.getId());
        assertEquals("edo",testlibraio.getNome());
        assertEquals("via breddo",testlibraio.getIndirizzo());
        assertEquals(2896007L,testlibraio.getNumeroTelefono());
        assertEquals(230.0,testlibraio.getSalario());
        assertEquals(5,testlibraio.numeroUfficio);
        Libreria.getInstance().aggiungiLibraio(testlibraio);
        ArrayList listaLibraio=Libreria.getInstance().getPersone();
        assertEquals(testlibraio,listaLibraio.get(0));

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
        assertEquals(testLibro,listalibri.get(0));

    }
    @Test
    void testPrestito(){
        Cliente testcliente=new Cliente(1,"edo","via breddo",2896007);
        Cassiere testcassiere=new Cassiere(1,"edo","via breddo",2896007,230,5);
        Libro testLibro=new Libro(1,"IT", "horror","stephen king",false);
        Libreria.getInstance().aggiungiLibro(testLibro);
        Libreria.getInstance().aggiungiCassiere(testcassiere);         //Aggiunta Attori
        Libreria.getInstance().aggiungiCliente(testcliente);
        Date data=new Date();

        Prestito testprestito=new Prestito(testcliente,testLibro,testcassiere,null,data,null,true);
        Libreria.getInstance().aggiungiPrestito(testprestito);           //creazione prestito
        testLibro.libroInPrestito(testcliente,testcassiere);

        ArrayList listaprestiti=Libreria.getInstance().getPrestiti();
        assertEquals(testprestito,listaprestiti.get(0));

        ArrayList listalibri=Libreria.getInstance().getLibri();
        assertEquals(true,((Libro)listalibri.get(0)).isInprestito());

        ArrayList listaprestiticliente=testcliente.getLibriInPrestito();
        assertEquals("stephen king",((Prestito)listaprestiticliente.get(0)).getLibro().getAutore());

    }

    //todo:test prenotazione libro

}
