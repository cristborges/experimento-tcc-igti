package com.cristborges.experimento_camel_tcc.matricula;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MatriculasWebsiteProcessor {

   private static final Logger logger = LoggerFactory.getLogger(MatriculasWebsiteProcessor.class);

   @Inject
   private MatriculaService orderService;

   public String transformSqlResultAlunosComMatriculasNovasToXmlNovasMatriculasAluno(Map<String, Object> sqlResultAlunosComMatriculasNovas) {
      String result = null;

      try {
         if (sqlResultAlunosComMatriculasNovas == null) {
            throw new Exception("O ID do aluno não foi passado para o método através do framework de integração.");
         }

         if (!sqlResultAlunosComMatriculasNovas.containsKey("idaluno")) {
            throw new Exception("Não foi encontrada a chave 'idaluno' para o ID do aluno.");
         }

         if (sqlResultAlunosComMatriculasNovas.get("idAluno") == null || !(sqlResultAlunosComMatriculasNovas.get("idaluno") instanceof Long)) {
            throw new Exception("O ID do aluno não foi fornecido ou formatado corretamente.");
         }

         result = orderService.getXmlNovasMatriculasAlunoByIdAluno((Long) sqlResultAlunosComMatriculasNovas.get("idaluno"));
      } catch (Exception e) {
    	  logger.error("Falha ao processar matrículas novas de alunos: " + e.getMessage(), e);
      }

      return result;
   }
}
