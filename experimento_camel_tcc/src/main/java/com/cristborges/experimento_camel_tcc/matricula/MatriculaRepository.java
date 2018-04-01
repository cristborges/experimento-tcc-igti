package com.cristborges.experimento_camel_tcc.matricula;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MatriculaRepository extends PagingAndSortingRepository<MatriculaEntity, Long> {

	@Query("select matricula from MatriculaEntity matricula where matricula.status = ?1 order by matricula.data")
	Page<MatriculaEntity> findByStatus(String status, Pageable pageable);

	@Query("select matricula from MatriculaEntity matricula where matricula.aluno.id = ?1")
	List<MatriculaEntity> findByIdAluno(long idAluno);
}
