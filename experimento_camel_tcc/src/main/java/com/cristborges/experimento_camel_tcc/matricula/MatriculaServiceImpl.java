package com.cristborges.experimento_camel_tcc.matricula;

import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cristborges.experimento_camel_tcc.aluno.Aluno;
import com.cristborges.experimento_camel_tcc.aluno.AlunoEntity;
import com.cristborges.experimento_camel_tcc.aluno.AlunoRepository;
import com.cristborges.experimento_camel_tcc.curso.Curso;
import com.cristborges.experimento_camel_tcc.curso.CursoEntity;
import com.cristborges.experimento_camel_tcc.curso.PeriodoCurso;
import com.cristborges.experimento_camel_tcc.generated.CentroGerenciamentoTurmas;
import com.cristborges.experimento_camel_tcc.generated.MatriculaType;
import com.cristborges.experimento_camel_tcc.generated.MatriculasAlunoType;
import com.cristborges.experimento_camel_tcc.generated.ObjectFactory;
import com.cristborges.experimento_camel_tcc.generated.Periodo;

@Transactional
@Service
public class MatriculaServiceImpl implements MatriculaService {

	private static final Logger logger = LoggerFactory.getLogger(MatriculaServiceImpl.class);

	@Inject
	private MatriculaRepository matriculaRepository;

	@Inject
	private AlunoRepository alunoRepository;

	@Override
	public Matricula getMatricula(Long id) {
		return getMatriculaByMatriculaEntity(matriculaRepository.findOne(id));
	}

	@Override
	public List<Matricula> getMatriculas() {
		List<Matricula> matriculas = new ArrayList<Matricula>();

		try {
			populateMatriculasByMatriculasEntities(matriculas, matriculaRepository.findAll());
		} catch (Exception e) {
			logger.error("Ocorreu um erro ao obter todas as matrículas: " + e.getMessage(), e);
		}

		return matriculas;
	}

	@Override
	public List<Matricula> getMatriculasByIdAluno(long idAluno) {
		List<Matricula> matriculas = new ArrayList<Matricula>();

		try {
			populateMatriculasByMatriculasEntities(matriculas, matriculaRepository.findByIdAluno(idAluno));
		} catch (Exception e) {
			logger.error("Ocorreu um erro ao obter as matrículas para o aluno id '" + idAluno + "': " + e.getMessage(), e);
		}

		return matriculas;
	}

	@Override
	public void salvarMatriculasAluno(MatriculasAluno matriculasAluno) {
		Aluno aluno = matriculasAluno.getAluno();
		AlunoEntity alunoEntity = alunoRepository.saveAndFlush(new AlunoEntity(aluno.getId(), aluno.getCpf(), aluno.getNome(), aluno.getSobrenome(), aluno.getEmail()));

		matriculaRepository.save(
			matriculasAluno.getMatriculas().stream().map(matricula -> {
				Curso curso = matricula.getCurso();
				ZonedDateTime zonedDateTime = ZonedDateTime.now();

				return new MatriculaEntity(
					null, 
					alunoEntity, 
					new CursoEntity(curso.getId(), curso.getCodigo(), curso.getNome(), curso.getCargaHoraria(), curso.getPeriodo()),
					curso.getId() + DateTimeFormatter.ofPattern("yyMMddHHmmssSSS").format(zonedDateTime), 
					Date.from(zonedDateTime.toInstant()), 
					StatusMatricula.NOVA.getCodigo(), 
					matricula.isTurmaReduzida()
				);
			}).collect(Collectors.toSet())
		);
	}

	@Override
	public String getXmlNovasMatriculasAlunoByIdAluno(Long idAluno) throws Exception {
		AlunoEntity alunoEntity = alunoRepository.findOne(idAluno);

		ObjectFactory objectFactory = new ObjectFactory();
		MatriculasAlunoType matriculasAlunoType = objectFactory.createMatriculasAlunoType();
		matriculasAlunoType.setNomeAluno(StringUtils.join(new Object[] { alunoEntity.getNome(), alunoEntity.getSobrenome() }, ' '));
		matriculasAlunoType.setEmailAluno(alunoEntity.getEmail());
		matriculasAlunoType.setCentroGerenciamentoTurmas(CentroGerenciamentoTurmas.SMART_CLASS);

		for (MatriculaEntity matriculaEntity : matriculaRepository.findByIdAluno(idAluno)) {
			if (matriculaEntity.getStatus().equals(StatusMatricula.NOVA.getCodigo())) {
				MatriculaType matriculaType = objectFactory.createMatriculaType();
				matriculaType.setNumero(matriculaEntity.getNumeroMatricula());
				matriculaType.setTurmaReduzida(matriculaEntity.isTurmaReduzida());

				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				gregorianCalendar.setTime(matriculaEntity.getData());
				matriculaType.setData(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));

				CursoEntity cursoEntity = matriculaEntity.getCurso();
				matriculaType.setCurso(cursoEntity.getNome());
				matriculaType.setCodigoCurso(cursoEntity.getCodigo());
				matriculaType.setCargaHorariaCurso(cursoEntity.getCargaHoraria());
				matriculaType.setPeriodoCurso(Periodo.fromValue(PeriodoCurso.getPeriodoByCodigo(cursoEntity.getPeriodo()).name()));

				matriculasAlunoType.getMatriculas().add(matriculaType);	
			}
		}

		com.cristborges.experimento_camel_tcc.generated.MatriculasAluno matriculasAluno = objectFactory.createMatriculasAluno();
		matriculasAluno.setMatriculasAlunoType(matriculasAlunoType);

		JAXBContext jaxbContext = JAXBContext.newInstance(com.cristborges.experimento_camel_tcc.generated.MatriculasAluno.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(matriculasAluno, stringWriter);
		return stringWriter.toString();
	}

	private void populateMatriculasByMatriculasEntities(List<Matricula> matriculas, Iterable<MatriculaEntity> matriculasEntities) {
		for (MatriculaEntity matriculaEntity : (Iterable<MatriculaEntity>) matriculasEntities::iterator) {
			matriculas.add(getMatriculaByMatriculaEntity(matriculaEntity));
		}
	}

	private Matricula getMatriculaByMatriculaEntity(MatriculaEntity matriculaEntity) {
		AlunoEntity alunoEntity = matriculaEntity.getAluno();
		CursoEntity cursoEntity = matriculaEntity.getCurso();

		return new Matricula(
			matriculaEntity.getId(), 
			new Aluno(alunoEntity.getId(), alunoEntity.getCpf(), alunoEntity.getNome(), alunoEntity.getSobrenome(), alunoEntity.getEmail()), 
			new Curso(cursoEntity.getId(), cursoEntity.getCodigo(), cursoEntity.getNome(), cursoEntity.getCargaHoraria(), PeriodoCurso.getPeriodoByCodigo(cursoEntity.getPeriodo()).getDescricao()), 
			matriculaEntity.getNumeroMatricula(),
			matriculaEntity.getData(),
			StatusMatricula.getStatusMatriculaByCodigo(matriculaEntity.getStatus()).getDescricao(),
			matriculaEntity.isTurmaReduzida()
		);
	}
}
