import java.io.IOException;
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

    public void populateLibrary(Connection con,Libreria libreria) throws SQLException, IOException, InterruptedException {
        Libreria lib = libreria;
        Statement stmt = con.createStatement();
        DatabaseMetaData dbm = con.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "libro", null);

        if (!tables.next()) {
            System.out.println("Il database non esiste, lo creo.");
            CreazioneDatabase init=new CreazioneDatabase(con,lib);
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

        SQL = "SELECT id_persona,nome_persona,indirizzo,password,numero_telefono,stipendio,numero_scrivania FROM persona INNER JOIN Cassiere ON id_persona=id_cassiere INNER JOIN Impiegato ON id_impiegato=id_cassiere";
        rs = stmt.executeQuery(SQL);
        int phn;
        double sal;
        int i;
        if (!rs.next()) {
            System.out.println("Nessun cassiere trovato in libreria");
        } else {
            do {
                id = rs.getInt("id_persona");
                name = rs.getString("nome_persona");
                adrs = rs.getString("indirizzo");
                phn = rs.getInt("numero_telefono");
                sal = rs.getDouble("stipendio");
                i = rs.getInt("numero_scrivania");
                Cassiere c = new Cassiere(id, name, adrs, phn, sal, i);
                lib.aggiungiCassiere(c);
            } while(rs.next());
        }

        SQL = "SELECT id_persona,nome_persona,indirizzo,password,numero_telefono,stipendio,numero_scrivania FROM persona INNER JOIN Libraio ON id_persona=id_libraio INNER JOIN Impiegato ON id_impiegato=id_libraio";
        rs = stmt.executeQuery(SQL);
        if (!rs.next()) {
            System.out.println("Nessun libraio presente in libreria");
        } else {
            do {
                id = rs.getInt("id_libraio");
                name = rs.getString("nome_persona");
                adrs = rs.getString("indirizzo");
                phn = rs.getInt("numero_telefono");
                sal = rs.getDouble("stipendio");
                i = rs.getInt("numero_scrivania");
                Libraio l = new Libraio(id, name, adrs, phn, sal, i);
                lib.aggiungiLibraio(l);
            } while(rs.next());
        }

        SQL = "SELECT id_persona,nome_persona,indirizzo,password,numero_telefono FROM Persona INNER JOIN Cliente ON id_persona=id_cliente";
        rs = stmt.executeQuery(SQL);
        Cliente bb;
        if (!rs.next()) {
            System.out.println("Nessun cliente trovato in libreria");
        } else {
            do {
                id = rs.getInt("id_persona");
                name = rs.getString("nome_persona");
                adrs = rs.getString("indirizzo");
                phn = rs.getInt("numero_telefono");
                bb = new Cliente(id, name, adrs, phn);
                lib.aggiungiCliente(bb);
            } while(rs.next());
        }

        SQL = "SELECT * FROM Prestito";
        rs = stmt.executeQuery(SQL);
        int bokid;
        //int i;
        if (!rs.next()) {
            System.out.println("Nessun libro in prestito");
        } else {
            do {
                id = rs.getInt("cliente");
                bokid = rs.getInt("libro");
                i = rs.getInt("prestante");
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
                //Cliente
                        bb = null;

                for( i = 0; i < lib.getPersone().size() && set; ++i) {
                    if (((Persona)lib.getPersone().get(i)).getId() == id) {
                        set = false;
                        bb = (Cliente)lib.getPersone().get(i);
                    }
                }

                set = true;
                Impiegati[] s = new Impiegati[2];
                int k;
                if (i == lib.getLibraio().getId()) {


                    s[0] = lib.getLibraio();
                } else {
                    for(k = 0; k < lib.getPersone().size() && set; ++k) {
                        if (((Persona)lib.getPersone().get(k)).getId() == i && ((Persona)lib.getPersone().get(k)).getClass().getSimpleName().equals("Cassiere")) {
                            set = false;
                            s[0] = (Cassiere)lib.getPersone().get(k);
                        }
                    }
                }

                set = true;
                if (rid == null) {
                    s[1] = null;
                    rdate = null;
                } else if (rd == lib.getLibraio().getId()) {
                    s[1] = lib.getLibraio();
                } else {
                    for(k = 0; k < lib.getPersone().size() && set; ++k) {
                        if (((Persona)lib.getPersone().get(k)).getId() == rd && ((Persona)lib.getPersone().get(k)).getClass().getSimpleName().equals("Cassiere")) {
                            set = false;
                            s[1] = (Cassiere)lib.getPersone().get(k);
                        }
                    }
                }

                set = true;
                ArrayList<Libro> books = lib.getLibri();

                for(k = 0; k < books.size() && set; ++k) {
                    if (((Libro)books.get(k)).getIdlibro() == bokid) {
                        set = false;
                        Prestito l = new Prestito(bb, (Libro) books.get(k), s[0], s[1], idate, rdate, fineStatus);
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
                books = lib.getPersone();

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
//SELECT id_persona,libro era qui sotto
        SQL = "SELECT id_persona FROM persona INNER JOIN Cliente ON id_persona=id_cliente INNER JOIN libro_in_prestito ON id_cliente=cliente ";
        rs = stmt.executeQuery(SQL);
        if (!rs.next()) {
            System.out.println("Nessun cliente ha ancora preso in prestito un libro dalla libreria");
        } else {
            do {
                id = rs.getInt("id_persona");
                bokid = rs.getInt("libro");
                bb = null;
                set = true;
                boolean okay = true;

                for(i = 0; i < lib.getPersone().size() && set; ++i) {
                    if (((Persona)lib.getPersone().get(i)).getClass().getSimpleName().equals("cliente") && ((Persona)lib.getPersone().get(i)).getId() == id) {
                        set = false;
                        bb = (Cliente) lib.getPersone().get(i);
                    }
                }

                set = true;
                books = lib.getPrestiti();

                for(i = 0; i < books.size() && set; ++i) {
                    if (((Prestito)books.get(i)).getLibro().getIdlibro() == bokid && ((Prestito)books.get(i)).getRicevente() == null) {
                        set = false;
                        Prestito bBook = new Prestito(bb, ((Prestito)books.get(i)).getLibro(), ((Prestito)books.get(i)).getPrestante(), (Impiegati) null, ((Prestito)books.get(i)).getDataInizioPrestito(), (java.util.Date)null, ((Prestito)books.get(i)).isMultaPagata());
                        bb.aggiungiLibroInPrestito(bBook);
                    }
                }
            } while(rs.next());
        }

        ArrayList<Persona> persons = lib.getPersone();
        bokid = 0;

        for(i = 0; i < persons.size(); ++i) {
            if (bokid < ((Persona)persons.get(i)).getId()) {
                bokid = ((Persona)persons.get(i)).getId();
            }
        }

        Persona.setNumeroIdAttuale(bokid);
    }

    public void fillItBack(Connection con,Libreria libreria) throws SQLException, SQLIntegrityConstraintViolationException {
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
        for(i = 0; i < lib.getPersone().size(); ++i) {
            template = "INSERT INTO persona (id_persona,nome_persona,password,indirizzo,numero_telefono) values (?,?,?,?,?)";
            stmt = con.prepareStatement(template);
            stmt.setInt(1, ((Persona)lib.getPersone().get(i)).getId());
            stmt.setString(2, ((Persona)lib.getPersone().get(i)).getNome());
            stmt.setString(3, ((Persona)lib.getPersone().get(i)).getPassword());
            stmt.setString(4, ((Persona)lib.getPersone().get(i)).getIndirizzo());
            stmt.setLong(5, ((Persona)lib.getPersone().get(i)).getNumeroTelefono());
            stmt.executeUpdate();
        }

        for(i = 0; i < lib.getPersone().size(); ++i) {
            if (((Persona)lib.getPersone().get(i)).getClass().getSimpleName().equals("Cassiere")) {
                template = "INSERT INTO impiegato (id_impiegato,tipo,stipendio) values (?,?,?)";
                stmt = con.prepareStatement(template);
                stmt.setInt(1, ((Persona)lib.getPersone().get(i)).getId());
                stmt.setString(2, "Cassiere");
                stmt.setDouble(3, ((Cassiere)lib.getPersone().get(i)).getSalario());
                stmt.executeUpdate();
                template = "INSERT INTO Cassiere (id_cassiere,numero_scrivania) values (?,?)";
                stmt = con.prepareStatement(template);
                stmt.setInt(1, ((Persona)lib.getPersone().get(i)).getId());
                stmt.setInt(2, ((Cassiere)lib.getPersone().get(i)).numeroScrivania);
                stmt.executeUpdate();
            }
        }

        if (lib.getLibraio() != null) {
            template = "INSERT INTO impiegato (id_impiegato,tipo,stipendio) values (?,?,?)";
            stmt = con.prepareStatement(template);
            stmt.setInt(1, lib.getLibraio().getId());
            stmt.setString(2, "Libraio");
            stmt.setDouble(3, lib.getLibraio().getSalario());
            stmt.executeUpdate();
            template = "INSERT INTO Libraio (id_libraio,numero_scrivania) values (?,?)";
            stmt = con.prepareStatement(template);
            stmt.setInt(1, lib.getLibraio().getId());
            stmt.setInt(2, lib.getLibraio().numeroUfficio);
            stmt.executeUpdate();
        }

        for(i = 0; i < lib.getPersone().size(); ++i) {
            if (((Persona)lib.getPersone().get(i)).getClass().getSimpleName().equals("Cliente")) {
                template = "INSERT INTO cliente(id_cliente) values (?)";
                stmt = con.prepareStatement(template);
                stmt.setInt(1, ((Persona)lib.getPersone().get(i)).getId());
                stmt.executeUpdate();
            }
        }

        ArrayList<Libro> books = lib.getLibri();

        //PreparedStatement stmt;
        int x;
        for(x = 0; x < books.size(); ++x) {
            template = "INSERT INTO libro (id_libro,titolo,autore,genere,in_prestito) values (?,?,?,?,?)";
            stmt = con.prepareStatement(template);
            stmt.setInt(1, ((Libro)books.get(x)).getIdlibro());
            stmt.setString(2, ((Libro)books.get(x)).getTitolo());
            stmt.setString(3, ((Libro)books.get(x)).getAutore());
            stmt.setString(4, ((Libro)books.get(x)).getGenere());
            stmt.setBoolean(5, ((Libro)books.get(x)).isInprestito());
            stmt.executeUpdate();
        }

        for(x = 0; x < lib.getPrestiti().size(); ++x) {
            template = "INSERT INTO prestito(id_prestito,cliente,libro,prestante,data_inizio_prestito,ricevente,data_fine_prestito,multa_pagata) values (?,?,?,?,?,?,?,?)";
            stmt = con.prepareStatement(template);
            stmt.setInt(1, x + 1);
            stmt.setInt(2, ((Prestito)lib.getPrestiti().get(x)).getCliente().getId());
            stmt.setInt(3, ((Prestito)lib.getPrestiti().get(x)).getLibro().getIdlibro());
            stmt.setInt(4, ((Prestito)lib.getPrestiti().get(x)).getPrestante().getId());
            stmt.setTimestamp(5, new Timestamp(((Prestito)lib.getPrestiti().get(x)).getDataInizioPrestito().getTime()));
            stmt.setBoolean(8, ((Prestito)lib.getPrestiti().get(x)).isMultaPagata());
            if (((Prestito)lib.getPrestiti().get(x)).getRicevente() == null) {
                stmt.setNull(6, 4);
                stmt.setDate(7, (java.sql.Date)null);
            } else {
                stmt.setInt(6, ((Prestito)lib.getPrestiti().get(x)).getRicevente().getId());
                stmt.setTimestamp(7, new Timestamp(((Prestito)lib.getPrestiti().get(x)).getDataFinePrestito().getTime()));
            }

            stmt.executeUpdate();
        }

        x = 1;

        //int i;
        for(i = 0; i < lib.getLibri().size(); ++i) {
            for(int j = 0; j < ((Libro)lib.getLibri().get(i)).getLibriPrenotati().size(); ++j) {
                template = "INSERT INTO libro_prenotato(id_prenotazione,libro,cliente,data_prenotazione) values (?,?,?,?)";
                stmt = con.prepareStatement(template);
                stmt.setInt(1, x);
                stmt.setInt(3, ((RichiestaPrestito)((Libro)lib.getLibri().get(i)).getLibriPrenotati().get(j)).getCliente().getId());
                stmt.setInt(2, ((RichiestaPrestito)((Libro)lib.getLibri().get(i)).getLibriPrenotati().get(j)).getLibro().getIdlibro());
                stmt.setDate(4, new java.sql.Date(((RichiestaPrestito)((Libro)lib.getLibri().get(i)).getLibriPrenotati().get(j)).getDataRichiesta().getTime()));
                stmt.executeUpdate();
                ++x;
            }
        }

        for(i = 0; i < lib.getLibri().size(); ++i) {
            if (((Libro)lib.getLibri().get(i)).isInprestito()) {
                boolean set = true;

                for(int j = 0; j < lib.getPrestiti().size() && set; ++j) {
                    if (((Libro)lib.getLibri().get(i)).getIdlibro() == ((Prestito)lib.getPrestiti().get(j)).getLibro().getIdlibro() && ((Prestito)lib.getPrestiti().get(j)).getRicevente() == null) {
                        template = "INSERT INTO libro_in_prestito(Libro,cliente) values (?,?)";
                        stmt = con.prepareStatement(template);
                        stmt.setInt(1, ((Prestito)lib.getPrestiti().get(j)).getLibro().getIdlibro());
                        stmt.setInt(2, ((Prestito)lib.getPrestiti().get(j)).getCliente().getId());
                        stmt.executeUpdate();
                        set = false;
                    }
                }
            }
        }

    }
}
