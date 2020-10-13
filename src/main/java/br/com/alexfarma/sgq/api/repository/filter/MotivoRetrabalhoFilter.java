package br.com.alexfarma.sgq.api.repository.filter;

public class MotivoRetrabalhoFilter {

	private Long codigo;

	private Long processo;

	private String descricao;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getProcesso() {
		return processo;
	}

	public void setProcesso(Long processo) {
		this.processo = processo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
