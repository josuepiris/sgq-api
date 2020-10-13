package br.com.alexfarma.sgq.api.repository.funcionario;

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

import br.com.alexfarma.sgq.api.model.Departamento_;
import br.com.alexfarma.sgq.api.model.Funcionario;
import br.com.alexfarma.sgq.api.model.Funcionario_;
import br.com.alexfarma.sgq.api.repository.filter.FuncionarioFilter;

public class FuncionarioRepositoryImpl implements FuncionarioRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	public Page<Funcionario> filtrar(FuncionarioFilter filtros, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Funcionario> criteria = builder.createQuery(Funcionario.class);
		Root<Funcionario> root = criteria.from(Funcionario.class);

		// restrições
		Predicate[] predicates = criarRestricoes(filtros, builder, root);
		criteria.where(predicates);

		TypedQuery<Funcionario> query = manager.createQuery(criteria);

		// nº de máximo de registros por página e página atual
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(filtros));

	}

	private Predicate[] criarRestricoes(FuncionarioFilter filtros, CriteriaBuilder builder,
			Root<Funcionario> root) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (filtros.getCodigo() != null) {
			predicates.add(builder.equal(root.get(Funcionario_.FUNCIONARIO_ID), filtros.getCodigo()));
		}

		if (filtros.getDepartamento() != null) {
			predicates.add(builder.equal(root.get(Funcionario_.DEPARTAMENTO).get(Departamento_.CODIGO),
					filtros.getDepartamento()));
		}

		if (!StringUtils.isEmpty(filtros.getNome())) {
			predicates.add(builder.like(root.get(Funcionario_.NOME), "%" + filtros.getNome() + "%"));
		}

		return predicates.toArray(new Predicate[predicates.size()]);

	}

	public Long total(FuncionarioFilter filtros) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Funcionario> root = criteria.from(Funcionario.class);

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
