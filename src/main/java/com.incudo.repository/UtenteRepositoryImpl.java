package com.incudo.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.incudo.entity.Utente;


public class UtenteRepositoryImpl implements UtenteRepository {

	
	@Override
	public List<CSVRecord> leggiUtentiDalFile(String filePath, char delimitatore) throws IOException {
		
	    try (FileReader fileReader = new FileReader(filePath);
	    		
	         CSVParser csvParser = new CSVParser(fileReader,
	                 CSVFormat.DEFAULT.withDelimiter(delimitatore)
	                         .withHeader("ID", "Nome", "Cognome", "Data di nascita", "Indirizzo", "Documento ID")
	                         .withTrim())) {

	    	
	        Iterator<CSVRecord> records = csvParser.iterator();

	        if (records.hasNext()) {
	            records.next(); 
	        }

	        return StreamSupport
	                .stream(Spliterators.spliteratorUnknownSize(records, Spliterator.ORDERED | Spliterator.NONNULL),
	                        false)
	                .collect(Collectors.toList());
	    }
	}

	
	
	@Override
	public void scriviUtentiSuCsv(List<Utente> utenti, String filePath, char delimitatore) {
		
	    try (FileWriter fileWriter = new FileWriter(filePath);
	    		
	         CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT
	                 .withDelimiter(delimitatore)
	                 .withHeader("ID", "Nome", "Cognome", "Data di nascita", "Indirizzo", "Documento ID"))) {

	    	
	        for (Utente utente : utenti) {
	            csvPrinter.printRecord(
	                utente.getId(),
	                utente.getNome(),
	                utente.getCognome(),
	                utente.getDataDiNascita().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
	                utente.getIndirizzo(),
	                utente.getDocumentoId()
	            );
	        }

	        csvPrinter.flush();
	    } 
	    catch (IOException e) {
	    	
	        e.printStackTrace();
	    }
	}


}
