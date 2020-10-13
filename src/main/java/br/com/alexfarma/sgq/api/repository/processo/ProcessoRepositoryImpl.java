package br.com.alexfarma.sgq.api.repository.processo;

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

import br.com.alexfarma.sgq.api.model.Processo;
import br.com.alexfarma.sgq.api.model.Processo_;
import br.com.alexfarma.sgq.api.repository.filter.ProcessoFilter;

public class ProcessoRepositoryImpl implements ProcessoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	public Page<Processo> filtrar(ProcessoFilter filtros, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Processo> criteria = builder.createQuery(Processo.class);
		Root<Processo> root = criteria.from(Processo.class);

		// restrições
		Predicate[] predicates = criarRestricoes(filtros, builder, root);
		criteria.where(predicates);

		TypedQuery<Processo> query = manager.createQuery(criteria);

		// nº de máximo de registros por página e página atual
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(filtros));

	}

	private Predicate[] criarRestricoes(ProcessoFilter filtros, CriteriaBuilder builder,
			Root<Processo> root) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (filtros.getCodigo() != null) {
			predicates.add(builder.equal(root.get(Processo_.CODIGO), filtros.getCodigo()));
		}

		if (!StringUtils.isEmpty(filtros.getDescricao())) {
			predicates.add(builder.like(root.get(Processo_.DESCRICAO), "%" + filtros.getDescricao() + "%"));
		}

		return predicates.toArray(new Predicate[predicates.size()]);

	}

	public Long total(ProcessoFilter filtros) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Processo> root = criteria.from(Processo.class);

		// restrições
		Predicate[] predicates = criarRestricoes(filtros, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();

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
