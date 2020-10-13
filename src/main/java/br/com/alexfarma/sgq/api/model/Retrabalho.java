package br.com.alexfarma.sgq.api.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Retrabalho {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotNull
	private String protocolo;

	@NotNull
	private Long numeroFormula;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_processo")
	private Processo processo;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_motivo")
	private MotivoRetrabalho motivoRetrabalho;

	@JsonIgnoreProperties({"departamento", "ativo", "email", "senha", "permissao"})
	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_funcionario")
	private Funcionario funcionario;

	@JsonIgnoreProperties("processoPadrao")
	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_departamento")
	private Departamento departamento;

	private String observacao;

	@JsonIgnoreProperties({"departamento", "ativo", "email", "senha", "permissao"})
	@NotNull
	@ManyToOne
	@JoinColumn(name = "funcionario_registro")
	private Funcionario funcionarioRegistro;

	@JsonIgnoreProperties({"departamento", "ativo", "email", "senha", "permissao"})
	@ManyToOne
	@JoinColumn(name = "funcionario_alteracao")
	private Funcionario funcionarioAlteracao;

	private String divergencias;

	@NotNull
	private Boolean ignorarDivergencias;

	@NotNull
	private Boolean ignorarDuplicado;

	@Column(name = "data_hora_criacao", insertable = false, updatable = false)
	private LocalDateTime dataHoraCriacao;

	@Column(name = "data_hora_alteracao", insertable = false, updatable = false)
	private LocalDateTime dataHoraAlteracao;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public Long getNumeroFormula() {
		return numeroFormula;
	}

	public void setNumeroFormula(Long numeroFormula) {
		this.numeroFormula = numeroFormula;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public MotivoRetrabalho getMotivoRetrabalho() {
		return motivoRetrabalho;
	}

	public void setMotivoRetrabalho(MotivoRetrabalho motivoRetrabalho) {
		this.motivoRetrabalho = motivoRetrabalho;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Funcionario getFuncionarioRegistro() {
		return funcionarioRegistro;
	}

	public void setFuncionarioRegistro(Funcionario funcionarioRegistro) {
		this.funcionarioRegistro = funcionarioRegistro;
	}

	public Funcionario getFuncionarioAlteracao() {
		return funcionarioAlteracao;
	}

	public void setFuncionarioAlteracao(Funcionario funcionarioAlteracao) {
		this.funcionarioAlteracao = funcionarioAlteracao;
	}

	public String getDivergencias() {
		return divergencias;
	}

	public void setDivergencias(String divergencias) {
		this.divergencias = divergencias;
	}

	public Boolean getIgnorarDivergencias() {
		return ignorarDivergencias;
	}

	public void setIgnorarDivergencias(Boolean ignorarDivergencias) {
		this.ignorarDivergencias = ignorarDivergencias;
	}

	public Boolean getIgnorarDuplicado() {
		return ignorarDuplicado;
	}

	public void setIgnorarDuplicado(Boolean ignorarDuplicado) {
		this.ignorarDuplicado = ignorarDuplicado;
	}

	public LocalDateTime getDataHoraCriacao() {
		return dataHoraCriacao;
	}

	public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
		this.dataHoraCriacao = dataHoraCriacao;
	}

	public LocalDateTime getDataHoraAlteracao() {
		return dataHoraAlteracao;
	}

	public void setDataHoraAlteracao(LocalDateTime dataHoraAlteracao) {
		this.dataHoraAlteracao = dataHoraAlteracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Retrabalho other = (Retrabalho) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
