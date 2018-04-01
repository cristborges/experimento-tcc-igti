package com.cristborges.gerenciamento_turmas.turma;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.cristborges.gerenciamento_turmas.agendamento.Sala;

@Entity
@Table(name = "turma")
@SequenceGenerator(name = "turma_id_seq", sequenceName = "turma_id_seq")
public class TurmaEntity implements Serializable {

	private static final long serialVersionUID = 586434319942103757L;

	private Long id;
	private String codigo;
	private String codigoCurso;
	private boolean reduzida;
	private Sala sala;

	public TurmaEntity() {
	}

	public TurmaEntity(Long id, String codigo, String codigoCurso, boolean reduzida, Sala sala) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.codigoCurso = codigoCurso;
		this.reduzida = reduzida;
		this.sala = sala;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "turma_id_seq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "codigo", nullable = false)
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "codigocurso", nullable = false)
	public String getCodigoCurso() {
		return codigoCurso;
	}

	public void setCodigoCurso(String codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	@Column(name = "reduzida", nullable = false)
	public boolean isReduzida() {
		return reduzida;
	}

	public void setReduzida(boolean reduzida) {
		this.reduzida = reduzida;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "sala")
	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}
}
