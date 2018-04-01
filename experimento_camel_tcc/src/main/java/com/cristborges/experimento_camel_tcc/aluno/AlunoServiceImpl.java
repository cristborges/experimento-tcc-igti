package com.cristborges.experimento_camel_tcc.aluno;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class AlunoServiceImpl implements AlunoService {

	@Inject
	private AlunoRepository alunoRepository;

	@Override
	public Aluno getAlunoByCpf(String cpf) {
		Aluno aluno = null;
		AlunoEntity alunoEntity = alunoRepository.findByCpf(cpf);

		if (alunoEntity != null) {
			aluno = new Aluno(alunoEntity.getId(), alunoEntity.getCpf(), alunoEntity.getNome(), alunoEntity.getSobrenome(), alunoEntity.getEmail());
		}

		return aluno;
	}
}
