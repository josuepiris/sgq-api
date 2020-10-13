package br.com.alexfarma.sgq.api.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Processo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoProcesso tipoProcesso;

	@NotNull
	private String descricao;

	private Long codigoMapeamento;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public TipoProcesso getTipoProcesso() {
		return tipoProcesso;
	}

	public void setTipoProcesso(TipoProcesso tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getCodigoMapeamento() {
		return codigoMapeamento;
	}

	public void setCodigoMapeamento(Long codigoMapeamento) {
		this.codigoMapeamento = codigoMapeamento;
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
		Processo other = (Processo) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
