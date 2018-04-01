package com.cristborges.gerenciamento_turmas.agendamento;

public enum Sala {

	S101("101"), 
	S102("102"), 
	S103("103"), 
	S201("201"), 
	S205("205"), 
	S301("301"), 
	S310("310"), 
	LABORATORIO1("LAB1"), 
	LABORATORIO2("LAB2"), 
	INTERATIVA("IN");

	private String codigo;

	private Sala(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}
}
