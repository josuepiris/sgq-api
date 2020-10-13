package br.com.alexfarma.sgq.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alexfarma.sgq.api.model.Processo;
import br.com.alexfarma.sgq.api.repository.filter.ProcessoFilter;

public interface ProcessoRepository extends JpaRepository<Processo, Long>{

	public Page<Processo> filtrar(ProcessoFilter filtros, Pageable pageable);

}
