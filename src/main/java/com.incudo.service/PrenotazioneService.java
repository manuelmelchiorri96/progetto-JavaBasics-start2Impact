package com.incudo.service;

import java.io.IOException;
import java.util.List;


import com.incudo.entity.Prenotazione;

public interface PrenotazioneService {

	List<Prenotazione> listaPrenotazioni(String filePath, char delimitatore) throws IOException;
	
	void scriviPrenotazioniSuCsv(List<Prenotazione> prenotazioni, String filePath, char delimitatore) throws IOException, InterruptedException;
	
	int trovaProssimoIdPrenotazione(List<Prenotazione> prenotazioni);
	
	void esportaPrenotazioniUtenteSuCsv(String nome, int idUtente, String filePath, char delimitatore) throws IOException, InterruptedException;
	
	List<Prenotazione> getPrenotazioniUtente(int idUtente) throws IOException;
	
	List<Integer> cancellaPrenotazioniUtente(int idUtente) throws IOException, InterruptedException;
	
}
