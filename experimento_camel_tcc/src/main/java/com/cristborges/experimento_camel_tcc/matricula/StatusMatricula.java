package com.cristborges.experimento_camel_tcc.matricula;

import java.util.Arrays;

public enum StatusMatricula {

	NOVA("N", "Nova"), EM_PROCESSAMENTO("E", "Em Processamento"), PROCESSADA("P", "Processada");

	private String codigo;
	private String descricao;

	private StatusMatricula(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static StatusMatricula getStatusMatriculaByCodigo(String codigo) {
		return Arrays.stream(StatusMatricula.values())
				.filter(statusMatricula -> statusMatricula.getCodigo().equals(codigo))
				.findFirst().get();
	}
}
