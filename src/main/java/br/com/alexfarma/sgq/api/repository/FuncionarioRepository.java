package br.com.alexfarma.sgq.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alexfarma.sgq.api.model.Funcionario;
import br.com.alexfarma.sgq.api.repository.filter.FuncionarioFilter;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

	public Page<Funcionario> filtrar(FuncionarioFilter filtros, Pageable pageable);

	public List<Funcionario> findByAtivoTrue();
	public List<Funcionario> findByDepartamentoCodigo(Long codigoDepartamento);

}
