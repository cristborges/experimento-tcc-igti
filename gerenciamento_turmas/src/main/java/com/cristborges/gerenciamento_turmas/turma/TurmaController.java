package com.cristborges.gerenciamento_turmas.turma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cristborges.gerenciamento_turmas.rest.GenericRestResponse;

@RestController
public class TurmaController {

	@Autowired
	private TurmaService turmaService;

	@PostMapping("/processarMatriculasAluno")
	public GenericRestResponse processarMatriculasAluno(@RequestBody ProcessarMatriculasRequest processarMatriculasRequest) {
		try {
			turmaService.processarMatriculas(processarMatriculasRequest.getMatriculas());
			return new GenericRestResponse(HttpStatus.OK.value(), "Matrículas processadas com sucesso!");
		} catch (Exception e) {
			return new GenericRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro ao processar as matrículas!");
		}
	}
}
