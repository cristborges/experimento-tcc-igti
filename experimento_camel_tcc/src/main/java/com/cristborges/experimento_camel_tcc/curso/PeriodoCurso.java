package com.cristborges.experimento_camel_tcc.curso;

import java.util.Arrays;

public enum PeriodoCurso {

	MATUTINO("M", "Matutino"), VESPERTINO("V", "Vespertino"), NOTURNO("N", "Noturno"), INTEGRAL("I", "Integral");

	private String codigo;
	private String descricao;

	private PeriodoCurso(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static PeriodoCurso getPeriodoByCodigo(String codigo) {
		return Arrays.stream(PeriodoCurso.values())
				.filter(periodoCurso -> periodoCurso.getCodigo().equals(codigo))
				.findFirst().get();
	}
}
