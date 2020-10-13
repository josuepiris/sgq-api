package br.com.alexfarma.sgq.api.repository.retrabalho_motivo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alexfarma.sgq.api.model.MotivoRetrabalho;
import br.com.alexfarma.sgq.api.repository.filter.MotivoRetrabalhoFilter;

public interface MotivoRetrabalhoRepositoryQuery {

	public Page<MotivoRetrabalho> filtrar(MotivoRetrabalhoFilter filtros, Pageable pageable);

}
