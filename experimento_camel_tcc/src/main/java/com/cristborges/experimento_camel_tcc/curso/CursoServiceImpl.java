package com.cristborges.experimento_camel_tcc.curso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CursoServiceImpl implements CursoService {

	private static final Logger logger = LoggerFactory.getLogger(CursoServiceImpl.class);

	@Inject
	private CursoRepository cursoRepository;

	@Override
	public List<Curso> getCursos() {
		List<Curso> cursos = new ArrayList<Curso>();

		try {
			populateCursosByCursosEntities(cursos, cursoRepository.findAll());
		} catch (Exception e) {
			logger.error("Ocorreu um erro ao obter todas os cursos: " + e.getMessage(), e);
		}

		return cursos;
	}

	private void populateCursosByCursosEntities(List<Curso> cursos, Iterable<CursoEntity> cursosEntities) {
		for (CursoEntity cursoEntity : (Iterable<CursoEntity>) cursosEntities::iterator) {
			cursos.add(new Curso(cursoEntity.getId(), cursoEntity.getCodigo(), cursoEntity.getNome(), cursoEntity.getCargaHoraria(), PeriodoCurso.getPeriodoByCodigo(cursoEntity.getPeriodo()).getDescricao()));
		}
	}
}
