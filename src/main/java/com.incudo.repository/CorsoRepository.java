package com.incudo.repository;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.incudo.entity.Corso;

public interface CorsoRepository {

	List<CSVRecord> leggiCorsiDalFile(String filePath, char delimitatore) throws IOException;
	
	void scriviCorsiSuCsv(List<Corso> corsi, String filePath, char delimitatore) throws IOException, InterruptedException;
	
	void esportaCorsiDisponibiliSuCsv(List<Corso> corsi, String filePath, char delimitatore) throws IOException;
	
}
