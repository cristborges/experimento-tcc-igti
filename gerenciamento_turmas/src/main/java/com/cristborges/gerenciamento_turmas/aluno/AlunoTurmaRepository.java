package com.cristborges.gerenciamento_turmas.aluno;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cristborges.gerenciamento_turmas.turma.TurmaEntity;

public interface AlunoTurmaRepository extends PagingAndSortingRepository<AlunoTurmaEntity, Long> {

	long countByTurma(TurmaEntity turma);
}
