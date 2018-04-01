package com.cristborges.gerenciamento_turmas.turma;

public class Agendamento {

	private String sala;
	private String curso;
	private String turma;
	private long agendamentos[][];

	public Agendamento(String sala, String curso, String turma, long[][] agendamentos) {
		super();
		this.sala = sala;
		this.curso = curso;
		this.turma = turma;
		this.agendamentos = agendamentos;
	}

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public long[][] getAgendamentos() {
		return agendamentos;
	}

	public void setAgendamentos(long[][] agendamentos) {
		this.agendamentos = agendamentos;
	}
}
