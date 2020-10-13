package br.com.alexfarma.sgq.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alexfarma.sgq.api.model.MotivoRetrabalho;
import br.com.alexfarma.sgq.api.repository.filter.MotivoRetrabalhoFilter;

public interface MotivoRetrabalhoRepository extends JpaRepository<MotivoRetrabalho, Long> {

	public List<MotivoRetrabalho> findByProcessoCodigo(Long codigoProcesso);

	public Page<MotivoRetrabalho> filtrar(MotivoRetrabalhoFilter filtros, Pageable pageable);

}
