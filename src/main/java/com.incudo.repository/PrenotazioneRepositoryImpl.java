package com.incudo.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import com.incudo.entity.Prenotazione;


public class PrenotazioneRepositoryImpl implements PrenotazioneRepository {

	
	@Override
	public List<List<String>> leggiPrenotazioniDalFile(String filePath, char delimitatore) throws IOException {
		
		try (FileReader fileReader = new FileReader(filePath);
				
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withDelimiter(delimitatore).withTrim())) {

			return csvParser.getRecords().stream().map(record -> {
				
				List<String> values = new ArrayList<>();
				
				for (Object value : record) {
					values.add(value.toString());
				}
				return values;
			}).collect(Collectors.toList());
		}
	}

	
	@Override
	public void scriviPrenotazioniCsv(List<String[]> prenotazioniCsv, String filePath, char delimitatore) {
		
	    try (FileWriter fileWriter = new FileWriter(filePath);
	    		
	            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withDelimiter(delimitatore)
	                    .withHeader("ID", "ID Attività", "ID Utente", "Data inizio", "Data fine"))) {

	    	
	        for (String[] record : prenotazioniCsv) {
	            csvPrinter.printRecord((Object[]) record);
	        }

	        csvPrinter.flush();
	        
	    } 
	    catch (IOException e) {
	        e.printStackTrace();
	       
	    }
	}

	
	@Override
	public int trovaProssimoIdPrenotazione(List<Prenotazione> prenotazioni) {
		
	    if (prenotazioni.isEmpty()) {
	    	
	        return 1;
	    } 
	    else {
	        int maxId = prenotazioni.stream().mapToInt(Prenotazione::getId).max().orElse(0);
	        return maxId + 1;
	    }
	}
	
	
	
	@Override
	public void esportaPrenotazioniSuCsv(List<Prenotazione> prenotazioni,
			String filePath, char delimitatore) throws IOException {

		
		try (FileWriter fileWriter = new FileWriter(filePath);
				CSVPrinter csvPrinter = new CSVPrinter(fileWriter,
						CSVFormat.DEFAULT.withDelimiter(delimitatore).withHeader("ID Prenotazione", "ID Utente",
								"Data inizio Prenotazione", "Data fine Prenotazione"))) {

			for (Prenotazione prenotazione : prenotazioni) {
				csvPrinter.printRecord(prenotazione.getId(), prenotazione.getIdUtente(),
						prenotazione.getDataInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
						prenotazione.getDataFine().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			}

			csvPrinter.flush();

		}
	}

}
