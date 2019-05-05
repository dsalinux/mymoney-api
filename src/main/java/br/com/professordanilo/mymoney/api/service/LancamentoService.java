package br.com.professordanilo.mymoney.api.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.professordanilo.mymoney.api.model.Lancamento;
import br.com.professordanilo.mymoney.api.model.Pessoa;
import br.com.professordanilo.mymoney.api.repository.LancamentoRepository;
import br.com.professordanilo.mymoney.api.repository.PessoaRepository;
import br.com.professordanilo.mymoney.api.service.exeption.PessoaInexistenteOuInativoException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	public Lancamento save(@Valid Lancamento entity) {
		Pessoa pessoa = pessoaRepository.getOne(entity.getPessoa().getCodigo());
		if(pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativoException();
		}
		return lancamentoRepository.save(entity);
	}

	
	
}
