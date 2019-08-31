package br.com.professordanilo.mymoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.professordanilo.mymoney.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
