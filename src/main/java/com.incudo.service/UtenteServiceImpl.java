package com.incudo.service;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import com.incudo.repository.UtenteRepositoryImpl;
import org.apache.commons.csv.CSVRecord;

import com.incudo.entity.Utente;
import com.incudo.repository.UtenteRepository;


public class UtenteServiceImpl implements UtenteService {


	private final UtenteRepository utenteRepository = new UtenteRepositoryImpl();
	
	
	
	@Override
	public List<Utente> listaUtenti(String filePath, char delimitatore) throws IOException {
		
	    List<CSVRecord> csvRecords = utenteRepository.leggiUtentiDalFile(filePath, delimitatore);
	    
	    return elaboraUtentiDaCsv(csvRecords);
	}

	
	private List<Utente> elaboraUtentiDaCsv(List<CSVRecord> csvRecords) {
		
	    List<Utente> utenti = new ArrayList<>();

	    for (CSVRecord record : csvRecords) {
	    	
	        Utente utente = new Utente();
	        
	        String idString = record.get("ID").trim();

	        
	        if (idString.isEmpty()) {
	            continue;
	        }

	        try {
	            utente.setId(Integer.parseInt(idString));
	        } 
	        catch (NumberFormatException e) {
	            e.printStackTrace();
	            continue;
	        }

	        utente.setNome(record.get("Nome"));
	        utente.setCognome(record.get("Cognome"));

	        try {
	            LocalDate dataDiNascita = LocalDate.parse(record.get("Data di nascita"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	            utente.setDataDiNascita(dataDiNascita);
	        } 
	        catch (DateTimeParseException e) {
	            e.printStackTrace();
	            continue;
	        }

	        utente.setIndirizzo(record.get("Indirizzo"));
	        utente.setDocumentoId(record.get("Documento ID"));

	        utenti.add(utente);
	    }

	    return utenti;
	}
	
	
	

	@Override
	public void inserisciNuovoUtente(int id, String nome, String cognome, String dataDiNascita, String indirizzo, String documentoId, String filePath, char delimitatore) throws IOException {
		try {
			Integer.parseInt(String.valueOf(id));
		}
		catch (InputMismatchException | NumberFormatException e) {
			System.out.println();
			System.out.println("L'ID deve essere un numero.");
			return;
		}


		if (nome.isEmpty() || cognome.isEmpty() || dataDiNascita.isEmpty() || indirizzo.isEmpty() || documentoId.isEmpty()) {
			System.out.println();
			System.out.println("Tutti i campi devono essere compilati.");
			return;
		}

		List<Utente> utenti = listaUtenti(filePath, delimitatore);

		boolean idEsistente = utenti.stream().anyMatch(utente -> utente.getId() == id);

		if (idEsistente) {
			System.out.println();
			System.out.println("L'ID specificato è già in uso da un altro utente. Scegli un altro ID.");
			return;
		}

		Utente nuovoUtente = new Utente();
		nuovoUtente.setId(id);

		try {
			nuovoUtente.setDataDiNascita(LocalDate.parse(dataDiNascita, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}
		catch (DateTimeParseException e) {
			System.out.println();
			System.out.println("Formato data non valido. Assicurati di inserire la data nel formato corretto.");
			return;
		}

		nuovoUtente.setNome(nome);
		nuovoUtente.setCognome(cognome);
		nuovoUtente.setIndirizzo(indirizzo);
		nuovoUtente.setDocumentoId(documentoId);

		utenti.add(nuovoUtente);

		utenteRepository.scriviUtentiSuCsv(utenti, filePath, delimitatore);

		System.out.println();
		System.out.println(nome + ", la tua registrazione è stata completata con successo!");
	}


	@Override
	public void eliminaUtente(int idUtente, String filePath, char delimitatore) throws IOException {
		
	    List<Utente> utenti = listaUtenti(filePath, delimitatore);
	    
	    int indiceDaEliminare = -1;
	    
	    String nomeUtenteEliminato = null;
	    
	    

	    for (int i = 0; i < utenti.size(); i++) {
	    	
	        Utente utente = utenti.get(i);
	        
	        if (utente.getId() == idUtente) {
	        	
	            indiceDaEliminare = i;
	            nomeUtenteEliminato = utente.getNome(); 
	            break;
	        }
	    }

	    if (indiceDaEliminare != -1) {
	    	
	        utenti.remove(indiceDaEliminare);
	        
	        utenteRepository.scriviUtentiSuCsv(utenti, filePath, delimitatore);
	        
	        System.out.println();
	        System.out.println(nomeUtenteEliminato.endsWith("a") ? nomeUtenteEliminato + " sei stata eliminata correttamente" : nomeUtenteEliminato + " sei stato eliminato correttamente");
	    } 
	    else {
	    	System.out.println();
	    	System.out.println("L'utente con ID " + idUtente + " non esiste");
	    }
	}
	
	

}
