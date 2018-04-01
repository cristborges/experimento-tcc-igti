package com.cristborges.gerenciamento_turmas.turma;

import java.util.List;

import com.cristborges.gerenciamento_turmas.matricula.Matricula;

public class ProcessarMatriculasRequest {

	private List<Matricula> matriculas;

	public ProcessarMatriculasRequest() {
	}

	public ProcessarMatriculasRequest(List<Matricula> matriculas) {
		super();
		this.matriculas = matriculas;
	}

	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}
}
