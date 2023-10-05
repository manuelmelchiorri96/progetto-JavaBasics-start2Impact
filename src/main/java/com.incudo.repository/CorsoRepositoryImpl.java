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

import com.incudo.entity.Corso;

public class CorsoRepositoryImpl implements CorsoRepository {

	
	@Override
	public List<CSVRecord> leggiCorsiDalFile(String filePath, char delimitatore) throws IOException {
		try (FileReader fileReader = new FileReader(filePath);
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withDelimiter(delimitatore)
								.withHeader("ID", "Nome", "Descrizione", "Data", "Durata (ore)", "Luogo", "Disponibile")
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
	public void scriviCorsiSuCsv(List<Corso> corsi, String filePath, char delimitatore) {
		
	    try (FileWriter fileWriter = new FileWriter(filePath);
	    		
	         CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withDelimiter(delimitatore)
	                 .withHeader("ID", "Nome", "Descrizione", "Data", "Durata (ore)", "Luogo", "Disponibile"))) {

	    	
	        for (Corso corso : corsi) {
	          
	            String stato = corso.getDisponibile().equalsIgnoreCase("SI") ? "SI" : "NO";

	            csvPrinter.printRecord(corso.getId(), corso.getNome(), corso.getDescrizione(),
	                    corso.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), corso.getDurata(),
	                    corso.getLuogo(), stato); 

	            
	            corso.setDisponibile(stato);
	        }

	        csvPrinter.flush();
	    } 
	    catch (IOException e) {
	    	
	        e.printStackTrace();
	    }
	}

	
	@Override
	public void esportaCorsiDisponibiliSuCsv(List<Corso> corsi, String filePath, char delimitatore) throws IOException {

		
		try (FileWriter fileWriter = new FileWriter(filePath);

				CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withDelimiter(delimitatore)
						.withHeader("ID", "Nome", "Descrizione", "Data", "Durata (ore)", "Luogo", "Disponibile"))) {

			for (Corso corso : corsi) {

				csvPrinter.printRecord(corso.getId(), corso.getNome(), corso.getDescrizione(),
						corso.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), corso.getDurata(),
						corso.getLuogo(), corso.getDisponibile());
			}

			csvPrinter.flush();
		}
	}
	

}
