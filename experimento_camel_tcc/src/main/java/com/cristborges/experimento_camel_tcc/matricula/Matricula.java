package com.cristborges.experimento_camel_tcc.matricula;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.cristborges.experimento_camel_tcc.aluno.Aluno;
import com.cristborges.experimento_camel_tcc.curso.Curso;

public class Matricula {

	private Long id;
	private Aluno aluno;
	@NotNull
	private Curso curso;
	private String numeroMatricula;
	private Date data;
	private String status;
	private boolean turmaReduzida;

	public Matricula() {
	}

	public Matricula(Long id, Aluno aluno, Curso curso, String numeroMatricula, Date data, String status, boolean turmaReduzida) {
		super();
		this.id = id;
		this.aluno = aluno;
		this.curso = curso;
		this.numeroMatricula = numeroMatricula;
		this.data = data;
		this.status = status;
		this.turmaReduzida = turmaReduzida;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public String getNumeroMatricula() {
		return numeroMatricula;
	}

	public void setNumeroMatricula(String numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isTurmaReduzida() {
		return turmaReduzida;
	}

	public void setTurmaReduzida(boolean turmaReduzida) {
		this.turmaReduzida = turmaReduzida;
	}

	@Override
	public String toString() {
		return new StringBuilder("Matricula { id: ").append(id)
				.append(", aluno: ").append(aluno)
				.append(", numeroMatricula: ").append(numeroMatricula)
				.append(", data: ").append(data)
				.append(", status: ").append(status).append(" }")
				.toString();
	}
}
