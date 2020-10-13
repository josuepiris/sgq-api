package br.com.alexfarma.sgq.api.repository.retrabalho;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatistica;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaDia;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaFuncionario;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaLoja;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaMotivo;
import br.com.alexfarma.sgq.api.model.Departamento_;
import br.com.alexfarma.sgq.api.model.Funcionario_;
import br.com.alexfarma.sgq.api.model.MotivoRetrabalho_;
import br.com.alexfarma.sgq.api.model.Processo_;
import br.com.alexfarma.sgq.api.model.Retrabalho;
import br.com.alexfarma.sgq.api.model.Retrabalho_;
import br.com.alexfarma.sgq.api.repository.filter.DashboardFilter;
import br.com.alexfarma.sgq.api.repository.filter.RetrabalhoFilter;
import br.com.alexfarma.sgq.api.repository.projection.ResumoRetrabalho;

public class RetrabalhoRepositoryImpl implements RetrabalhoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<ResumoRetrabalho> resumir(RetrabalhoFilter retrabalhoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoRetrabalho> criteria = builder.createQuery(ResumoRetrabalho.class);
		Root<Retrabalho> root = criteria.from(Retrabalho.class);

		criteria.select(
				builder.construct(ResumoRetrabalho.class, root.get(Retrabalho_.codigo),
						root.get(Retrabalho_.funcionarioRegistro)
							.get(Funcionario_.DEPARTAMENTO).get(Departamento_.CODIGO),
						root.get(Retrabalho_.motivoRetrabalho).get(MotivoRetrabalho_.ERRO_CRITICO),
						root.get(Retrabalho_.IGNORAR_DIVERGENCIAS),
						root.get(Retrabalho_.protocolo),
						root.get(Retrabalho_.NUMERO_FORMULA).as(String.class),
						root.get(Retrabalho_.motivoRetrabalho).get(MotivoRetrabalho_.descricao),
						root.get(Retrabalho_.funcionario).get(Funcionario_.nome),
						root.get(Retrabalho_.DIVERGENCIAS),
						root.get(Retrabalho_.dataHoraCriacao)));

		// restrições
		Predicate[] predicates = criarRestricoesRetrabalhoFilter(retrabalhoFilter, builder, root);
		criteria.where(predicates);

		criteria.orderBy(builder.desc(root.get(Retrabalho_.dataHoraCriacao)));

		TypedQuery<ResumoRetrabalho> query = manager.createQuery(criteria);

		// nº de máximo de registros por página e página atual
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(retrabalhoFilter));

	}

	@Override
	public List<RetrabalhoEstatistica> gerarEstatisticasRetrabalho(RetrabalhoFilter filtros) {

			CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
			CriteriaQuery<RetrabalhoEstatistica> criteriaQuery = criteriaBuilder.createQuery(RetrabalhoEstatistica.class);

			Root<Retrabalho> root = criteriaQuery.from(Retrabalho.class);

			criteriaQuery.select(criteriaBuilder.construct(RetrabalhoEstatistica.class,
					root.get(Retrabalho_.motivoRetrabalho).get(MotivoRetrabalho_.descricao),
					root.get(Retrabalho_.departamento).get(Departamento_.nome),
					root.get(Retrabalho_.funcionario).get(Funcionario_.nome),
					criteriaBuilder.count(root.get(Retrabalho_.motivoRetrabalho)))
			);

			// restrições
			Predicate[] predicates = criarRestricoesRetrabalhoFilter(filtros, criteriaBuilder, root);
			criteriaQuery.where(predicates);

			criteriaQuery.groupBy(root.get(Retrabalho_.funcionario),
					root.get(Retrabalho_.departamento),
					root.get(Retrabalho_.motivoRetrabalho));

			criteriaQuery.orderBy(criteriaBuilder.asc(root.get(Retrabalho_.funcionario).get(Funcionario_.nome)),
					criteriaBuilder.asc(root.get(Retrabalho_.departamento).get(Departamento_.nome)),
					criteriaBuilder.desc(criteriaBuilder.count(root.get(Retrabalho_.motivoRetrabalho))));

			TypedQuery<RetrabalhoEstatistica> typedQuery = manager.createQuery(criteriaQuery);
			return typedQuery.getResultList();

	}

	@Override
	public List<RetrabalhoEstatisticaDia> porDia(LocalDate mesReferencia, Long codigoProcesso) {

		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDateTime dtPrimeiroDia = primeiroDia.atTime(0, 0);

		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		LocalDateTime dtUltimoDia = ultimoDia.atTime(23, 59, 59, 999999999);

		TypedQuery<RetrabalhoEstatisticaDia> query = manager.createQuery(
				"SELECT new br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaDia " +
						"(DATE(r.dataHoraCriacao) AS data_criacao, COUNT(*)) " +
						"FROM Retrabalho r " +
						"WHERE r.dataHoraCriacao >= :pDataIntervaloInicial AND r.dataHoraCriacao <= :pDataIntervaloFinal " +
							"AND r.processo.codigo = :pCodigoProcesso " +
						"GROUP BY DATE(r.dataHoraCriacao)", RetrabalhoEstatisticaDia.class);
		query.setParameter("pDataIntervaloInicial", dtPrimeiroDia);
		query.setParameter("pDataIntervaloFinal", dtUltimoDia);
		query.setParameter("pCodigoProcesso", codigoProcesso);

		return query.getResultList();

	}

	@Override
	public List<RetrabalhoEstatisticaMotivo> porMotivo(DashboardFilter filtros) {

		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<RetrabalhoEstatisticaMotivo> criteriaQuery = criteriaBuilder.createQuery(RetrabalhoEstatisticaMotivo.class);

		Root<Retrabalho> root = criteriaQuery.from(Retrabalho.class);

		// SELECT codigo_motivo,
		criteriaQuery.select(criteriaBuilder.construct(RetrabalhoEstatisticaMotivo.class,
				root.get(Retrabalho_.motivoRetrabalho).get(MotivoRetrabalho_.descricao),
				criteriaBuilder.count(root.get(Retrabalho_.motivoRetrabalho)))
		);

		Predicate[] predicates = criarRestricoesDashboardFilter(filtros, criteriaBuilder, root);

		// Cláusula "WHERE";
		criteriaQuery.where(predicates);
		criteriaQuery.groupBy(root.get(Retrabalho_.motivoRetrabalho));
		criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.count(root.get(Retrabalho_.motivoRetrabalho))));

		TypedQuery<RetrabalhoEstatisticaMotivo> typedQuery = manager.createQuery(criteriaQuery);
		return typedQuery.getResultList();

	}

	@Override
	public List<RetrabalhoEstatisticaLoja> totalRetrabalhosPorLoja(LocalDate dataRegistroDe, LocalDate dataRegistroAte) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RetrabalhoEstatisticaLoja> criteria = builder.createQuery(RetrabalhoEstatisticaLoja.class);
		Root<Retrabalho> root = criteria.from(Retrabalho.class);

		criteria.select(builder.construct(RetrabalhoEstatisticaLoja.class,
				root.get(Retrabalho_.DEPARTAMENTO).get(Departamento_.CODIGO),
				builder.count(root)));

		// restrições
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.greaterThanOrEqualTo(root.get(Retrabalho_.dataHoraCriacao),
				dataRegistroDe.atTime(0, 0)));

		predicates.add(builder.lessThanOrEqualTo(root.get(Retrabalho_.dataHoraCriacao),
				dataRegistroAte.atTime(23, 59, 59, 999999999)));

		criteria.where(predicates.toArray(new Predicate[predicates.size()]));

		criteria.groupBy(root.get(Retrabalho_.DEPARTAMENTO));

		TypedQuery<RetrabalhoEstatisticaLoja> query = manager.createQuery(criteria);

		return query.getResultList();
	}

	@Override
	public List<RetrabalhoEstatisticaFuncionario> totalRetrabalhosPorFuncionario(RetrabalhoFilter filtros) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RetrabalhoEstatisticaFuncionario> criteria = builder.createQuery(RetrabalhoEstatisticaFuncionario.class);
		Root<Retrabalho> root = criteria.from(Retrabalho.class);

		criteria.select(builder.construct(RetrabalhoEstatisticaFuncionario.class,
				root.get(Retrabalho_.FUNCIONARIO).get(Funcionario_.FUNCIONARIO_ID),
				root.get(Retrabalho_.FUNCIONARIO).get(Funcionario_.NOME),
				builder.count(root)));

		// restrições
		Predicate[] predicates = criarRestricoesRetrabalhoFilter(filtros, builder, root);
		criteria.where(predicates);

		criteria.groupBy(root.get(Retrabalho_.FUNCIONARIO));

		TypedQuery<RetrabalhoEstatisticaFuncionario> query = manager.createQuery(criteria);

		return query.getResultList();
	}

	public Long total(RetrabalhoFilter lancamentoFilter) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Retrabalho> root = criteria.from(Retrabalho.class);

		// restrições
		Predicate[] predicates = criarRestricoesRetrabalhoFilter(lancamentoFilter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();

	}

	private Predicate[] criarRestricoesRetrabalhoFilter(RetrabalhoFilter retrabalhoFilter, CriteriaBuilder builder,
			Root<Retrabalho> root) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (retrabalhoFilter.getCodigo() != null) {
			predicates.add(builder.equal(root.get(Retrabalho_.CODIGO), retrabalhoFilter.getCodigo()));
		}

		if (retrabalhoFilter.getDataRegistroDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Retrabalho_.dataHoraCriacao),
					retrabalhoFilter.getDataRegistroDe().atTime(0, 0)));
		}

		if (retrabalhoFilter.getDataRegistroAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Retrabalho_.dataHoraCriacao),
					retrabalhoFilter.getDataRegistroAte().atTime(23, 59, 59, 999999999)));
		}

		if (!StringUtils.isEmpty(retrabalhoFilter.getProtocolo())) {
			predicates.add(builder.like(root.get(Retrabalho_.protocolo), retrabalhoFilter.getProtocolo()));
		}

		if (retrabalhoFilter.getProcesso() != null) {
			predicates.add(builder.equal(root.get(Retrabalho_.processo).get(Processo_.codigo),
					retrabalhoFilter.getProcesso()));
		}

		if (retrabalhoFilter.getMotivo() != null) {
			predicates.add(builder.equal(root.get(Retrabalho_.motivoRetrabalho).get(MotivoRetrabalho_.codigo),
					retrabalhoFilter.getMotivo()));
		}

		if (retrabalhoFilter.getDepartamento() != null) {
			predicates.add(builder.equal(root.get(Retrabalho_.departamento).get(Departamento_.codigo),
					retrabalhoFilter.getDepartamento()));
		}

		if (retrabalhoFilter.getFuncionario() != null) {
			predicates.add(builder.equal(root.get(Retrabalho_.funcionario).get(Funcionario_.funcionarioId),
					retrabalhoFilter.getFuncionario()));
		}

		if (retrabalhoFilter.getFuncionarioRegistro() != null) {
			predicates.add(builder.equal(root.get(Retrabalho_.funcionarioRegistro).get(Funcionario_.funcionarioId),
					retrabalhoFilter.getFuncionarioRegistro()));
		}

		if (retrabalhoFilter.getErroCritico()) {
			predicates.add(builder.isTrue(root.get(Retrabalho_.MOTIVO_RETRABALHO).get(MotivoRetrabalho_.ERRO_CRITICO)));
		}

		return predicates.toArray(new Predicate[predicates.size()]);

	}

	private Predicate[] criarRestricoesDashboardFilter(DashboardFilter dashboardFilter, CriteriaBuilder builder,
			Root<Retrabalho> root) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		LocalDate mesReferencia = dashboardFilter.getMesReferencia();

		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDateTime dtPrimeiroDia = primeiroDia.atTime(0, 0);

		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		LocalDateTime dtUltimoDia = ultimoDia.atTime(23, 59, 59, 999999999);
		
		predicates.add(builder.greaterThanOrEqualTo(root.get(Retrabalho_.dataHoraCriacao), dtPrimeiroDia));
		predicates.add(builder.lessThanOrEqualTo(root.get(Retrabalho_.dataHoraCriacao), dtUltimoDia));

		if (dashboardFilter.getProcesso() != null) {
			predicates.add(builder.equal(root.get(Retrabalho_.processo).get(Processo_.codigo),
					dashboardFilter.getProcesso()));
		}

		if (dashboardFilter.getDepartamento() != null) {
			predicates.add(builder.equal(root.get(Retrabalho_.departamento).get(Departamento_.codigo),
					dashboardFilter.getDepartamento()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);

	}

	// Métodos privados s/ retorno
	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {

		int paginaAtual = pageable.getPageNumber();
		int totalRegistroPorPagina = pageable.getPageSize();

		// Ex.: Exibindo 3 registros por página, se a página atual for igual a 2, o
		// primeiro registro será o de nº 6.
		int primeiroRegistroDaPagina = paginaAtual * totalRegistroPorPagina;

		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistroPorPagina);

	}

}
