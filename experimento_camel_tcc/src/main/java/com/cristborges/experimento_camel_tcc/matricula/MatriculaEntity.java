package com.cristborges.experimento_camel_tcc.matricula;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cristborges.experimento_camel_tcc.aluno.AlunoEntity;
import com.cristborges.experimento_camel_tcc.curso.CursoEntity;

@Entity
@Table(name = "matricula")
@SequenceGenerator(name = "matricula_id_seq", sequenceName = "matricula_id_seq")
public class MatriculaEntity implements Serializable {

	private static final long serialVersionUID = -2976898639528814671L;

	private Long id;
	private AlunoEntity aluno;
	private CursoEntity curso;
	private String numeroMatricula;
	private Date data;
	private String status;
	private boolean turmaReduzida;

	public MatriculaEntity() {
	}

	public MatriculaEntity(Long id, AlunoEntity aluno, CursoEntity curso, String numeroMatricula, Date data, String status, boolean turmaReduzida) {
		super();
		this.id = id;
		this.aluno = aluno;
		this.curso = curso;
		this.numeroMatricula = numeroMatricula;
		this.data = data;
		this.status = status;
		this.turmaReduzida = turmaReduzida;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "matricula_id_seq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aluno_id", nullable = false)
	public AlunoEntity getAluno() {
		return aluno;
	}

	public void setAluno(AlunoEntity aluno) {
		this.aluno = aluno;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id", nullable = false)
	public CursoEntity getCurso() {
		return curso;
	}

	public void setCurso(CursoEntity curso) {
		this.curso = curso;
	}

	@Column(name = "numeromatricula", nullable = false, unique = true)
	public String getNumeroMatricula() {
		return numeroMatricula;
	}

	public void setNumeroMatricula(String numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Column(name = "status", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "turmareduzida", nullable = false)
	public boolean isTurmaReduzida() {
		return turmaReduzida;
	}

	public void setTurmaReduzida(boolean turmaReduzida) {
		this.turmaReduzida = turmaReduzida;
	}
}
