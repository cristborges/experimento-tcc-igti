package com.cristborges.gerenciamento_turmas.turma;

import java.util.List;

import com.cristborges.gerenciamento_turmas.matricula.Matricula;

public interface TurmaService {

	void processarMatriculas(List<Matricula> matriculas);
}
