package com.cristborges.experimento_camel_tcc.config;

import org.springframework.jdbc.core.JdbcTemplate;

public class DerbyDatabaseBean {

	private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

	public void init() throws Exception {
		try {
			jdbcTemplate.execute("drop table matriculas.aluno");
			jdbcTemplate.execute("drop table matriculas.curso");
			jdbcTemplate.execute("drop table matriculas.matricula");
			jdbcTemplate.execute("drop schema matriculas");
		} catch (Throwable e) {
		}

		jdbcTemplate.execute("CREATE SCHEMA matriculas");
		jdbcTemplate.execute("create table matriculas.aluno (id bigint not null, nome varchar(200) not null, sobrenome varchar(200) not null, email varchar(200) not null, primary key (id))");
		jdbcTemplate.execute("create table matriculas.curso (id bigint not null, codigo varchar(200) not null, nome varchar(200) not null, cargahoraria integer not null, periodo varchar(200) not null, primary key (id))");
		jdbcTemplate.execute("create table matriculas.matricula (id bigint not null, aluno_id bigint not null, curso_id bigint not null, numeromatricula varchar(200) not null, data timestamp not null, status varchar(200) not null, turmareduzida boolean not null, primary key (id))");
		jdbcTemplate.execute("alter table matriculas.matricula add constraint matricula_fk_1 foreign key (aluno_id) references matriculas.aluno (id)");
		jdbcTemplate.execute("alter table matriculas.matricula add constraint matricula_fk_2 foreign key (curso_id) references matriculas.curso (id)");
		jdbcTemplate.execute("create sequence matriculas.aluno_id_seq as integer START WITH 100 INCREMENT BY 1");
		jdbcTemplate.execute("create sequence matriculas.curso_id_seq as integer START WITH 100 INCREMENT BY 1");
		jdbcTemplate.execute("create sequence matriculas.matricula_id_seq as integer START WITH 100 INCREMENT BY 1");
		jdbcTemplate.execute("create sequence matriculas.matriculacurso_id_seq as integer START WITH 100 INCREMENT BY 1");
	}

	public void destroy() throws Exception {
		try {
			jdbcTemplate.execute("drop table matriculas.aluno");
			jdbcTemplate.execute("drop table matriculas.curso");
			jdbcTemplate.execute("drop table matriculas.matricula");
			jdbcTemplate.execute("drop schema matriculas");
		} catch (Throwable e) {
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
