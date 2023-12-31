# Applicazione Java Basics - IncuDO Start2Impact

Questo è un progetto Java per la gestione delle prenotazioni dei corsi. L'applicazione consente agli utenti di visualizzare corsi disponibili, effettuare prenotazioni, cancellare prenotazioni, registrarsi come nuovi utenti e molto altro.

## Requisiti

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) versione 17 o successiva
- [Maven](https://maven.apache.org/download.cgi) per la gestione delle dipendenze

**1. Verifica la versione di Java:**

Assicurati di avere installato il Java Development Kit (JDK) versione 17 o successiva eseguendo il seguente comando:

```bash
java -version
```

**2. Verifica la versione di Maven:**

Assicurati di aver installato Maven globalmente nel tuo pc, puoi verificarlo con il seguente comando:

```bash
mvn -v
```


## Struttura del Progetto

- `src/`: Contiene il codice sorgente dell'applicazione.
- `pom.xml`: Il file di configurazione di Maven per il progetto.
- `.gitignore`: Il file di configurazione per filtrare il caricamento dei file sul repository.
- `LICENSE.txt`: La licenza per progetti open source.
- `README.md`: Il file di guida al progetto.

## Funzionalità Principali

L'applicazione offre le seguenti funzionalità principali:

1. **Visualizzazione di Corsi Disponibili**
   - Gli utenti possono visualizzare l'elenco dei corsi disponibili.

2. **Prenotazione di Corsi**
   - Gli utenti possono prenotare un corso specifico inserendo l'ID dell'utente e l'ID del corso.

3. **Cancellazione di Prenotazioni**
   - Gli utenti possono cancellare una prenotazione specifica inserendo l'ID dell'utente, l'ID della prenotazione e l'ID del corso.

4. **Registrazione di Nuovi Utenti**
   - Gli utenti possono registrarsi inserendo le informazioni personali, tra cui nome, cognome, data di nascita, indirizzo e documento ID.

5. **Eliminazione di Registrazione Utente**
   - Gli utenti possono eliminare la propria registrazione inserendo il proprio ID utente.

6. **Esportazione dei Corsi Disponibili**
   - Gli utenti possono esportare l'elenco dei corsi disponibili in un file CSV.

7. **Esportazione delle Prenotazioni Utente**
   - Gli utenti possono esportare le proprie prenotazioni in un file CSV.

## Come Iniziare

### Clonare il Repository

Puoi clonare il repository utilizzando il seguente comando:

```bash
git clone https://github.com/manuelmelchiorri96/progetto-JavaBasics-start2Impact.git
```

## Eseguire l'applicazione da terminale

### Eseguire l'applicazione con un file JAR nella directory del progetto

**1. Crea il file JAR:**

Crea il file JAR eseguibile eseguendo il seguente comando dalla directory principale del progetto:

Questo comando compilerà il codice sorgente e creerà un file JAR eseguibile nella directory target/.

```bash
mvn clean package
```

**2. Lancia l' applicazione:**

Puoi eseguire l'applicazione utilizzando il file JAR creato con il seguente comando:

Assicurati di essere nella directory principale del progetto prima di eseguirlo per garantire il corretto funzionamento.

Assicurati di sostituire NOME-DEL-JAR con il nome effettivo del file JAR creato.

```bash
java -jar target/NOME-DEL-JAR.jar
```


### Eseguire applicazione con un file JAR in una directory diversa dal progetto

**1. Eseguire il seguente comando:**

Per eseguire l' applicazione, basta digitare il seguente comando, l' applicazione farà riferimento a dei file presenti nella directory corrente.

**Attenzione**: Questo jar è progettato per essere eseguito solo in una directory contenente i file con questo percorso: src/main/resources/nomeFile.csv


```bash
java -jar NOME-DEL-JAR.jar
```


### Eseguire applicazione senza file JAR

**1. Compila l' applicazione:**

Per eseguire l'applicazione senza JAR, dalla directory del progetto eseguire questo comando:

```bash
mvn compile
```

**2. Lancia l' applicazione:**

Successivamente esegui il seguente comando per eseguire l'applicazione:

Sostituisci "com.example.Main" con il nome completo del package e della classe Java che desideri eseguire.

```bash
mvn exec:java -Dexec.mainClass="com.incudo.Main"
```

## Licenza

Questo progetto è distribuito con la licenza [GNU General Public License v3.0](LICENSE.txt).

Per favore, leggi il file [LICENSE](LICENSE.txt) per ulteriori dettagli sui termini e le condizioni della licenza.
