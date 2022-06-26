import me.tongfei.progressbar.ProgressBar;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CreazioneDatabase implements Runnable {


    private final List<avanzamento> observers;
    ProgressBar pb = new ProgressBar("Prova",100);

    private Connection con;
    private Libreria lib;




    public  CreazioneDatabase(Connection con,Libreria libreria){
        this.con=con;
        this.lib=libreria;
        observers=new ArrayList<>();
    }
    public void addObserver(avanzamento obs) {
        observers.add(obs);
    }
    private void notifyObservers(int progression) {
        for (var obs : observers) {
            obs.update(progression);
        }
    }

    public void run(){
        Statement stmt;
        avanzamento avanz=new avanzamento();
        addObserver(avanz);

        try {
            stmt = con.createStatement();


            String CreateSql = "Create Table Libro(id_libro int primary key, titolo varchar not null, autore varchar not null, genere varchar not null ,in_prestito boolean not null) ";
            notifyObservers(10);
            stmt.addBatch(CreateSql);
            Thread.sleep(1000);
            String CreateSql2 = "Create Table Prestito(id_prestito int primary key, libro integer not null, prestante integer not null,ricevente integer, cliente integer not null, data_inizio_prestito timestamp not null,data_fine_prestito timestamp,multa_pagata boolean not null) ";
            notifyObservers(20);
            stmt.addBatch(CreateSql2);
            Thread.sleep(1000);
            String CreateSql3 = "Create Table Libraio(id_libraio int primary key, numero_scrivania integer not null) ";
            notifyObservers(30);
            stmt.addBatch(CreateSql3);
            Thread.sleep(1000);
            String CreateSql4 = "Create Table Impiegato(id_impiegato int primary key, tipo varchar not null, stipendio double precision) ";
            notifyObservers(40);
            stmt.addBatch(CreateSql4);
            Thread.sleep(1000);
            String CreateSql5 = "Create Table Cliente(id_cliente int primary key) ";
            notifyObservers(50);
            stmt.addBatch(CreateSql5);
            Thread.sleep(1000);
            String CreateSql6 = "Create Table Cassiere(id_cassiere int primary key,numero_scrivania integer not null) ";
            notifyObservers(60);
            stmt.addBatch(CreateSql6);
            Thread.sleep(1000);
            String CreateSql7 = "Create Table Persona(id_persona int primary key, nome_persona varchar not null,password varchar not null,indirizzo varchar not null,numero_telefono integer not null) ";
            notifyObservers(70);
            stmt.addBatch(CreateSql7);
            Thread.sleep(1000);
            String CreateSql8 = "Create Table Libro_prenotato(id_prenotazione int primary key, libro integer not null, cliente integer not null,data_prenotazione timestamp) ";
            notifyObservers(80);
            stmt.addBatch(CreateSql8);
            Thread.sleep(1000);
            String CreateSql9 = "Create Table Libro_in_prestito(id_libro int primary key, cliente integer not null) ";
            notifyObservers(90);
            stmt.addBatch(CreateSql9);
            Thread.sleep(1000);

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
            String CreateSql19 = "ALTER TABLE Libro_in_prestito ADD CONSTRAINT fk_clente FOREIGN KEY (cliente) REFERENCES Cliente (id_cliente); ";
            stmt.addBatch(CreateSql19);

            stmt.executeBatch();
            notifyObservers(100);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
