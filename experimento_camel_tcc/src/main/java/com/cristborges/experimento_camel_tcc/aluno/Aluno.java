package com.cristborges.experimento_camel_tcc.aluno;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class Aluno {

	private Long id;
	@NotEmpty
	@Pattern(regexp = "^$|(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)")
	private String cpf;
	@NotEmpty
	private String nome;
	@NotEmpty
	private String sobrenome;
	@NotEmpty
	@Email
	private String email;

	public Aluno() {
	}

	public Aluno(Long id, String cpf, String nome, String sobrenome, String email) {
		super();
		this.id = id;
		this.cpf = cpf;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return new StringBuilder("Aluno { id: ").append(id)
				.append(", cpf: ").append(cpf)
				.append(", nome: ").append(nome)
				.append(", sobrenome: ").append(sobrenome)
				.append(", email: ").append(email).append(" }")
				.toString();
	}
}
