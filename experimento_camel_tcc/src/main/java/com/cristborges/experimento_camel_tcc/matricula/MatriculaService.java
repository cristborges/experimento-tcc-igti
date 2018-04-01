package com.cristborges.experimento_camel_tcc.matricula;

import java.util.List;

public interface MatriculaService {

	Matricula getMatricula(Long id);

	List<Matricula> getMatriculas();

	List<Matricula> getMatriculasByIdAluno(long idAluno);

	void salvarMatriculasAluno(MatriculasAluno matriculasAluno);

	String getXmlNovasMatriculasAlunoByIdAluno(Long idAluno) throws Exception;
}
