package com.cristborges.experimento_camel_tcc.gerenciamentoturma.smartclass;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cristborges.experimento_camel_tcc.curso.PeriodoCurso;
import com.cristborges.experimento_camel_tcc.generated.MatriculasAluno;
import com.cristborges.experimento_camel_tcc.generated.MatriculasAlunoType;
import com.google.gson.Gson;

@Component
public class SmartClassMessageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(SmartClassMessageProcessor.class);

	public String transformXmlMatriculasAlunoToJson(String xmlMatriculasAluno) {
		String result = null;

		try {
			if (xmlMatriculasAluno == null) {
				throw new Exception("O xml das matrículas do aluno não foi passado para o método através do framework de integração.");
			}

			MatriculasAluno matriculasAluno = (MatriculasAluno) JAXBContext.newInstance(MatriculasAluno.class)
					.createUnmarshaller().unmarshal(new StringReader(xmlMatriculasAluno));
			MatriculasAlunoType matriculasAlunoType = matriculasAluno.getMatriculasAlunoType();

			ProcessarMatriculasAlunoRequest processarMatriculasAlunoRequest = new ProcessarMatriculasAlunoRequest(matriculasAlunoType.getMatriculas().stream()
				.map(matricula -> new Matricula(
					matricula.getNumero(), 
					matricula.getCodigoCurso(), 
					matricula.getCurso(), 
					matricula.getCargaHorariaCurso(), 
					PeriodoCurso.valueOf(matricula.getPeriodoCurso().value()).getCodigo(), 
					matricula.getData().toGregorianCalendar().getTime(), 
					matricula.isTurmaReduzida()
				)).collect(Collectors.toCollection(ArrayList::new))
			);

			result = new Gson().toJson(processarMatriculasAlunoRequest);
		} catch (Exception e) {
			logger.error("A transformação da mensagem para o centro de gerenciamento de turmas SmartClass falhou: " + e.getMessage(), e);
		}

		return result;
	}
}
