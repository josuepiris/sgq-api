package br.com.alexfarma.sgq.api.repository.departamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alexfarma.sgq.api.model.Departamento;
import br.com.alexfarma.sgq.api.repository.filter.DepartamentoFilter;

public interface DepartamentoRepositoryQuery {
	
	public Page<Departamento> filtrar(DepartamentoFilter filtros, Pageable pageable);

}
