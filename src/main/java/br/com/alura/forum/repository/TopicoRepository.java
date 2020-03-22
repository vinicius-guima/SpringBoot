package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{
	
	//se houver um atributo de nome igual usa se "_" para dizer que é para filtrar pelo relacinamento
	//usa se a regra nome da entidade com o atributo para filtrar pela relaçao
	Page<Topico> findByCursoNome(String nomeCurso, Pageable pageable);
	
	//para casos que o repository nao tem metodos necessarios
	//monta a query usando JPQL
	//@Para informa ao Spring que o atr que chega pelo metodo é o atr a ser usado na query
	@Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
	List<Topico> carregaCursoPeloNome(@Param("nomeCurso")String nomeCurso);
	
}
