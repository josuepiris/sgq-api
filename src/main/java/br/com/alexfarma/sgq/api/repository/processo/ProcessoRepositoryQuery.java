package br.com.alexfarma.sgq.api.repository.processo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alexfarma.sgq.api.model.Processo;
import br.com.alexfarma.sgq.api.repository.filter.ProcessoFilter;

public interface ProcessoRepositoryQuery {
	
	public Page<Processo> filtrar(ProcessoFilter filtros, Pageable pageable);

}
