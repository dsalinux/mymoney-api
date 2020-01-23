package br.com.professordanilo.mymoney.api.repository.lancamento;

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

import br.com.professordanilo.mymoney.api.model.Lancamento;
import br.com.professordanilo.mymoney.api.repository.filter.LancamentoFilter;
import br.com.professordanilo.mymoney.api.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter filter, Pageable pageable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(filter, builder, root);
		
		criteria.where(predicates);
		TypedQuery<Lancamento> query = entityManager.createQuery(criteria);
		
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter filter, Pageable pageable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
//		criteria.select(builder.construct(ResumoLancamento.class, root.get(Lancamento_.codigo),
//				root.get(Lancamento_.descricao),
//				root.get(Lancamento_.dataVencimento),
//				root.get(Lancamento_.dataPagamento),
//				root.get(Lancamento_.valor),
//				root.get(Lancamento_.tipo),
//				root.get(Lancamento_.categoria).get(Categoria_.nome),
//				root.get(Lancamento_.pessoa).get(Pessoa_.nome)
//				));
		criteria.select(builder.construct(ResumoLancamento.class, root.get("codigo"),
				root.get("descricao"),
				root.get("dataVencimento"),
				root.get("dataPagamento"),
				root.get("valor"),
				root.get("tipo"),
				root.get("categoria").get("nome"),
				root.get("pessoa").get("nome")
				));
		Predicate[] predicates = criarRestricoes(filter, builder, root);
		criteria.where(predicates);
		TypedQuery<ResumoLancamento> query = entityManager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}

	private Predicate[] criarRestricoes(LancamentoFilter filter, CriteriaBuilder builder, Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(!StringUtils.isEmpty(filter.getDescricao())){
			predicates.add(builder.like(builder.lower(root.get("descricao")), "%"+filter.getDescricao().toLowerCase()+"%"));
		}
		if(filter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"), filter.getDataVencimentoDe()));
		}
		if(filter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataVencimento"), filter.getDataVencimentoAte()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int maxResult = pageable.getPageSize();
		int startPosition = paginaAtual * maxResult;
		
		query.setFirstResult(startPosition);
		query.setMaxResults(maxResult);
		
	}
	
	private Long total(LancamentoFilter filter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(filter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return entityManager.createQuery(criteria).getSingleResult();
	}
}
