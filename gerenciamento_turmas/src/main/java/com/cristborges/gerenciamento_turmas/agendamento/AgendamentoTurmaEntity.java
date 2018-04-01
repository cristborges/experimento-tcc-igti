package com.cristborges.gerenciamento_turmas.agendamento;

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
import javax.persistence.UniqueConstraint;

import com.cristborges.gerenciamento_turmas.turma.TurmaEntity;

@Entity
@Table(name = "agendamentoturma", uniqueConstraints= @UniqueConstraint(columnNames={"turma_id", "inicio", "termino"}))
@SequenceGenerator(name = "agendamentoturma_id_seq", sequenceName = "agendamentoturma_id_seq")
public class AgendamentoTurmaEntity implements Serializable {

	private static final long serialVersionUID = 6691695637374674774L;

	private Long id;
	private TurmaEntity turma;
	private Date inicio;
	private Date termino;

	public AgendamentoTurmaEntity() {
	}

	public AgendamentoTurmaEntity(Long id, TurmaEntity turma, Date inicio, Date termino) {
		super();
		this.id = id;
		this.turma = turma;
		this.inicio = inicio;
		this.termino = termino;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "inicio", nullable = false)
	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "termino", nullable = false)
	public Date getTermino() {
		return termino;
	}

	public void setTermino(Date termino) {
		this.termino = termino;
	}
}
