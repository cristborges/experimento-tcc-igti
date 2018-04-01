package com.cristborges.gerenciamento_turmas.turma;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.cristborges.gerenciamento_turmas.agendamento.AgendamentoTurmaEntity;
import com.cristborges.gerenciamento_turmas.agendamento.AgendamentoTurmaRepository;
import com.cristborges.gerenciamento_turmas.agendamento.PeriodoCurso;
import com.cristborges.gerenciamento_turmas.agendamento.Sala;
import com.cristborges.gerenciamento_turmas.aluno.AlunoTurmaEntity;
import com.cristborges.gerenciamento_turmas.aluno.AlunoTurmaRepository;
import com.cristborges.gerenciamento_turmas.config.ActiveMQProperties;
import com.cristborges.gerenciamento_turmas.config.TurmasProperties;
import com.cristborges.gerenciamento_turmas.matricula.Matricula;

@Transactional
@Service
public class TurmaServiceImpl implements TurmaService {

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	private TurmaRepository turmaRepository;

	@Autowired
	private AlunoTurmaRepository alunoTurmaRepository;

	@Autowired
	private AgendamentoTurmaRepository agendamentoTurmaRepository;

	@Autowired
	private TurmasProperties turmasProperties;

	@Autowired
	private ActiveMQProperties activemqProperties;

	@Override
	public void processarMatriculas(List<Matricula> matriculas) {
		matriculas.stream().forEach(matricula -> {
			boolean turmaReduzida = matricula.isTurmaReduzida();

			TurmaEntity turma = turmaRepository.findTurmaAberta(
				matricula.getCodigoCurso(), 
				turmaReduzida, 
				turmaReduzida ? turmasProperties.getQuantidadeMaximaAlunosReduzida() : turmasProperties.getQuantidadeMaximaAlunos()
			);

			if (turma == null) {
				turma = turmaRepository.save(new TurmaEntity(
					null, 
					obterCodigoProximaTurma(turmaRepository.findFirstByCodigoCursoOrderByCodigoCursoDesc(matricula.getCodigoCurso())),
					matricula.getCodigoCurso(), 
					matricula.isTurmaReduzida(), 
					null
				));
			}

			alunoTurmaRepository.save(new AlunoTurmaEntity(null, turma, matricula.getNumeroMatricula()));

			if (turma.getSala() == null && alunoTurmaRepository.countByTurma(turma) >= turmasProperties.getQuantidadeMinimaAlunos()) {
				List<Sala> salasAgendamentoNaoExecutado = turmaRepository.findTurmasAgendamentoNaoExecutado().stream()
					.map(turmaAgendamentoNaoExecutado -> turmaAgendamentoNaoExecutado.getSala()).collect(Collectors.toList());

				turma.setSala(Stream.of(Sala.values()).filter(sala -> !salasAgendamentoNaoExecutado.contains(sala)).findFirst().get());
				turma = turmaRepository.save(turma);

				int cargaHorariaAAgendar = matricula.getCargaHorariaCurso();
				PeriodoCurso periodoCurso = PeriodoCurso.getPeriodoByCodigo(matricula.getPeriodoCurso());
				ZonedDateTime inicioAgendamentoTurma = ZonedDateTime.of(matricula.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), periodoCurso.getHorarioInicio(), ZoneId.systemDefault())
					.plusDays(turmasProperties.getPrazoInicioTurma() - 1);
				ZonedDateTime terminoAgendamentoTurma = inicioAgendamentoTurma
					.withHour(periodoCurso.getHorarioTermino().getHour());

				int quantidadeAgendamentos = 0;
				long agendamentos[][] = new long[(int) Math.ceil(cargaHorariaAAgendar / periodoCurso.getDuracao().toHours())][2];

				do {
					do {
						inicioAgendamentoTurma = inicioAgendamentoTurma.plusDays(1);
					} while (inicioAgendamentoTurma.getDayOfWeek() == DayOfWeek.SATURDAY || inicioAgendamentoTurma.getDayOfWeek() == DayOfWeek.SUNDAY);

					terminoAgendamentoTurma = terminoAgendamentoTurma
						.withDayOfYear(inicioAgendamentoTurma.getDayOfYear())
						.minusHours(Math.max(0, periodoCurso.getDuracao().toHours() - cargaHorariaAAgendar));

					agendamentoTurmaRepository.save(new AgendamentoTurmaEntity(
						null, 
						turma, 
						Date.from(inicioAgendamentoTurma.toInstant()), 
						Date.from(terminoAgendamentoTurma.toInstant())
					));

					agendamentos[quantidadeAgendamentos][0] = inicioAgendamentoTurma.toInstant().toEpochMilli();
					agendamentos[quantidadeAgendamentos][1] = terminoAgendamentoTurma.toInstant().toEpochMilli();
					quantidadeAgendamentos++;

					cargaHorariaAAgendar -= (int) Duration.between(inicioAgendamentoTurma, terminoAgendamentoTurma).toHours();
				} while(cargaHorariaAAgendar > 0);

				jmsTemplate.convertAndSend(
					activemqProperties.getSchedulingTopic(), 
					new Agendamento(turma.getSala().getCodigo(), matricula.getDescricaoCurso(), turma.getCodigo(), agendamentos)
				);
			}
		});
	}

	private static String obterCodigoProximaTurma(TurmaEntity turmaAnterior) {
		AtomicBoolean encontradoCaractereDiferenteZ = new AtomicBoolean();

		return new StringBuilder(Optional.ofNullable(turmaAnterior).map(turma -> turma.getCodigo()).orElse(String.valueOf(Character.MIN_VALUE)))
				.reverse().chars()
				.mapToObj(c -> (char) (encontradoCaractereDiferenteZ.compareAndSet(false, c < 'Z') ? Math.max('A', ++c % ('Z' + 1)) : c))
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.append(encontradoCaractereDiferenteZ.get() ? "" : 'A')
				.reverse().toString();
	}
}
