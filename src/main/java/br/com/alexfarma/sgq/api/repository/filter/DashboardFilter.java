package br.com.alexfarma.sgq.api.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class DashboardFilter {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate mesReferencia;

	private Long processo;

	private Long departamento;

	public LocalDate getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(LocalDate mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public Long getProcesso() {
		return processo;
	}

	public void setProcesso(Long processo) {
		this.processo = processo;
	}

	public Long getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Long departamento) {
		this.departamento = departamento;
	}

}
