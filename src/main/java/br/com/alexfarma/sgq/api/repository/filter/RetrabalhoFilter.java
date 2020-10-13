package br.com.alexfarma.sgq.api.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class RetrabalhoFilter {

	private Long codigo;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataRegistroDe;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataRegistroAte;

	private String protocolo;

	private Long processo;

	private Long motivo;

	private Long departamento;

	private Long funcionario;

	private Long funcionarioRegistro;

	private Boolean erroCritico = false;

	public RetrabalhoFilter() {
	}

	public RetrabalhoFilter(LocalDate dataRegistroDe, LocalDate dataRegistroAte, Long processo) {
		this.dataRegistroDe = dataRegistroDe;
		this.dataRegistroAte = dataRegistroAte;
		this.processo = processo;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDate getDataRegistroDe() {
		return dataRegistroDe;
	}

	public void setDataRegistroDe(LocalDate dataRegistroDe) {
		this.dataRegistroDe = dataRegistroDe;
	}

	public LocalDate getDataRegistroAte() {
		return dataRegistroAte;
	}

	public void setDataRegistroAte(LocalDate dataRegistroAte) {
		this.dataRegistroAte = dataRegistroAte;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public Long getProcesso() {
		return processo;
	}

	public void setProcesso(Long processo) {
		this.processo = processo;
	}

	public Long getMotivo() {
		return motivo;
	}

	public void setMotivo(Long motivo) {
		this.motivo = motivo;
	}

	public Long getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Long departamento) {
		this.departamento = departamento;
	}

	public Long getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Long funcionario) {
		this.funcionario = funcionario;
	}

	public Long getFuncionarioRegistro() {
		return funcionarioRegistro;
	}

	public void setFuncionarioRegistro(Long funcionarioRegistro) {
		this.funcionarioRegistro = funcionarioRegistro;
	}

	public Boolean getErroCritico() {
		return erroCritico;
	}

	public void setErroCritico(Boolean erroCritico) {
		this.erroCritico = erroCritico;
	}

}
