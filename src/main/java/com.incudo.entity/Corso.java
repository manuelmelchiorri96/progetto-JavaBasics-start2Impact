package com.incudo.entity;

import java.time.LocalDate;

public class Corso {

	private int id;
	private String nome;
	private String descrizione;
	private LocalDate data;
	private int durata;
	private String luogo;
	private String disponibile;

	public Corso() {}
	
	public Corso(int id, String nome, String descrizione, LocalDate data, int durata, String luogo,
			String disponibile) {

		this.id = id;
		this.nome = nome;
		this.descrizione = descrizione;
		this.data = data;
		this.durata = durata;
		this.luogo = luogo;
		this.disponibile = disponibile;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	public String getLuogo() {
		return luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}

	public String getDisponibile() {
		return disponibile;
	}

	public void setDisponibile(String disponibile) {
		this.disponibile = disponibile;
	}

}
