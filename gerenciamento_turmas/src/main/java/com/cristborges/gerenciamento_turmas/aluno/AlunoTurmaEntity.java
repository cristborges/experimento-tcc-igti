package com.cristborges.gerenciamento_turmas.aluno;

import java.io.Serializable;

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
import javax.persistence.UniqueConstraint;

import com.cristborges.gerenciamento_turmas.turma.TurmaEntity;

@Entity
@Table(name = "alunoturma", uniqueConstraints= @UniqueConstraint(columnNames={"turma_id", "numeromatriculaaluno"}))
@SequenceGenerator(name = "alunoturma_id_seq", sequenceName = "alunoturma_id_seq")
public class AlunoTurmaEntity implements Serializable {

	private static final long serialVersionUID = -1783359013626160350L;

	private Long id;
	private TurmaEntity turma;
	private String numeroMatriculaAluno;

	public AlunoTurmaEntity(Long id, TurmaEntity turma, String numeroMatriculaAluno) {
		super();
		this.id = id;
		this.turma = turma;
		this.numeroMatriculaAluno = numeroMatriculaAluno;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "turma_id", nullable = false)
	public TurmaEntity getTurma() {
		return turma;
	}

	public void setTurma(TurmaEntity turma) {
		this.turma = turma;
	}

	@Column(name = "numeromatriculaaluno", nullable = false, unique = true)
	public String getNumeroMatriculaAluno() {
		return numeroMatriculaAluno;
	}

	public void setNumeroMatriculaAluno(String numeroMatriculaAluno) {
		this.numeroMatriculaAluno = numeroMatriculaAluno;
	}
}
