package br.com.alexfarma.sgq.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.com.alexfarma.sgq.api.dto.PrimeiroAndUltimoRegistro;
import br.com.alexfarma.sgq.api.model.Retrabalho;
import br.com.alexfarma.sgq.api.repository.projection.RegistroRetrabalhoDuplicado;
import br.com.alexfarma.sgq.api.repository.retrabalho.RetrabalhoRepositoryQuery;

public interface RetrabalhoRepository extends JpaRepository<Retrabalho, Long>, RetrabalhoRepositoryQuery {

	public boolean existsRetrabalhoByProtocoloAndNumeroFormulaAndMotivoRetrabalhoCodigo(
			String protocolo, Long numeroFormula, Long codigoMotivoRetrabalho);

	public PrimeiroAndUltimoRegistro buscarPrimeiroAndUltimoRegistro();

	// Named query implementada em arquivo externo (orm.xml);
	public List<RegistroRetrabalhoDuplicado> buscarRegistrosDuplicados(@Param("codigoDepartamento") Long codigoDepartamento);

	public List<Retrabalho> findByDepartamentoCodigoAndDataHoraCriacaoBetween(Long codigoDepartamento, LocalDateTime dataCriacao1, LocalDateTime dataCriacao2);

}
