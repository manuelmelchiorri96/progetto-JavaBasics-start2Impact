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

import com.incudo.configuration.ConfigurazioneFile;
import com.incudo.entity.Prenotazione;
import com.incudo.entity.Utente;
import com.incudo.repository.PrenotazioneRepository;
import com.incudo.repository.PrenotazioneRepositoryImpl;


public class PrenotazioneServiceImpl implements PrenotazioneService {


	private final PrenotazioneRepository prenotazioneRepository = new PrenotazioneRepositoryImpl();
	private final UtenteService utenteService = new UtenteServiceImpl();
	
	
	@Override
	public List<Prenotazione> listaPrenotazioni(String filePath, char delimitatore) throws IOException {

		List<List<String>> csvData = prenotazioneRepository.leggiPrenotazioniDalFile(filePath, delimitatore);

		List<Prenotazione> prenotazioni = new ArrayList<>();

		
		for (List<String> record : csvData) {
			
			Prenotazione prenotazione = mapToPrenotazione(record);

			if (prenotazione != null) {
				
				prenotazioni.add(prenotazione);
			}
		}

		return prenotazioni;
	}
	

	private Prenotazione mapToPrenotazione(List<String> record) {
		
		if (record.size() != 5) {
			
			return null;
		}

		try {
			Prenotazione prenotazione = new Prenotazione();
			prenotazione.setId(Integer.parseInt(record.get(0)));
			prenotazione.setIdAttività(Integer.parseInt(record.get(1)));
			prenotazione.setIdUtente(Integer.parseInt(record.get(2)));
			prenotazione.setDataInizio(parseDate(record.get(3)));
			prenotazione.setDataFine(parseDate(record.get(4)));
			
			return prenotazione;
		} 
		catch (NumberFormatException e) {
			
			return null;
		}
	}

	
	private LocalDate parseDate(String dateStr) {
		
		try {
			
			return LocalDate.parse(dateStr.trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} 
		catch (DateTimeParseException e) {
			return null;
		}
	}

	
	@Override
	public void scriviPrenotazioniSuCsv(List<Prenotazione> prenotazioni, String filePath, char delimitatore)
	        throws IOException, InterruptedException {
	    
	    List<String[]> prenotazioniCsv = new ArrayList<>();

	    for (Prenotazione prenotazione : prenotazioni) {
	        String[] record = new String[5];
	        record[0] = String.valueOf(prenotazione.getId());
	        record[1] = String.valueOf(prenotazione.getIdAttività());
	        record[2] = String.valueOf(prenotazione.getIdUtente());
	        record[3] = prenotazione.getDataInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        record[4] = prenotazione.getDataFine().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

	        prenotazioniCsv.add(record);
	    }

	    
	    prenotazioneRepository.scriviPrenotazioniCsv(prenotazioniCsv, filePath, delimitatore);
	}

	
	@Override
	public int trovaProssimoIdPrenotazione(List<Prenotazione> prenotazioni) {
		
		return prenotazioneRepository.trovaProssimoIdPrenotazione(prenotazioni);
	}


	@Override
	public void esportaPrenotazioniUtenteSuCsv(String nomeUtente, int idUtente, String filePath, char delimitatore)
			throws InterruptedException {
		try {
			Path path = Paths.get(filePath);
			if (!Files.exists(path) || !Files.isWritable(path)) {
				System.out.println();
				System.out.println("Percorso non valido o non scrivibile.");
				return;
			}

			List<Utente> utenti = utenteService.listaUtenti(ConfigurazioneFile.filePathUtenti, ConfigurazioneFile.delimitatore);


			Optional<Utente> utenteOptional = utenti.stream()
					.filter(u -> u.getId() == idUtente && u.getNome().equals(nomeUtente))
					.findFirst();

			if (utenteOptional.isEmpty()) {
				System.out.println();
				System.out.println("Nome utente e/o ID utente non validi o non corrispondenti.");
				return;
			}

			List<Prenotazione> prenotazioniUtente = getPrenotazioniUtente(idUtente);

			if (prenotazioniUtente.isEmpty()) {
				System.out.println();
				System.out.println(nomeUtente + ", non hai prenotazioni a tuo carico");
			} else {
				prenotazioneRepository.esportaPrenotazioniSuCsv(prenotazioniUtente, filePath, delimitatore);
				System.out.println();
				System.out.println(nomeUtente + ", esportazione delle tue prenotazioni completata con successo!");
			}
		} catch (IOException e) {
			System.out.println();
			System.out.println("Errore durante l'esportazione delle tue prenotazioni");
		}
	}




	@Override
    public List<Prenotazione> getPrenotazioniUtente(int idUtente) throws IOException {
        
        List<Prenotazione> prenotazioni = listaPrenotazioni(ConfigurazioneFile.filePathPrenotazioni, ConfigurazioneFile.delimitatore);


        return prenotazioni.stream()
                .filter(prenotazione -> prenotazione.getIdUtente() == idUtente)
                .collect(Collectors.toList());
    }



	@Override
	public List<Integer> cancellaPrenotazioniUtente(int idUtente) throws IOException, InterruptedException {

	    List<Prenotazione> prenotazioni = listaPrenotazioni(ConfigurazioneFile.filePathPrenotazioni, ConfigurazioneFile.delimitatore);

	    List<Prenotazione> prenotazioniDaCancellare = prenotazioni.stream()
	            .filter(prenotazione -> prenotazione.getIdUtente() == idUtente)
	            .toList();

	    List<Integer> idCorsiDaRipristinare = new ArrayList<>();

	    for (Prenotazione prenotazione : prenotazioniDaCancellare) {
	        idCorsiDaRipristinare.add(prenotazione.getIdAttività());
	    }

	    prenotazioni.removeAll(prenotazioniDaCancellare);

	    scriviPrenotazioniSuCsv(prenotazioni, ConfigurazioneFile.filePathPrenotazioni, ConfigurazioneFile.delimitatore);

	    if (!prenotazioniDaCancellare.isEmpty()) {
	    	System.out.println();
	        System.out.println("Le tue prenotazioni sono state cancellate con successo");
	    }
	    else {
	    	System.out.println();
	        System.out.println("Non ci sono prenotazioni associate all' utente con ID " + idUtente);
	    }

	    return idCorsiDaRipristinare;
	}

}
