package br.com.alexfarma.sgq.api.dto;

import java.util.Date;

public class RetrabalhoEstatisticaDia {

	private Date data;

	private Long total;

	public RetrabalhoEstatisticaDia(Date data, Long total) {
		this.data = data;
		this.total = total;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

}
