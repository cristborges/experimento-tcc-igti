package com.cristborges.experimento_camel_tcc.aluno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlunoRepository extends JpaRepository<AlunoEntity, Long> {

	@Query("select aluno from AlunoEntity aluno where aluno.cpf = ?1")
	AlunoEntity findByCpf(String cpf);
}
