package com.cristborges.experimento_camel_tcc.curso;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "curso")
@SequenceGenerator(name = "curso_id_seq", sequenceName = "curso_id_seq")
public class CursoEntity implements Serializable {

	private static final long serialVersionUID = -5956078011517972508L;

	private Long id;
	private String codigo;
	private String nome;
	private int cargaHoraria;
	private String periodo;

   public CursoEntity() {
   }

   public CursoEntity(Long id, String codigo, String nome, int cargaHoraria, String periodo) {
      super();
	   this.id = id;
      this.codigo = codigo;
      this.nome = nome;
      this.cargaHoraria = cargaHoraria;
      this.periodo = periodo;
   }

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "curso_id_seq")
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   @Column(name = "codigo", nullable = false, unique = true)
   public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
   }

   @Column(name = "nome", nullable = false)
   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   @Column(name = "cargahoraria", nullable = false)
   public int getCargaHoraria() {
      return cargaHoraria;
   }

   public void setCargaHoraria(int cargaHoraria) {
      this.cargaHoraria = cargaHoraria;
   }

   @Column(name = "periodo", nullable = false)
   public String getPeriodo() {
      return periodo;
   }

   public void setPeriodo(String periodo) {
      this.periodo = periodo;
   }
}
