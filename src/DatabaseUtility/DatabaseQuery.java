package DatabaseUtility;

import Libri.Libreria;
import Libri.Libro;
import Libri.Prestito;
import Libri.RichiestaPrestito;
import Persone.*;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseQuery{
    public Connection makeConnection(){
        try {
            String host = "jdbc:postgresql://[::1]:5432/LIBRERIA?allowMultiQueries=true";  //password edo
            String uName = "postgres";
            String uPass = "edo";
            Connection con = DriverManager.getConnection(host, uName, uPass);
            return con;
        } catch (SQLException var5) {
            System.out.println(var5.getMessage());
            return null;
        }
    }

    //Popolamento Applicativo prima dell'esecuzione
    public void populateLibrary(Connection con, Libreria libreria, CentroClientiPersonale centrocp) throws SQLException, InterruptedException {
        Libreria lib = libreria;
        CentroClientiPersonale ccp=centrocp;
        Statement stmt = con.createStatement();
        DatabaseMetaData dbm = con.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "libro", null);

        //Creazione database nel caso non esistesse
        if (!tables.next()) {
            System.out.println("Il database non esiste, lo creo.");
            CreazioneDatabase init=new CreazioneDatabase(con);
            Thread thread=new Thread(init);
            thread.start();
            thread.join();
        }

        String SQL = "SELECT * FROM Libro";
        ResultSet rs = stmt.executeQuery(SQL);
        int id;
        String name;
        String adrs;
        int rd;
        if (!rs.next()) {
            System.out.println("\nNessun libro trovato in Libreria");
        } else {
            id = 0;

            do {
                if (rs.getString("titolo") != null && rs.getString("autore") != null && rs.getString("genere") != null && rs.getInt("id_libro") != 0) {
                    name = rs.getString("titolo");
                    adrs = rs.getString("autore");
                    String subject = rs.getString("genere");
                    rd = rs.getInt("id_libro");
                    boolean issue = rs.getBoolean("in_prestito");
                    Libro b = new Libro(rd, name, subject, adrs, issue);
                    lib.aggiungiLibro(b);
                    if (id < rd) {
                        id = rd;
                    }
                }
            } while(rs.next());

            Libro.setIdlibroattuale(id);
        }

        SQL = "SELECT id_persona,nome_persona,password,indirizzo,numero_telefono,stipendio,numero_scrivania FROM persona INNER JOIN Cassiere ON id_persona=id_cassiere INNER JOIN Impiegato ON id_impiegato=id_cassiere";
        rs = stmt.executeQuery(SQL);
        int phn;
        double sal;
        int i;
        String psw;
        if (!rs.next()) {
            System.out.println("Nessun cassiere trovato in libreria");
        } else {
            do {
                id = rs.getInt("id_persona");
                name = rs.getString("nome_persona");
                psw=rs.getString("password");
                adrs = rs.getString("indirizzo");
                phn = rs.getInt("numero_telefono");
                sal = rs.getDouble("stipendio");
                i = rs.getInt("numero_scrivania");
                Cassiere c = new Cassiere(id, name,adrs, phn, sal, i);
                c.setPassword(psw);
                ccp.aggiungiCassiere(c);
            } while(rs.next());
        }

        SQL = "SELECT id_persona,nome_persona,password,indirizzo,numero_telefono,stipendio,numero_scrivania FROM persona INNER JOIN Libraio ON id_persona=id_libraio INNER JOIN Impiegato ON id_impiegato=id_libraio";
        rs = stmt.executeQuery(SQL);
        if (!rs.next()) {
            System.out.println("Nessun libraio presente in libreria");
        } else {
            do {
                id = rs.getInt("id_persona");
                name = rs.getString("nome_persona");
                psw=rs.getString("password");
                adrs = rs.getString("indirizzo");
                phn = rs.getInt("numero_telefono");
                sal = rs.getDouble("stipendio");
                i = rs.getInt("numero_scrivania");
                Libraio l = new Libraio(id, name, adrs, phn, sal, i);
                l.setPassword(psw);
                ccp.aggiungiLibraio(l);
            } while(rs.next());
        }

        SQL = "SELECT id_persona,nome_persona,password,indirizzo,password,numero_telefono FROM Persona INNER JOIN Cliente ON id_persona=id_cliente";
        rs = stmt.executeQuery(SQL);
        Cliente bb;
        if (!rs.next()) {
            System.out.println("Nessun cliente trovato in libreria");
        } else {
            do {
                id = rs.getInt("id_persona");
                name = rs.getString("nome_persona");
                psw=rs.getString("password");
                adrs = rs.getString("indirizzo");
                phn = rs.getInt("numero_telefono");
                bb = new Cliente(id, name, adrs, phn);
                bb.setPassword(psw);
                ccp.aggiungiCliente(bb);
            } while(rs.next());
        }

        SQL = "SELECT * FROM Prestito";
        rs = stmt.executeQuery(SQL);
        int bokid;
        if (!rs.next()) {
            System.out.println("Nessun libro in prestito");
        } else {
            do {
                id = rs.getInt("cliente");
                bokid = rs.getInt("libro");
                int pres = rs.getInt("prestante");
                Integer rid = (Integer)rs.getObject("ricevente");
                rd = 0;
                java.util.Date idate = new java.util.Date(rs.getTimestamp("data_inizio_prestito").getTime());
                java.util.Date rdate;
                if (rid != null) {
                    rdate = new java.util.Date(rs.getTimestamp("data_fine_prestito").getTime());
                    rd = rid;
                } else {
                    rdate = null;
                }

                boolean fineStatus = rs.getBoolean("multa_pagata");
                boolean set = true;
                bb = null;

                for(i = 0; i < CentroClientiPersonale.getPersone().size() && set; ++i) {
                    if ((CentroClientiPersonale.getPersone().get(i)).getId() == id) {
                        set = false;
                        bb = (Cliente) CentroClientiPersonale.getPersone().get(i);
                    }
                }

                set = true;
                Impiegati[] s = new Impiegati[2];
                int k;
                if (pres == ccp.getLibraio().getId()) {
                    s[0] = ccp.getLibraio();
                } else {
                    for(k = 0; k < CentroClientiPersonale.getPersone().size() && set; ++k) {
                        if ((CentroClientiPersonale.getPersone().get(k)).getId() == pres && (CentroClientiPersonale.getPersone().get(k)).getClass().getSimpleName().equals("Cassiere")) {
                            set = false;
                            s[0] = (Cassiere) CentroClientiPersonale.getPersone().get(k);
                        }
                    }
                }

                set = true;
                if (rid == null) {
                    s[1] = null;
                    rdate = null;
                } else if (rd == ccp.getLibraio().getId()) {
                    s[1] = ccp.getLibraio();
                } else {
                    for(k = 0; k < CentroClientiPersonale.getPersone().size() && set; ++k) {
                        if ((CentroClientiPersonale.getPersone().get(k)).getId() == rd && (CentroClientiPersonale.getPersone().get(k)).getClass().getSimpleName().equals("Cassiere")) {
                            set = false;
                            s[1] = (Cassiere) CentroClientiPersonale.getPersone().get(k);
                        }
                    }
                }

                set = true;
                ArrayList<Libro> books = lib.getLibri();
                for(k = 0; k < books.size() && set; ++k) {
                    if ((books.get(k)).getIdlibro() == bokid) {
                        set = false;
                        Prestito l = new Prestito(bb, books.get(k), s[0], s[1], idate, rdate, fineStatus);
                        lib.getPrestiti().add(l);
                    }
                }
            } while(rs.next());
        }

        SQL = "SELECT * FROM libro_prenotato";
        rs = stmt.executeQuery(SQL);
        boolean set;
        ArrayList books;
        if (!rs.next()) {
            System.out.println("Nessuna prenotazione");
        } else {
            do {
                id = rs.getInt("cliente");
                bokid = rs.getInt("libro");
                java.util.Date off = new java.util.Date(rs.getDate("data_prenotazione").getTime());
                set = true;
                bb = null;
                books = CentroClientiPersonale.getPersone();

                for(i = 0; i < books.size() && set; ++i) {
                    if (((Persona)books.get(i)).getId() == id) {
                        set = false;
                        bb = (Cliente) books.get(i);
                    }
                }

                set = true;
                books = lib.getLibri();

                for(i = 0; i < books.size() && set; ++i) {
                    if (((Libro)books.get(i)).getIdlibro() == bokid) {
                        set = false;
                        RichiestaPrestito hbook = new RichiestaPrestito(bb, (Libro) books.get(i), off);
                        ((Libro)books.get(i)).aggiungiPrenotazione(hbook);
                        bb.aggiungiPrenotazione(hbook);
                    }
                }
            } while(rs.next());
        }

        SQL = "SELECT id_persona,id_libro FROM persona INNER JOIN Cliente ON id_persona=id_cliente INNER JOIN libro_in_prestito ON id_cliente=cliente ";
        rs = stmt.executeQuery(SQL);
        if (!rs.next()) {
            System.out.println("Nessun cliente ha ancora preso in prestito un libro dalla libreria");
        } else {
            do {
                id = rs.getInt("id_persona");
                bokid = rs.getInt("id_libro");
                bb=null;
                set = true;
                for(i = 0; i < CentroClientiPersonale.getPersone().size() && set; ++i) {
                    if ((CentroClientiPersonale.getPersone().get(i)).getClass().getSimpleName().equals("Cliente") && (CentroClientiPersonale.getPersone().get(i)).getId() == id) {
                        set = false;
                        bb = (Cliente) CentroClientiPersonale.getPersone().get(i);
                    }
                }

                set = true;
                books = lib.getPrestiti();

                for(i = 0; i < books.size() && set; ++i) {
                    if (((Prestito)books.get(i)).getLibro().getIdlibro() == bokid && ((Prestito)books.get(i)).getRicevente() == null) {
                        set = false;
                        Prestito bBook = new Prestito(bb, ((Prestito)books.get(i)).getLibro(), ((Prestito)books.get(i)).getPrestante(), null, ((Prestito)books.get(i)).getDataInizioPrestito(), null, ((Prestito)books.get(i)).isMultaPagata());
                        bb.aggiungiLibroInPrestito(bBook);
                    }
                }
            } while(rs.next());
        }

        ArrayList<Persona> persone = CentroClientiPersonale.getPersone();
        bokid = 0;
        for(i = 0; i < persone.size(); ++i) {
            if (bokid < (persone.get(i)).getId()) {
                bokid = (persone.get(i)).getId();
            }
        }
        Persona.setNumeroIdAttuale(bokid);
    }

   //Riempimento DB dopo l'esecuzione
    public void riempiDB(Connection con, Libreria libreria, CentroClientiPersonale ccp) throws SQLException{
        System.out.println("Salvataggio e chiusura in corso...");
        String template = "DELETE FROM public.prestito";
        PreparedStatement stmts = con.prepareStatement(template);
        stmts.executeUpdate();
        template = "DELETE FROM public.libro_in_prestito";
        stmts = con.prepareStatement(template);
        stmts.executeUpdate();
        template = "DELETE FROM public.libro_prenotato";
        stmts = con.prepareStatement(template);
        stmts.executeUpdate();
        template = "DELETE FROM public.libro";
        stmts = con.prepareStatement(template);
        stmts.executeUpdate();
        template = "DELETE FROM public.Cassiere";
        stmts = con.prepareStatement(template);
        stmts.executeUpdate();
        template = "DELETE FROM public.Libraio";
        stmts = con.prepareStatement(template);
        stmts.executeUpdate();
        template = "DELETE FROM public.cliente";
        stmts = con.prepareStatement(template);
        stmts.executeUpdate();
        template = "DELETE FROM public.impiegato";
        stmts = con.prepareStatement(template);
        stmts.executeUpdate();
        template = "DELETE FROM public.persona";
        stmts = con.prepareStatement(template);
        stmts.executeUpdate();
        Libreria lib = libreria;

        int i;
        PreparedStatement stmt;
        for(i = 0; i < CentroClientiPersonale.getPersone().size(); ++i) {
            template = "INSERT INTO persona (id_persona,nome_persona,password,indirizzo,numero_telefono) values (?,?,?,?,?)";
            stmt = con.prepareStatement(template);
            stmt.setInt(1, (CentroClientiPersonale.getPersone().get(i)).getId());
            stmt.setString(2, (CentroClientiPersonale.getPersone().get(i)).getNome());
            stmt.setString(3, (CentroClientiPersonale.getPersone().get(i)).getPassword());
            stmt.setString(4, (CentroClientiPersonale.getPersone().get(i)).getIndirizzo());
            stmt.setLong(5, (CentroClientiPersonale.getPersone().get(i)).getNumeroTelefono());
            stmt.executeUpdate();
        }

        for(i = 0; i < CentroClientiPersonale.getPersone().size(); ++i) {
            if ((CentroClientiPersonale.getPersone().get(i)).getClass().getSimpleName().equals("Cassiere")) {
                template = "INSERT INTO impiegato (id_impiegato,tipo,stipendio) values (?,?,?)";
                stmt = con.prepareStatement(template);
                stmt.setInt(1, (CentroClientiPersonale.getPersone().get(i)).getId());
                stmt.setString(2, "Cassiere");
                stmt.setDouble(3, ((Cassiere) CentroClientiPersonale.getPersone().get(i)).getSalario());
                stmt.executeUpdate();
                template = "INSERT INTO Cassiere (id_cassiere,numero_scrivania) values (?,?)";
                stmt = con.prepareStatement(template);
                stmt.setInt(1, (CentroClientiPersonale.getPersone().get(i)).getId());
                stmt.setInt(2, ((Cassiere) CentroClientiPersonale.getPersone().get(i)).getNumeroScrivania());
                stmt.executeUpdate();
            }
        }

        if (ccp.getLibraio() != null) {
            template = "INSERT INTO impiegato (id_impiegato,tipo,stipendio) values (?,?,?)";
            stmt = con.prepareStatement(template);
            stmt.setInt(1, ccp.getLibraio().getId());
            stmt.setString(2, "Libraio");
            stmt.setDouble(3, ccp.getLibraio().getSalario());
            stmt.executeUpdate();
            template = "INSERT INTO Libraio (id_libraio,numero_scrivania) values (?,?)";
            stmt = con.prepareStatement(template);
            stmt.setInt(1, ccp.getLibraio().getId());
            stmt.setInt(2, ccp.getLibraio().getNumeroUfficio());
            stmt.executeUpdate();
        }

        for(i = 0; i < CentroClientiPersonale.getPersone().size(); ++i) {
            if ((CentroClientiPersonale.getPersone().get(i)).getClass().getSimpleName().equals("Cliente")) {
                template = "INSERT INTO cliente(id_cliente) values (?)";
                stmt = con.prepareStatement(template);
                stmt.setInt(1, (CentroClientiPersonale.getPersone().get(i)).getId());
                stmt.executeUpdate();
            }
        }

        ArrayList<Libro> books = lib.getLibri();


        for(i = 0; i < books.size(); ++i) {
            template = "INSERT INTO libro (id_libro,titolo,autore,genere,in_prestito) values (?,?,?,?,?)";
            stmt = con.prepareStatement(template);
            stmt.setInt(1, (books.get(i)).getIdlibro());
            stmt.setString(2, (books.get(i)).getTitolo());
            stmt.setString(3, (books.get(i)).getAutore());
            stmt.setString(4, (books.get(i)).getGenere());
            stmt.setBoolean(5, (books.get(i)).isInprestito());
            stmt.executeUpdate();
        }

        for(i = 0; i < lib.getPrestiti().size(); ++i) {
            template = "INSERT INTO prestito(id_prestito,cliente,libro,prestante,data_inizio_prestito,ricevente,data_fine_prestito,multa_pagata) values (?,?,?,?,?,?,?,?)";
            stmt = con.prepareStatement(template);
            stmt.setInt(1, i + 1);
            stmt.setInt(2, (lib.getPrestiti().get(i)).getCliente().getId());
            stmt.setInt(3, (lib.getPrestiti().get(i)).getLibro().getIdlibro());
            stmt.setInt(4, (lib.getPrestiti().get(i)).getPrestante().getId());
            stmt.setTimestamp(5, new Timestamp((lib.getPrestiti().get(i)).getDataInizioPrestito().getTime()));
            stmt.setBoolean(8, (lib.getPrestiti().get(i)).isMultaPagata());
            if ((lib.getPrestiti().get(i)).getRicevente() == null) {
                stmt.setNull(6, 4);
                stmt.setDate(7, null);
            } else {
                stmt.setInt(6, lib.getPrestiti().get(i).getRicevente().getId());
                stmt.setTimestamp(7, new Timestamp(lib.getPrestiti().get(i).getDataFinePrestito().getTime()));
            }
            stmt.executeUpdate();
        }

        int x=1;
        for(i = 0; i < lib.getLibri().size(); ++i) {
            for(int j = 0; j < lib.getLibri().get(i).getLibriPrenotati().size(); ++j) {
                template = "INSERT INTO libro_prenotato(id_prenotazione,libro,cliente,data_prenotazione) values (?,?,?,?)";
                stmt = con.prepareStatement(template);
                stmt.setInt(1, x);
                stmt.setInt(3, lib.getLibri().get(i).getLibriPrenotati().get(j).getCliente().getId());
                stmt.setInt(2, lib.getLibri().get(i).getLibriPrenotati().get(j).getLibro().getIdlibro());
                stmt.setDate(4, new java.sql.Date(lib.getLibri().get(i).getLibriPrenotati().get(j).getDataRichiesta().getTime()));
                stmt.executeUpdate();
                ++x;
            }
        }

        for(i = 0; i < lib.getLibri().size(); ++i) {
            if (lib.getLibri().get(i).isInprestito()) {
                boolean set = true;
                for(int j = 0; j < lib.getPrestiti().size() && set; ++j) {
                    if (lib.getLibri().get(i).getIdlibro() == lib.getPrestiti().get(j).getLibro().getIdlibro() && lib.getPrestiti().get(j).getRicevente() == null) {
                        template = "INSERT INTO libro_in_prestito(id_libro,cliente) values (?,?)";
                        stmt = con.prepareStatement(template);
                        stmt.setInt(1, lib.getPrestiti().get(j).getLibro().getIdlibro());
                        stmt.setInt(2, lib.getPrestiti().get(j).getCliente().getId());
                        stmt.executeUpdate();
                        set = false;
                    }
                }
            }
        }

    }
}

