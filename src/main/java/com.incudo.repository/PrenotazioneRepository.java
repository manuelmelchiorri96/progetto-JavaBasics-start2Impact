package com.incudo.repository;

import java.io.IOException;
import java.util.List;

import com.incudo.entity.Prenotazione;


public interface PrenotazioneRepository {

	List<List<String>> leggiPrenotazioniDalFile(String filePath, char delimitatore) throws IOException;
	
	void scriviPrenotazioniCsv(List<String[]> prenotazioniCsv, String filePath, char delimitatore) throws IOException, InterruptedException;
	
	int trovaProssimoIdPrenotazione(List<Prenotazione> prenotazioni);
	
	void esportaPrenotazioniSuCsv(List<Prenotazione> prenotazioni, String filePath, char delimitatore) throws IOException, InterruptedException;
	
}
