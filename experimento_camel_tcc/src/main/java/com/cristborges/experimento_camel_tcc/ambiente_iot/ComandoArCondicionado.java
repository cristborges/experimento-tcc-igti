package com.cristborges.experimento_camel_tcc.ambiente_iot;

import java.sql.Array;
import java.sql.SQLException;

import org.apache.commons.lang3.ArrayUtils;

public class ComandoArCondicionado {

	private String sala;

	private int[] comando;

	private float temperatura;

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public int[] getComando() {
		return comando;
	}

	public void setComandoArray(Array comandoArray) {
		try {
			this.comando = ArrayUtils.toPrimitive((Integer[]) comandoArray.getArray());
		} catch (SQLException e) { }
	}

	public void setComando(int[] comando) {
		this.comando = comando;
	}

	public float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}
}
