# Gestione Libreria

Programma ideato e scritto come progetto per l'esame di Ingegneria del software.

## Sommario attori:
Il programma prevede 3 livelli di autorizazzione ogniuno con diversi tipi di funzionalità disponibili, in particolare:
1. *Sys Admin:*<br />
   
   Livello massimo di accesso.<br />
   Prevede le seguenti funzionalità:<br />

   - Aggiungi cassiere <br />
   - Aggiungi libraio<br />
   - Stampa storico prestiti<br />
   - Stampa inventario Libri <br />

2. *Libraio:*<br />

   Secondo livello di privilegi, ne può esistere solo uno in libreria.<br />
   
   - Cerca un libro     <br />        
   - Prenota un libro           <br />
   - Stampa informazioni cliente	<br />
   - Controlla multa cliente	<br />
   - Controlla coda prenotazione di un libro<br />
   - Dai in prestito un libro<br />
   - Ricevi dal prestito un libro  <br />
   - Rinnova un libro        <br />     
   - Aggiungi nuovo cliente    <br />   
   - Aggiorna informazioni cliente   <br />
   - Aggiungi un libro    <br />       
   - Rimuovi un libro     <br />       
   - Cambia informazioni libro		<br />
   - Stampa informazioni cassiere<br />

3. *Cassiere:*<br />

   Terzo livello di privilegi, ne possono essere aggiunti infiniti alla libreria.
  
   - Cerca un libro<br />
   - Prenota un libro<br />
   - Controlla informazioni cliente<br />
   - Controlla multe cliente<br />
   - Controlla coda prenotazioni di un libro<br />
   - Dai in prestito un libro<br />
   - Ricevi dal prestito un libro<br />
   - Rinnova un libro<br />
   - Aggiungi nuovo cliente<br />
   - Cambia informazioni cliente<br />
   
4. *Cliente:*<br />

   Quarto livello di privilegi, possono essere aggiunti da Cassieri e Libraio.
   - Ricerca un libro  <br />
   - Prenota un libro  <br />
   - Stampa i tuoi dati  <br />
   - Controlla le tue multe <br />
   - Controlla prenotazioni per un libro <br />
   
## Sommario funzionalità:
### Disponibili per ogni livello di privilegi:
1. *Ricerca di un Libro:*<br />

    E' possibile cercare un libro presente in libreria per Autore, Genere o titolo.

2. *Prenotazione di un libro:*<br />

    Permette la prenotazione di un libro, se è richiesta dal cliente la prenotazione sarà effettuata per se stesso, nel caso la prenotazione fosse richiesta da un cassiere o dal libraio è prevista la ricerca di un cliente per cui effettuarla.

3. *Stampa informazioni cliente:*<br />

    Permette la stampa dei dati personali, se richiesta dal cliente stamperà i propri dati personali, nel caso fosse richiesta da un cassiere o da un libraio è prevista la ricerca di un cliente.

4. *Controllo multa di un cliente:*<br />

    Stampa tutte le multe associate ad un dato cliente per ogni singolo prestito, nel caso una o più multe non fossero state saldate è previsto un conteggio del totale e la possibilià di pagarle singolarmente per ogni prestito.
    E' permesso il controllo delle multe nel caso un cliente voglia pagarle di persona da un cassiere o dal libraio.

5. *Controllo coda di prenotazione per un libro:*<br />

    Ricerca un particolare libro e stampa la sua coda di prenotazioni.

### Disponibili solo per Cassiere e Libraio:

6. *Dai in prestito un libro:*<br />

    Prevede la ricerca di un libro e di un cliente a cui cedere un libro in prestito.

7. *Ricezione di un libro dal prestito:*<br />

    Prevede la ricerca di un cliente e la selezione dei libri attualmente in prestito ad un dato cliente per la restituzione. Nel caso la restituzione avvenisse dopo il periodo indicato di scadenza prevederà il calcolo della multa e la possibilià di pagarla subito o successivamente.

8. *Rinnova un libro:*<br />

    Prevede la ricerca di un cliente e la possibilità di rinnovare uno dei prestiti che ha attualmente, in particolare estende la durata del prestito come se il libro fosse appena stato ricevuto.

9. *Aggiunta nuovo cliente:*<br />

    Prevede l'aggiunta di un nuovo cliente, dopo l'inserimento dei suoi dati personali verranno generati id e password personali per l'accesso all'area personale.

10. *Cambia informazioni personali di un cliente:*<br />

    Possibilià di cambiare nome, indirizzo o numero di telefono di un cliente esistente.

### Disponibili solo per il libraio:

11. *Aggiungi Libro:*<br />

    Possibilià di aggiungere un libro alla libreria.

12. *Rimuovi Libro:*<br />

    Possibilià di rimuovere dalla libreria un libro esistente.

13. *Cambia informazioni libro:*<br />

    Possibilità di cambiare Titolo, Autore o Genere di un libro già presente in libreria.

14. *Stampa informazioni cassiere:*<br />

    Possibilità di stampare le informazioni personali di un cassiere.

## Sommario persistenza dei dati e DB:

   E' previsto un sistema di salvataggio dati su DB esterno.
   In particolare è stato usato PostGreSQL.

   Il sistema prevede la creazione del database al primo avvio nel caso non esistesse utilizzando delle credenziali di accesso di default.
   La comunicazione frà applicativo e database non avviene in maniera sincrona, all'avvio il sistema carica il contenuto del database nei vari array utilizzati dal programma, durante l'esecuzione i suddetti array verranno modificati ma saranno effettivamente modificati nel DB solo al momento della chiusura del programma scegliendo l'opzione "ESCI" dal menù.


### Diagramma E-R database:

![ERLIBRERIA](https://user-images.githubusercontent.com/28054437/178505768-215b2d89-b834-456c-bf1e-561a00fbfd00.jpg)



##Class Diagram:

###Persone:
![Persone!ClassDiagram1_0](https://user-images.githubusercontent.com/28054437/179314057-6381ee8d-a454-4d35-8018-f06d98d4b448.png)

###Libro:
![Libri!ClassDiagram1_1](https://user-images.githubusercontent.com/28054437/179314084-951903ff-f38f-4ead-8e30-98fb80e68ebd.png)

###DatabaseUtility:
![DatabaseUtility!ClassDiagram1_2](https://user-images.githubusercontent.com/28054437/179314106-92d24dca-6055-45af-ba50-1dc5fdff8247.png)


##Use Cases:

###Cliente:
![UseCaseCliente!UseCaseDiagram1_3](https://user-images.githubusercontent.com/28054437/179314204-022d02eb-61fc-40ae-b5b9-10ba292b44d8.png)

###Cassiere:
![UseCaseCassiere!UseCaseDiagram1_4](https://user-images.githubusercontent.com/28054437/179314245-b0e6ad3e-c794-402f-b417-9a223215b280.png)

###Libraio:
![UseCaseLibraio!UseCaseDiagram1_5](https://user-images.githubusercontent.com/28054437/179314275-e44b4dba-a0f2-4c9e-8685-88e6b1f448ba.png)

###Operazioni disponibili al Cliente ma con i privilegi di Cassiere e Libraio:
![Model1!UseCaseDiagram1_6](https://user-images.githubusercontent.com/28054437/179314401-cf032515-e3c0-422e-90b0-57bf3f8d825a.png)

###SysAdmin:
![UseCaseDiagram1](https://user-images.githubusercontent.com/28054437/179314852-1ead75c2-a408-4916-b5f6-3c6b1febc965.png)

