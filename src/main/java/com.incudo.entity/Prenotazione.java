package com.incudo.entity;

import java.time.LocalDate;

public class Prenotazione {

    private int id;
    private int idAttività;
    private int idUtente;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    
    public Prenotazione () {}
    
	public Prenotazione(int id, int idAttività, int idUtente, LocalDate dataInizio, LocalDate dataFine) {

		this.id = id;
		this.idAttività = idAttività;
		this.idUtente = idUtente;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getIdAttività() {
		return idAttività;
	}


	public void setIdAttività(int idAttività) {
		this.idAttività = idAttività;
	}


	public int getIdUtente() {
		return idUtente;
	}


	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}


	public LocalDate getDataInizio() {
		return dataInizio;
	}


	public void setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
	}


	public LocalDate getDataFine() {
		return dataFine;
	}


	public void setDataFine(LocalDate dataFine) {
		this.dataFine = dataFine;
	}

}
