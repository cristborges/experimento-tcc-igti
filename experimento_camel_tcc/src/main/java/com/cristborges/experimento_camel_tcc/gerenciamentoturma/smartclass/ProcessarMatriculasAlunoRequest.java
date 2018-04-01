package com.cristborges.experimento_camel_tcc.gerenciamentoturma.smartclass;

import java.util.List;

public class ProcessarMatriculasAlunoRequest {

   private List<Matricula> matriculas;

   public ProcessarMatriculasAlunoRequest() {
   }

   public ProcessarMatriculasAlunoRequest(List<Matricula> matriculas) {
      super();
      this.matriculas = matriculas;
   }

   public List<Matricula> getMatriculas() {
      return matriculas;
   }

   public void setMatriculas(List<Matricula> matriculas) {
      this.matriculas = matriculas;
   }

   @Override
   public String toString() {
      return new StringBuilder("MatriculaRequest { ")
    		  .append("matriculas: ").append(matriculas).append(" }")
    		  .toString();
   }
}
