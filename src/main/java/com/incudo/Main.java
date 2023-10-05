package com.incudo;

import com.incudo.configuration.ConfigurazioneFile;
import com.incudo.entity.Corso;
import com.incudo.entity.Prenotazione;
import com.incudo.entity.Utente;
import com.incudo.service.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        CorsoService corsoService = new CorsoServiceImpl();
        PrenotazioneService prenotazioneService = new PrenotazioneServiceImpl();
        UtenteService utenteService = new UtenteServiceImpl();


        List<Corso> corsi = corsoService.listaCorsi(ConfigurazioneFile.filePathCorsi, ConfigurazioneFile.delimitatore);
        List<Prenotazione> prenotazioni = prenotazioneService.listaPrenotazioni(ConfigurazioneFile.filePathPrenotazioni,
                ConfigurazioneFile.delimitatore);
        List<Utente> utenti = utenteService.listaUtenti(ConfigurazioneFile.filePathUtenti,
                ConfigurazioneFile.delimitatore);


        Scanner scanner = new Scanner(System.in);

        int scelta;

        do {
            System.out.println();
            System.out.println("Menù:");
            System.out.println("1. Visualizza tutti i corsi");
            System.out.println("2. Prenota un corso");
            System.out.println("3. Disdici una prenotazione");
            System.out.println("4. Registrati");
            System.out.println("5. Elimina la tua registrazione");
            System.out.println("6. Esporta i corsi disponibili");
            System.out.println("7. Esporta le tue prenotazioni attuali");
            System.out.println("0. Esci");

            scelta = scanner.nextInt();

            switch (scelta) {
                case 1:
                    corsoService.visualizzaCorsi();
                    break;

                case 2:
                    System.out.println();
                    System.out.println("Inserisci i dati per prenotare un corso");
                    System.out.print("Inserisci il tuo ID: ");
                    int idUtente = scanner.nextInt();

                    System.out.println();
                    System.out.print("Inserisci l'ID del corso: ");
                    int idCorso = scanner.nextInt();

                    corsoService.prenotaCorso(idCorso, idUtente);
                    break;

                case 3:
                    System.out.println();
                    System.out.println("Inserisci i dati per disdire una prenotazione");
                    System.out.print("Inserisci il tuo ID: ");
                    int idUtentePerCancellazione = scanner.nextInt();

                    System.out.println();
                    System.out.print("Inserisci l'ID della prenotazione da cancellare: ");
                    int idPrenotazione = scanner.nextInt();

                    System.out.println();
                    System.out.print("Inserisci l'ID del corso: ");
                    int idCorsoPerCancellazione = scanner.nextInt();

                    corsoService.disdiciPrenotazioneERipristinaCorso(idPrenotazione, idCorsoPerCancellazione,
                            idUtentePerCancellazione);
                    break;

                case 4:
                    System.out.println();
                    System.out.println("Inserisci i tuoi dati per registrarti");
                    System.out.print("ID: ");
                    int idUtenteDaRegistrare = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println();
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();

                    System.out.println();
                    System.out.print("Cognome: ");
                    String cognome = scanner.nextLine();

                    System.out.println();
                    System.out.print("Data di nascita (dd/MM/yyyy): ");
                    String dataDiNascita = scanner.nextLine();

                    System.out.println();
                    System.out.print("Indirizzo: ");
                    String indirizzo = scanner.nextLine();

                    System.out.println();
                    System.out.print("Documento ID: ");
                    String documentoId = scanner.nextLine();

                    utenteService.inserisciNuovoUtente(idUtenteDaRegistrare, nome, cognome, dataDiNascita, indirizzo,
                            documentoId, ConfigurazioneFile.filePathUtenti, ConfigurazioneFile.delimitatore);
                    break;

                case 5:
                    System.out.println();
                    System.out.println("Inserisci i tuoi dati per eliminare la tua registrazione");
                    System.out.print("Il tuo ID: ");
                    int idUtenteDaEliminare = scanner.nextInt();
                    scanner.nextLine();

                    utenteService.eliminaUtente(idUtenteDaEliminare, ConfigurazioneFile.filePathUtenti,
                            ConfigurazioneFile.delimitatore);
                    List<Integer> corsiDaRipristinare = prenotazioneService.cancellaPrenotazioniUtente(idUtenteDaEliminare);
                    corsoService.reimpostaDisponibilitaCorsi(corsiDaRipristinare);
                    break;

                case 6:
                    System.out.println();
                    System.out.println("Inserisci i dati per esportare su un file i corsi disponibili");
                    System.out.print("Indica il file (Inserisci percorso del file, il file deve essere vuoto): ");
                    String filePathDoveEsportareCorsiDisponibili = scanner.next();

                    System.out.println();
                    System.out.print("Indica il delimitatore del file: ");
                    char delimitatoreFile = scanner.next().charAt(0);

                    corsoService.esportaCorsiDisponibiliSuCsv(filePathDoveEsportareCorsiDisponibili, delimitatoreFile);
                    break;

                case 7:
                    System.out.println();
                    System.out.println("Inserisci i dati per esportare su un file le tue prenotazioni attuali");
                    System.out.print("Inserisci il tuo ID: ");
                    int idUtenteCheEsportaPrenotazioni = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println();
                    System.out.print("Nome: ");
                    String nomeUtenteCheEsportaPrenotazioni = scanner.nextLine();

                    System.out.println();
                    System.out.print(
                            "Indica il file dove esportare le tue prenotazioni attuali (Inserisci percorso del file, il file deve essere vuoto): ");
                    String filePathDoveEsportarePrenotazioniUtente = scanner.nextLine();

                    System.out.println();
                    System.out.print("Indica il delimitatore del file: ");
                    char delimitatoreFilePrenotazioniUtente = scanner.next().charAt(0);

                    prenotazioneService.esportaPrenotazioniUtenteSuCsv(nomeUtenteCheEsportaPrenotazioni,
                            idUtenteCheEsportaPrenotazioni, filePathDoveEsportarePrenotazioniUtente,
                            delimitatoreFilePrenotazioniUtente);
                    break;

                case 0:
                    System.out.println();
                    System.out.println("Arrivederci!");
                    break;

                default:
                    System.out.println();
                    System.out.println("Scelta non valida. Riprova.");
            }
        } while (scelta != 0);
    }

}
