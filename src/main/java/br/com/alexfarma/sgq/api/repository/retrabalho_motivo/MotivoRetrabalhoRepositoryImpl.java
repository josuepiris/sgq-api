package br.com.alexfarma.sgq.api.repository.retrabalho_motivo;

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

import br.com.alexfarma.sgq.api.model.MotivoRetrabalho;
import br.com.alexfarma.sgq.api.model.MotivoRetrabalho_;
import br.com.alexfarma.sgq.api.model.Processo_;
import br.com.alexfarma.sgq.api.repository.filter.MotivoRetrabalhoFilter;

public class MotivoRetrabalhoRepositoryImpl implements MotivoRetrabalhoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	public Page<MotivoRetrabalho> filtrar(MotivoRetrabalhoFilter filtros, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<MotivoRetrabalho> criteria = builder.createQuery(MotivoRetrabalho.class);
		Root<MotivoRetrabalho> root = criteria.from(MotivoRetrabalho.class);

		// restrições
		Predicate[] predicates = criarRestricoes(filtros, builder, root);
		criteria.where(predicates);

		TypedQuery<MotivoRetrabalho> query = manager.createQuery(criteria);

		// nº de máximo de registros por página e página atual
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(filtros));

	}

	private Predicate[] criarRestricoes(MotivoRetrabalhoFilter filtros, CriteriaBuilder builder,
			Root<MotivoRetrabalho> root) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (filtros.getCodigo() != null) {
			predicates.add(builder.equal(root.get(MotivoRetrabalho_.CODIGO), filtros.getCodigo()));
		}

		if (filtros.getProcesso() != null) {
			predicates.add(builder.equal(root.get(MotivoRetrabalho_.PROCESSO).get(Processo_.CODIGO), 
					filtros.getProcesso()));
		}

		if (!StringUtils.isEmpty(filtros.getDescricao())) {
			predicates.add(builder.like(root.get(MotivoRetrabalho_.DESCRICAO), "%" + filtros.getDescricao() + "%"));
		}

		return predicates.toArray(new Predicate[predicates.size()]);

	}

	public Long total(MotivoRetrabalhoFilter filtros) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<MotivoRetrabalho> root = criteria.from(MotivoRetrabalho.class);

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
