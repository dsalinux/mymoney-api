package br.com.professordanilo.mymoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.professordanilo.mymoney.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
