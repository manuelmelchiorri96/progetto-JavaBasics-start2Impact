package com.incudo.service;

import java.io.IOException;
import java.util.List;


import com.incudo.entity.Corso;

public interface CorsoService {

	
	List<Corso> listaCorsi(String filePath, char delimitatore) throws IOException;
	
	void visualizzaCorsi() throws IOException;
	
	void prenotaCorso(int idCorso, int idUtente) throws IOException, InterruptedException;
	
	void scriviCorsiSuCsv(List<Corso> corsi, String filePath, char delimitatore) throws IOException, InterruptedException;
	
	void ripristinaDisponibilitaCorso(int idCorso) throws IOException, InterruptedException;
	
	void disdiciPrenotazioneERipristinaCorso(int idPrenotazione, int idCorso, int idUtente) throws IOException, InterruptedException;
	
	List<Corso> getCorsiDisponibili() throws IOException;
	
	void esportaCorsiDisponibiliSuCsv(String filePath, char delimitatore) throws IOException;
	
	void reimpostaDisponibilitaCorsi(List<Integer> idCorsi) throws IOException, InterruptedException;
	
}
