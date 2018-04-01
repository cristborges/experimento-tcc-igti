package com.cristborges.experimento_camel_tcc.gerenciamentoturma.xyz;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cristborges.experimento_camel_tcc.generated.MatriculasAluno;
import com.cristborges.experimento_camel_tcc.generated.MatriculasAlunoType;

@Component
public class XyzMessageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(XyzMessageProcessor.class);

	public List<Map<String, String>> transformXmlsMatriculasAlunoToKeyValueList(List<String> xmlsMatriculasAluno) throws Exception {
		logger.info("Processando os XMLs agregados");

		List<Map<String, String>> result = new ArrayList<>();

		Map<String, String> header = new LinkedHashMap<>(3);
		header.put("numeroMatricula", "Número da Matrícula");
		header.put("curso", "Curso");
		header.put("nomeAluno", "Nome do Aluno");
		result.add(header);

		try {
			if (xmlsMatriculasAluno != null) {
				for (String xmlMatriculasAluno : (Iterable<String>) xmlsMatriculasAluno::iterator) {
					JAXBContext jaxbContext = JAXBContext.newInstance(MatriculasAluno.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					MatriculasAlunoType matriculasAluno = ((MatriculasAluno) unmarshaller.unmarshal(new StringReader(xmlMatriculasAluno))).getMatriculasAlunoType();

					matriculasAluno.getMatriculas().stream().forEach(matricula -> {
						Map<String, String> row = new LinkedHashMap<>(3);
						row.put("numeroMatricula", matricula.getNumero());
						row.put("curso", matricula.getCurso());
						row.put("nomeAluno", matriculasAluno.getNomeAluno());
						result.add(row);
					});
				}
			}
		} catch (Exception e) {
			logger.error("A transformação das mensagens para o centro de gerenciamento de turmas Xyz falhou: " + e.getMessage(), e);
			throw e;
		}

		return result;
	}
}
