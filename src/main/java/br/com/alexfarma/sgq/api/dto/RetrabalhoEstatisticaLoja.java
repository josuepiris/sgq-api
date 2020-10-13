package br.com.alexfarma.sgq.api.dto;

public class RetrabalhoEstatisticaLoja {

	private Long codigoDepartamento;

	private Long totalRetrabalhos;

	public RetrabalhoEstatisticaLoja(Long codigoDepartamento, Long totalRetrabalhos) {
		this.codigoDepartamento = codigoDepartamento;
		this.totalRetrabalhos = totalRetrabalhos;
	}

	public Long getCodigoDepartamento() {
		return codigoDepartamento;
	}

	public void setCodigoDepartamento(Long codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	public Long getTotalRetrabalhos() {
		return totalRetrabalhos;
	}

	public void setTotalRetrabalhos(Long totalRetrabalhos) {
		this.totalRetrabalhos = totalRetrabalhos;
	}	

}
