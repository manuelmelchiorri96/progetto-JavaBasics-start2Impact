package com.incudo.repository;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.incudo.entity.Utente;

public interface UtenteRepository {

	List<CSVRecord> leggiUtentiDalFile(String filePath, char delimitatore) throws IOException;
	
	void scriviUtentiSuCsv(List<Utente> utenti, String filePath, char delimitatore) throws IOException;

}
