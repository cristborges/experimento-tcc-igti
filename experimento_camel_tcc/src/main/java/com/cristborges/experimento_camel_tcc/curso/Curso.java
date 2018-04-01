package com.cristborges.experimento_camel_tcc.curso;

public class Curso {

	private Long id;
	private String codigo;
	private String nome;
	private int cargaHoraria;
	private String periodo;

	public Curso() {
	}

	public Curso(Long id, String codigo, String nome, int cargaHoraria, String periodo) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nome = nome;
		this.cargaHoraria = cargaHoraria;
		this.periodo = periodo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(int cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	@Override
	public String toString() {
		return new StringBuilder("CatalogItem { id: ").append(id)
				.append(", codigo: ").append(codigo)
				.append(", nome: ").append(nome)
				.append(", cargaHoraria: ").append(cargaHoraria)
				.append(", periodo: ").append(periodo).append(" }")
				.toString();
	}

}
