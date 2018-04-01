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
            throw new Exception("O ID do aluno n�o foi passado para o m�todo atrav�s do framework de integra��o.");
         }

         if (!sqlResultAlunosComMatriculasNovas.containsKey("idaluno")) {
            throw new Exception("N�o foi encontrada a chave 'idaluno' para o ID do aluno.");
         }

         if (sqlResultAlunosComMatriculasNovas.get("idAluno") == null || !(sqlResultAlunosComMatriculasNovas.get("idaluno") instanceof Long)) {
            throw new Exception("O ID do aluno n�o foi fornecido ou formatado corretamente.");
         }

         result = orderService.getXmlNovasMatriculasAlunoByIdAluno((Long) sqlResultAlunosComMatriculasNovas.get("idaluno"));
      } catch (Exception e) {
    	  logger.error("Falha ao processar matr�culas novas de alunos: " + e.getMessage(), e);
      }

      return result;
   }
}
