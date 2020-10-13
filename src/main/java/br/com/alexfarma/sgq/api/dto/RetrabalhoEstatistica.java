package br.com.alexfarma.sgq.api.dto;

public class RetrabalhoEstatistica {

	private String motivoRetrabalho;
	
	private String departamento;

	private String nomeFuncionario;

	private Long totalRegistrosMotivo;

	public RetrabalhoEstatistica(String motivoRetrabalho, String departamento, String nomeFuncionario, Long totalRegistrosMotivo) {
		this.motivoRetrabalho = motivoRetrabalho;
		this.departamento = departamento;
		this.nomeFuncionario = nomeFuncionario;
		this.totalRegistrosMotivo = totalRegistrosMotivo;
	}

	public String getMotivoRetrabalho() {
		return motivoRetrabalho;
	}

	public void setMotivoRetrabalho(String motivoRetrabalho) {
		this.motivoRetrabalho = motivoRetrabalho;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getNomeFuncionario() {
		return nomeFuncionario;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}

	public Long getTotalRegistrosMotivo() {
		return totalRegistrosMotivo;
	}

	public void setTotalRegistrosMotivo(Long totalRegistrosMotivo) {
		this.totalRegistrosMotivo = totalRegistrosMotivo;
	}

}
