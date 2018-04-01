package com.cristborges.experimento_camel_tcc.ambiente_iot;

public class AgendamentoAmbiente {
	private String sala;

	private String curso;

	private String turma;

	private int[][] agendamentos;

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

	public int[][] getAgendamentos() {
		return agendamentos;
	}

	public void setAgendamentos(int[][] agendamentos) {
		this.agendamentos = agendamentos;
	}
}
