package br.com.professordanilo.mymoney.api.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.professordanilo.mymoney.api.model.Lancamento;
import br.com.professordanilo.mymoney.api.repository.filter.LancamentoFilter;
import br.com.professordanilo.mymoney.api.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	Page<Lancamento> filtrar(LancamentoFilter filter, Pageable pageable);
	Page<ResumoLancamento> resumir(LancamentoFilter filter, Pageable pageable);
}
