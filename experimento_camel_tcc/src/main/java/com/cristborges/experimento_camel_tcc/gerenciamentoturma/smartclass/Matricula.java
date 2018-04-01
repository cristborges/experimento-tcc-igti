package com.cristborges.experimento_camel_tcc.gerenciamentoturma.smartclass;

import java.util.Date;

public class Matricula {

	private String numeroMatricula;
	private String codigoCurso;
	private String descricaoCurso;
	private int cargaHorariaCurso;
	private String periodoCurso;
	private Date data;
	private boolean turmaReduzida;

	public Matricula() {
	}

	public Matricula(String numeroMatricula, String codigoCurso, String descricaoCurso, int cargaHorariaCurso, String periodoCurso, Date data, boolean turmaReduzida) {
		super();
		this.numeroMatricula = numeroMatricula;
		this.codigoCurso = codigoCurso;
		this.descricaoCurso = descricaoCurso;
		this.cargaHorariaCurso = cargaHorariaCurso;
		this.periodoCurso = periodoCurso;
		this.data = data;
		this.turmaReduzida = turmaReduzida;
	}

	public String getNumeroMatricula() {
		return numeroMatricula;
	}

	public void setNumeroMatricula(String numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}

	public String getCodigoCurso() {
		return codigoCurso;
	}

	public void setCodigoCurso(String codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	public String getDescricaoCurso() {
		return descricaoCurso;
	}

	public void setDescricaoCurso(String descricaoCurso) {
		this.descricaoCurso = descricaoCurso;
	}

	public int getCargaHorariaCurso() {
		return cargaHorariaCurso;
	}

	public void setCargaHorariaCurso(int cargaHorariaCurso) {
		this.cargaHorariaCurso = cargaHorariaCurso;
	}

	public String getPeriodoCurso() {
		return periodoCurso;
	}

	public void setPeriodoCurso(String periodoCurso) {
		this.periodoCurso = periodoCurso;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public boolean isTurmaReduzida() {
		return turmaReduzida;
	}

	public void setTurmaReduzida(boolean turmaReduzida) {
		this.turmaReduzida = turmaReduzida;
	}

	@Override
	public String toString() {
		return new StringBuilder("Matricula { ").append("numeroMatricula: ").append(numeroMatricula)
				.append(", codigoCurso: ").append(codigoCurso)
				.append(", periodoCurso: ").append(periodoCurso)
				.append(", data: ").append(data)
				.append(", turmaReduzida: ").append(turmaReduzida).append(" }")
				.toString();
	}
}
