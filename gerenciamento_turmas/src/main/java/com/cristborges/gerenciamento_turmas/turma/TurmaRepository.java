package com.cristborges.gerenciamento_turmas.turma;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TurmaRepository extends PagingAndSortingRepository<TurmaEntity, Long> {

	@Query("select turma from TurmaEntity turma " +
			"where turma.codigoCurso = ?1 and turma.reduzida = ?2 " +
			"group by turma.id " +
			"having (select count(alunoTurma) from AlunoTurmaEntity alunoTurma where alunoTurma.turma.id = turma.id) < ?3")
	TurmaEntity findTurmaAberta(String codigoCurso, boolean reduzida, long quantidadeMaximaAlunos);

	@Query("select turma from TurmaEntity turma " +
			"where turma.sala is not null " +
			"group by turma.id " +
			"having (select max(agendamentoTurma.termino) from AgendamentoTurmaEntity agendamentoTurma where agendamentoTurma.turma.id = turma.id) > current_timestamp()")
	List<TurmaEntity> findTurmasAgendamentoNaoExecutado();

	TurmaEntity findFirstByCodigoCursoOrderByCodigoCursoDesc(String codigoCurso);
}
