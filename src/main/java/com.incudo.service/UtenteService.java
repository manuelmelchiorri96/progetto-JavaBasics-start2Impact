package com.incudo.service;

import java.io.IOException;
import java.util.List;


import com.incudo.entity.Utente;

public interface UtenteService {
	
	List<Utente> listaUtenti(String filePath, char delimitatore) throws IOException;
	
	void inserisciNuovoUtente(int id, String nome, String cognome, String dataDiNascita, String indirizzo, String documentoId, String filePath, char delimitatore) throws IOException;

	void eliminaUtente(int idUtente, String filePathUtenti, char delimitatore) throws IOException;
	
}
