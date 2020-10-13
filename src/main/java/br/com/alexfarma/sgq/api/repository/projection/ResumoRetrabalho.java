package br.com.alexfarma.sgq.api.repository.projection;

import java.time.LocalDateTime;

public class ResumoRetrabalho {

	private Long codigo;
	private Long codigoDptoFuncionarioRegistro;
	private Boolean erroCritico;
	private Boolean ignorarDivergencias;
	private String numeroProtocolo;
	private String numeroFormula;
	private String motivo;
	private String funcionario;
	private String divergencias;
	private LocalDateTime dataHoraCriacao;

	public ResumoRetrabalho(Long codigo, Long codigoDptoFuncionarioRegistro, Boolean erroCritico, Boolean ignorarDivergencias,
			String numeroProtocolo, String numeroFormula, String motivo, String funcionario, String divergencias, LocalDateTime dataHoraCriacao) {
		this.codigo = codigo;
		this.codigoDptoFuncionarioRegistro = codigoDptoFuncionarioRegistro;
		this.erroCritico = erroCritico;
		this.ignorarDivergencias = ignorarDivergencias;
		this.numeroProtocolo = numeroProtocolo;
		this.numeroFormula = numeroFormula;
		this.motivo = motivo;
		this.funcionario = funcionario;
		this.divergencias = divergencias;
		this.dataHoraCriacao = dataHoraCriacao;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCodigoDptoFuncionarioRegistro() {
		return codigoDptoFuncionarioRegistro;
	}

	public void setCodigoDptoFuncionarioRegistro(Long codigoDptoFuncionarioRegistro) {
		this.codigoDptoFuncionarioRegistro = codigoDptoFuncionarioRegistro;
	}

	public Boolean getErroCritico() {
		return erroCritico;
	}

	public void setErroCritico(Boolean erroCritico) {
		this.erroCritico = erroCritico;
	}

	public Boolean getIgnorarDivergencias() {
		return ignorarDivergencias;
	}

	public void setIgnorarDivergencias(Boolean ignorarDivergencias) {
		this.ignorarDivergencias = ignorarDivergencias;
	}

	public String getNumeroProtocolo() {
		return numeroProtocolo;
	}

	public void setNumeroProtocolo(String numeroProtocolo) {
		this.numeroProtocolo = numeroProtocolo;
	}

	public String getNumeroFormula() {
		return numeroFormula;
	}

	public void setNumeroFormula(String numeroFormula) {
		this.numeroFormula = numeroFormula;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}

	public String getDivergencias() {
		return divergencias;
	}

	public void setDivergencias(String divergencias) {
		this.divergencias = divergencias;
	}

	public LocalDateTime getDataHoraCriacao() {
		return dataHoraCriacao;
	}

	public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
		this.dataHoraCriacao = dataHoraCriacao;
	}

}
