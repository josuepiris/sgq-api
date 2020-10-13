package br.com.alexfarma.sgq.api.dto;

public class RetrabalhoEstatisticaFuncionario {

	private Long codigoFuncionario;

	private String nomeFuncionario;

	private Long totalRetrabalhos;

	public RetrabalhoEstatisticaFuncionario(Long codigoFuncionario, String nomeFuncionario, Long totalRetrabalhos) {
		this.codigoFuncionario = codigoFuncionario;
		this.nomeFuncionario = nomeFuncionario;
		this.totalRetrabalhos = totalRetrabalhos;
	}

	public Long getCodigoFuncionario() {
		return codigoFuncionario;
	}

	public void setCodigoFuncionario(Long codigoFuncionario) {
		this.codigoFuncionario = codigoFuncionario;
	}

	public String getNomeFuncionario() {
		return nomeFuncionario;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}

	public Long getTotalRetrabalhos() {
		return totalRetrabalhos;
	}

	public void setTotalRetrabalhos(Long totalRetrabalhos) {
		this.totalRetrabalhos = totalRetrabalhos;
	}

}
