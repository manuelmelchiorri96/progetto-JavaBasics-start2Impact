package com.incudo.service;

import java.io.IOException;
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


	private PrenotazioneService prenotazioneService = new PrenotazioneServiceImpl();


	private CorsoRepository corsoRepository = new CorsoRepositoryImpl();


	private UtenteService utenteService = new UtenteServiceImpl();
	
	
	
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

	    
	    
	    if (corsoDaPrenotare.isPresent() && corsoDaPrenotare.get().getDisponibile().equalsIgnoreCase("SI") && utentePrenotante.isPresent()) {
	    	
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
	    else {
	        System.out.println();
	        System.out.println("Il corso non è disponibile per la prenotazione, ID del corso o dell'utente non valido.");
	    }
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
	public void disdiciPrenotazioneERipristinaCorso(int idPrenotazione, int idCorso, int idUtente) throws IOException, InterruptedException {
		
	    List<Prenotazione> prenotazioni = prenotazioneService.listaPrenotazioni(ConfigurazioneFile.filePathPrenotazioni, ConfigurazioneFile.delimitatore);

	    List<Utente> utenti = utenteService.listaUtenti(ConfigurazioneFile.filePathUtenti, ConfigurazioneFile.delimitatore);
	    
	    
	    Optional<Utente> utentePrenotante = utenti.stream().filter(utente -> utente.getId() == idUtente).findFirst();
	    
	    Optional<Prenotazione> prenotazioneDaCancellare = prenotazioni.stream()
	            .filter(prenotazione -> prenotazione.getId() == idPrenotazione && prenotazione.getIdAttività() == idCorso && prenotazione.getIdUtente() == idUtente)
	            .findFirst();

	    String nomeUtente = utentePrenotante.get().getNome();
	    
	    
	    if (prenotazioneDaCancellare.isPresent()) {
	        prenotazioni.remove(prenotazioneDaCancellare.get());

	        prenotazioneService.scriviPrenotazioniSuCsv(prenotazioni, ConfigurazioneFile.filePathPrenotazioni, ConfigurazioneFile.delimitatore);

	        int idCorsoAssociato = prenotazioneDaCancellare.get().getIdAttività();

	        ripristinaDisponibilitaCorso(idCorsoAssociato);

	        System.out.println();
	        System.out.println(nomeUtente + " hai cancellato la prenotazione con successo");
	    } 
	    else {
	    	System.out.println();
	        System.out.println("Prenotazione non trovata o ID non valido.");
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
			List<Corso> corsiDisponibili = getCorsiDisponibili();

			if (corsiDisponibili.isEmpty()) {

				System.out.println();
				System.out.println("Non ci sono corsi disponibili ");
			} 
			else {

				corsoRepository.esportaCorsiDisponibiliSuCsv(corsiDisponibili, filePath, delimitatore);

				System.out.println();
				System.out.println("Esportazione dei corsi disponibili completata con successo!");
			}
		} 
		catch (IOException e) {

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
