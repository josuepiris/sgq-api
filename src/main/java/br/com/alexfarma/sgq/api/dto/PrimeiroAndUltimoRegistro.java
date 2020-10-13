package br.com.alexfarma.sgq.api.dto;

import java.util.Date;

public class PrimeiroAndUltimoRegistro {

	private Date registroMaisAntigo;

	private Date registroMaisRecente;

	public PrimeiroAndUltimoRegistro(Date registroMaisAntigo, Date registroMaisRecente) {
		this.registroMaisAntigo = registroMaisAntigo;
		this.registroMaisRecente = registroMaisRecente;
	}

	public Date getRegistroMaisAntigo() {
		return registroMaisAntigo;
	}

	public void setRegistroMaisAntigo(Date registroMaisAntigo) {
		this.registroMaisAntigo = registroMaisAntigo;
	}

	public Date getRegistroMaisRecente() {
		return registroMaisRecente;
	}

	public void setRegistroMaisRecente(Date registroMaisRecente) {
		this.registroMaisRecente = registroMaisRecente;
	}

}
