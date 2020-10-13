package br.com.alexfarma.sgq.api.repository.funcionario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alexfarma.sgq.api.model.Funcionario;
import br.com.alexfarma.sgq.api.repository.filter.FuncionarioFilter;

public interface FuncionarioRepositoryQuery {
	
	public Page<Funcionario> filtrar(FuncionarioFilter filtros, Pageable pageable);

}
