package com.cristborges.gerenciamento_turmas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:turmas.properties")
@ConfigurationProperties
public class TurmasProperties {

	private int quantidadeMinimaAlunos;
	private int quantidadeMaximaAlunos;
	private int quantidadeMaximaAlunosReduzida;
	private int prazoInicioTurma;

	public int getQuantidadeMinimaAlunos() {
		return quantidadeMinimaAlunos;
	}

	public void setQuantidadeMinimaAlunos(int quantidadeMinimaAlunos) {
		this.quantidadeMinimaAlunos = quantidadeMinimaAlunos;
	}

	public int getQuantidadeMaximaAlunos() {
		return quantidadeMaximaAlunos;
	}

	public void setQuantidadeMaximaAlunos(int quantidadeMaximaAlunos) {
		this.quantidadeMaximaAlunos = quantidadeMaximaAlunos;
	}

	public int getQuantidadeMaximaAlunosReduzida() {
		return quantidadeMaximaAlunosReduzida;
	}

	public void setQuantidadeMaximaAlunosReduzida(int quantidadeMaximaAlunosReduzida) {
		this.quantidadeMaximaAlunosReduzida = quantidadeMaximaAlunosReduzida;
	}

	public int getPrazoInicioTurma() {
		return prazoInicioTurma;
	}

	public void setPrazoInicioTurma(int prazoInicioTurma) {
		this.prazoInicioTurma = prazoInicioTurma;
	}
}
