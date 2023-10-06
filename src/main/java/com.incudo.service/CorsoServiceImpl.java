package com.incudo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.incudo.repository.CorsoRepositoryImpl;
import org.apache.commons.csv.CSVRecord;

import com.incudo.configuration.ConfigurazioneFile;
import com.incudo.entity.Corso;
import com.incudo.entity.Prenotazione;
import com.incudo.entity.Utente;
import com.incudo.repository.CorsoRepository;


public class CorsoServiceImpl implements CorsoService {


	private final PrenotazioneService prenotazioneService = new PrenotazioneServiceImpl();
	private final CorsoRepository corsoRepository = new CorsoRepositoryImpl();
	private final UtenteService utenteService = new UtenteServiceImpl();
	
	
	
	@Override
	public List<Corso> listaCorsi(String filePath, char delimitatore) throws IOException {
		
		List<CSVRecord> csvRecords = corsoRepository.leggiCorsiDalFile(filePath, delimitatore);
		
		return elaboraCorsiDaCsv(csvRecords);
	}

	
	private List<Corso> elaboraCorsiDaCsv(List<CSVRecord> csvRecords) {
		
		List<Corso> corsi = new ArrayList<>();

		
		for (CSVRecord record : csvRecords) {
			
			Corso corso = mappaRecordACorso(record);
			
			if (corso != null) {
				
				corsi.add(corso);
			}
		}

		return corsi;
	}

	
	private Corso mappaRecordACorso(CSVRecord record) {
		
		Corso corso = new Corso();
		
		String idString = record.get("ID").trim();

		
		if (idString.isEmpty()) {
			return null;
		}

		try {
			corso.setId(Integer.parseInt(idString));
		} 
		catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}

		corso.setNome(record.get("Nome"));
		corso.setDescrizione(record.get("Descrizione"));

		try {
			corso.setData(LocalDate.parse(record.get("Data"), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		} 
		catch (DateTimeParseException e) {
			e.printStackTrace();
			return null;
		}

		String durataString = record.get("Durata (ore)").trim();

		if (durataString.isEmpty()) {
			return null;
		}

		try {
			corso.setDurata(Integer.parseInt(durataString));
		} 
		catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		corso.setLuogo(record.get("Luogo").trim());
		corso.setDisponibile(record.get("Disponibile"));

		return corso;
	}

	
	@Override
	public void visualizzaCorsi() throws IOException {

		List<Corso> corsi = listaCorsi(ConfigurazioneFile.filePathCorsi, ConfigurazioneFile.delimitatore);

		
		if (corsi.isEmpty()) {
			System.out.println();
			System.out.println("Nessun corso disponibile.");
			System.out.println();
		} 
		else {
			System.out.println();
			System.out.println("Elenco dei corsi disponibili:");

			for (Corso corso : corsi) {
				System.out.println();
				System.out.println("ID: " + corso.getId());
				System.out.println("Nome: " + corso.getNome());
				System.out.println("Descrizione: " + corso.getDescrizione());
				System.out.println("Data: " + corso.getData());
				System.out.println("Durata (ore): " + corso.getDurata());
				System.out.println("Luogo: " + corso.getLuogo());
				System.out.println("Disponibile: " + corso.getDisponibile());
				System.out.println();
			}
		}

	}



	@Override
	public void prenotaCorso(int idCorso, int idUtente) throws IOException, InterruptedException {

		List<Corso> corsi = listaCorsi(ConfigurazioneFile.filePathCorsi, ConfigurazioneFile.delimitatore);

		List<Prenotazione> prenotazioni = prenotazioneService
				.listaPrenotazioni(ConfigurazioneFile.filePathPrenotazioni, ConfigurazioneFile.delimitatore);

		List<Utente> utenti = utenteService.listaUtenti(ConfigurazioneFile.filePathUtenti, ConfigurazioneFile.delimitatore);

		Optional<Corso> corsoDaPrenotare = corsi.stream().filter(corso -> corso.getId() == idCorso).findFirst();
		Optional<Utente> utentePrenotante = utenti.stream().filter(utente -> utente.getId() == idUtente).findFirst();

		if (utentePrenotante.isEmpty()) {
			System.out.println();
			System.out.println("ID utente non valido. La prenotazione non può essere effettuata.");
			return;
		}

		if (corsoDaPrenotare.isEmpty()) {
			System.out.println();
			System.out.println("ID del corso non valido. La prenotazione non può essere effettuata.");
			return;
		}

		if (!corsoDaPrenotare.get().getDisponibile().equalsIgnoreCase("SI")) {
			System.out.println();
			System.out.println("Il corso non è disponibile per la prenotazione. La prenotazione non può essere effettuata.");
			return;
		}

		int prossimoId = prenotazioneService.trovaProssimoIdPrenotazione(prenotazioni);

		Prenotazione nuovaPrenotazione = new Prenotazione();
		nuovaPrenotazione.setId(prossimoId);
		nuovaPrenotazione.setIdAttività(idCorso);
		nuovaPrenotazione.setIdUtente(idUtente);
		LocalDate oggi = LocalDate.now();
		LocalDate dataFine = oggi.plusDays(7);

		nuovaPrenotazione.setDataInizio(oggi);
		nuovaPrenotazione.setDataFine(dataFine);

		corsoDaPrenotare.get().setDisponibile("NO");

		scriviCorsiSuCsv(corsi, ConfigurazioneFile.filePathCorsi, ConfigurazioneFile.delimitatore);

		prenotazioni.add(nuovaPrenotazione);

		prenotazioneService.scriviPrenotazioniSuCsv(prenotazioni, ConfigurazioneFile.filePathPrenotazioni,
				ConfigurazioneFile.delimitatore);

		String nomeUtente = utentePrenotante.get().getNome();
		int idPrenotazione = nuovaPrenotazione.getId();

		System.out.println();
		System.out.println(nomeUtente + " la tua prenotazione è avvenuta con successo!");
		System.out.println("ID della tua prenotazione: " + idPrenotazione);
	}



	@Override
	public void scriviCorsiSuCsv(List<Corso> corsi, String filePath, char delimitatore)
			throws InterruptedException {
		
		try {
			corsoRepository.scriviCorsiSuCsv(corsi, filePath, delimitatore);

		} 
		catch (IOException e) {
			e.printStackTrace();

		}
	}
	
	
	
	@Override
	public void ripristinaDisponibilitaCorso(int idCorso) throws IOException, InterruptedException {
		
	    List<Corso> corsi = listaCorsi(ConfigurazioneFile.filePathCorsi, ConfigurazioneFile.delimitatore);

	    Optional<Corso> corsoAssociato = corsi.stream()
	            .filter(corso -> corso.getId() == idCorso)
	            .findFirst();

	    if (corsoAssociato.isPresent()) {
	        corsoAssociato.get().setDisponibile("SI");

	        scriviCorsiSuCsv(corsi, ConfigurazioneFile.filePathCorsi, ConfigurazioneFile.delimitatore);
	    } 
	    else {
	    	System.out.println();
	        System.out.println("Corso associato non trovato.");
	    }
	}



	@Override
	public void disdiciPrenotazioneERipristinaCorso(int idPrenotazione, int idCorso, int idUtente) throws IOException {

		List<Prenotazione> prenotazioni = prenotazioneService.listaPrenotazioni(ConfigurazioneFile.filePathPrenotazioni, ConfigurazioneFile.delimitatore);

		List<Utente> utenti = utenteService.listaUtenti(ConfigurazioneFile.filePathUtenti, ConfigurazioneFile.delimitatore);

		Optional<Utente> utentePrenotante = utenti.stream().filter(utente -> utente.getId() == idUtente).findFirst();

		if (utentePrenotante.isPresent()) {
			utentePrenotante.ifPresent(utente -> {
				String nomeUtente = utente.getNome();

				Optional<Prenotazione> prenotazioneDaCancellare = prenotazioni.stream()
						.filter(prenotazione -> prenotazione.getId() == idPrenotazione && prenotazione.getIdAttività() == idCorso && prenotazione.getIdUtente() == idUtente)
						.findFirst();

				if (prenotazioneDaCancellare.isPresent()) {
					prenotazioni.remove(prenotazioneDaCancellare.get());

					try {
						prenotazioneService.scriviPrenotazioniSuCsv(prenotazioni, ConfigurazioneFile.filePathPrenotazioni, ConfigurazioneFile.delimitatore);
					}
					catch (IOException | InterruptedException e) {
						throw new RuntimeException(e);
					}

                    int idCorsoAssociato = prenotazioneDaCancellare.get().getIdAttività();

					try {
						ripristinaDisponibilitaCorso(idCorsoAssociato);
					}
					catch (IOException | InterruptedException e) {
						throw new RuntimeException(e);
					}

                    System.out.println();
					System.out.println(nomeUtente + " hai cancellato la prenotazione con successo");
				} else {
					System.out.println();
					System.out.println("Prenotazione non trovata o ID non valido.");
				}
			});
		} else {
			System.out.println();
			System.out.println("Utente non trovato con l'ID specificato.");
		}
	}



	@Override
	public List<Corso> getCorsiDisponibili() throws IOException {
		
	    List<Corso> corsi = listaCorsi(ConfigurazioneFile.filePathCorsi, ConfigurazioneFile.delimitatore);
	    
	    return corsi.stream()
	            .filter(corso -> corso.getDisponibile().equalsIgnoreCase("SI"))
	            .collect(Collectors.toList());
	}


	@Override
	public void esportaCorsiDisponibiliSuCsv(String filePath, char delimitatore) {
		try {
			Path path = Paths.get(filePath);
			if (!Files.exists(path) || !Files.isWritable(path)) {
				System.out.println();
				System.out.println("Percorso non valido o non scrivibile.");
				return;
			}

			List<Corso> corsiDisponibili = getCorsiDisponibili();

			if (corsiDisponibili.isEmpty()) {
				System.out.println();
				System.out.println("Non ci sono corsi disponibili");
			} else {
				corsoRepository.esportaCorsiDisponibiliSuCsv(corsiDisponibili, filePath, delimitatore);
				System.out.println();
				System.out.println("Esportazione dei corsi disponibili completata con successo!");
			}
		} catch (IOException e) {
			System.out.println();
			System.out.println("Errore durante l'esportazione dei corsi disponibili");
		}
	}



	@Override
	public void reimpostaDisponibilitaCorsi(List<Integer> idCorsi) throws IOException, InterruptedException {
		
	    List<Corso> corsi = listaCorsi(ConfigurazioneFile.filePathCorsi, ConfigurazioneFile.delimitatore);

	    for (Integer idCorso : idCorsi) {
	        Optional<Corso> corsoAssociato = corsi.stream()
	                .filter(corso -> corso.getId() == idCorso)
	                .findFirst();

            corsoAssociato.ifPresent(corso -> corso.setDisponibile("SI"));
	    }

	    scriviCorsiSuCsv(corsi, ConfigurazioneFile.filePathCorsi, ConfigurazioneFile.delimitatore);
	}

}
