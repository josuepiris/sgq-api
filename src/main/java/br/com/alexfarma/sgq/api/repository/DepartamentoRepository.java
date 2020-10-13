package br.com.alexfarma.sgq.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alexfarma.sgq.api.model.Departamento;
import br.com.alexfarma.sgq.api.repository.filter.DepartamentoFilter;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long>{

	public List<Departamento> findByAtivoTrue();
	public Page<Departamento> filtrar(DepartamentoFilter filtros, Pageable pageable);

}
