package com.cristborges.gerenciamento_turmas.agendamento;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;

public enum PeriodoCurso {

	MATUTINO("M", LocalTime.of(8, 0), LocalTime.of(12, 0)), 
	VESPERTINO("V", LocalTime.of(14, 0), LocalTime.of(18, 0)), 
	NOTURNO("N", LocalTime.of(18, 0), LocalTime.of(22, 0)), 
	INTEGRAL("I", LocalTime.of(9, 0), LocalTime.of(17, 0));

	private String codigo;
	LocalTime horarioInicio;
	LocalTime horarioTermino;

	private PeriodoCurso(String codigo, LocalTime horarioInicio, LocalTime horarioTermino) {
		this.codigo = codigo;
		this.horarioInicio = horarioInicio;
		this.horarioTermino = horarioTermino;
	}

	public String getCodigo() {
		return codigo;
	}

	public LocalTime getHorarioInicio() {
		return horarioInicio;
	}

	public LocalTime getHorarioTermino() {
		return horarioTermino;
	}

	public Duration getDuracao() {
		return Duration.between(horarioInicio, horarioTermino);
	}

	public static PeriodoCurso getPeriodoByCodigo(String codigo) {
		return Arrays.stream(PeriodoCurso.values())
				.filter(periodoCurso -> periodoCurso.getCodigo().equals(codigo))
				.findFirst().get();
	}
}
