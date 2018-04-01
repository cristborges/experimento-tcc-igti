package com.cristborges.experimento_camel_tcc.ambiente_iot;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.SQLException;

import org.apache.commons.lang3.ArrayUtils;

public class ComandosAgendamentoAmbiente {
	private String sala;

	private int[] comandoDesligar;

	private int[] comandoLigar;

	private BigDecimal temperatura;

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public int[] getComandoDesligar() {
		return this.comandoDesligar;
	}

	public void setComandoDesligarArray(Array comandoDesligarArray) {
		try {
			this.comandoDesligar = ArrayUtils.toPrimitive((Integer[]) comandoDesligarArray.getArray());
		} catch (SQLException e) { }
	}

	public void setComandoDesligar(int[] comandoDesligar) {
		this.comandoDesligar = comandoDesligar;
	}

	public void setComandoLigarArray(Array comandoLigarArray) {
		try {
			this.comandoLigar = ArrayUtils.toPrimitive((Integer[]) comandoLigarArray.getArray());
		} catch (SQLException e) { }
	}

	public int[] getComandoLigar() {
		return this.comandoLigar;
	}

	public void setComandoLigar(int[] comandoLigar) {
		this.comandoLigar = comandoLigar;
	}

	public BigDecimal getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(BigDecimal temperatura) {
		this.temperatura = temperatura;
	}
}
