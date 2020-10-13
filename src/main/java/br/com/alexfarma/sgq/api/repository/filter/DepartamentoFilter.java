package br.com.alexfarma.sgq.api.repository.filter;

public class DepartamentoFilter {

	private Long codigo;

	private String nome;

	private Long processoPadrao;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getProcessoPadrao() {
		return processoPadrao;
	}

	public void setProcessoPadrao(Long processoPadrao) {
		this.processoPadrao = processoPadrao;
	}

}
