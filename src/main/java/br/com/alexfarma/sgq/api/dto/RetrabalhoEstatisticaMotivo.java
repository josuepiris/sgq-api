package br.com.alexfarma.sgq.api.dto;

public class RetrabalhoEstatisticaMotivo {
	
	private String motivo;
	private Long total;
	
	public RetrabalhoEstatisticaMotivo(String motivo, Long total) {
		this.motivo = motivo;
		this.total = total;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

}
