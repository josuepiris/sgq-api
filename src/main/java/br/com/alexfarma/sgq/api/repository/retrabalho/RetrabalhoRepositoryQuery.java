package br.com.alexfarma.sgq.api.repository.retrabalho;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatistica;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaDia;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaFuncionario;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaLoja;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaMotivo;
import br.com.alexfarma.sgq.api.repository.filter.DashboardFilter;
import br.com.alexfarma.sgq.api.repository.filter.RetrabalhoFilter;
import br.com.alexfarma.sgq.api.repository.projection.ResumoRetrabalho;

public interface RetrabalhoRepositoryQuery {

	public Page<ResumoRetrabalho> resumir(RetrabalhoFilter retrabalhoFilter, Pageable pageable);

	public List<RetrabalhoEstatistica> gerarEstatisticasRetrabalho(RetrabalhoFilter filtros);
	public List<RetrabalhoEstatisticaDia> porDia(LocalDate mesReferencia, Long codigoProcesso);
	public List<RetrabalhoEstatisticaMotivo> porMotivo(DashboardFilter filtros);
	public List<RetrabalhoEstatisticaLoja> totalRetrabalhosPorLoja(LocalDate dataRegistroDe, LocalDate dataRegistroAte);
	public List<RetrabalhoEstatisticaFuncionario> totalRetrabalhosPorFuncionario(RetrabalhoFilter filtros);

	public Long total(RetrabalhoFilter filtros);

}
