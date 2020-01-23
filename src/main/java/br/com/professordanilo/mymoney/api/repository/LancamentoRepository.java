package br.com.professordanilo.mymoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.professordanilo.mymoney.api.model.Lancamento;
import br.com.professordanilo.mymoney.api.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{

	
	
}
