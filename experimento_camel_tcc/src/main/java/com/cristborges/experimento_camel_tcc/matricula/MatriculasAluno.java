package com.cristborges.experimento_camel_tcc.matricula;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cristborges.experimento_camel_tcc.aluno.Aluno;

public class MatriculasAluno {

	@NotNull
	@Valid
	private Aluno aluno;
	@NotNull
	@Size(min = 1)
	@Valid
	private List<Matricula> matriculas;

	public MatriculasAluno() {
	}

	public MatriculasAluno(Aluno aluno, List<Matricula> matriculas) {
		super();
		this.aluno = aluno;
		this.matriculas = matriculas;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	@Override
	public String toString() {
		return new StringBuilder("MatriculasAluno { aluno: ").append(aluno)
				.append(", matriculas: ").append(matriculas).append(" }")
				.toString();
	}
}
