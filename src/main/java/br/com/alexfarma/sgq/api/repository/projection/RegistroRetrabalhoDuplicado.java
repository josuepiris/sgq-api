package br.com.alexfarma.sgq.api.repository.projection;

import java.util.Date;

public class RegistroRetrabalhoDuplicado {
	
	private String protocolo;

	private String motivoRetrabalho;

	private String funcionarioRegistro;

	private Date dataRegistro;

	private Long repeticoes;

	public RegistroRetrabalhoDuplicado(String protocolo, String motivoRetrabalho, String funcionarioRegistro,
			Date dataRegistro, Long repeticoes) {
		this.protocolo = protocolo;
		this.motivoRetrabalho = motivoRetrabalho;
		this.funcionarioRegistro = funcionarioRegistro;
		this.dataRegistro = dataRegistro;
		this.repeticoes = repeticoes;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getMotivoRetrabalho() {
		return motivoRetrabalho;
	}

	public void setMotivoRetrabalho(String motivoRetrabalho) {
		this.motivoRetrabalho = motivoRetrabalho;
	}

	public String getFuncionarioRegistro() {
		return funcionarioRegistro;
	}

	public void setFuncionarioRegistro(String funcionarioRegistro) {
		this.funcionarioRegistro = funcionarioRegistro;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Long getRepeticoes() {
		return repeticoes;
	}

	public void setRepeticoes(Long repeticoes) {
		this.repeticoes = repeticoes;
	}

}
