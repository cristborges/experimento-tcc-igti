package com.cristborges.experimento_camel_tcc.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.http.HttpStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.cristborges.experimento_camel_tcc.ambiente_iot.AgendamentoAmbiente;
import com.cristborges.experimento_camel_tcc.ambiente_iot.ComandoArCondicionado;
import com.cristborges.experimento_camel_tcc.ambiente_iot.ComandosAgendamentoAmbiente;
import com.cristborges.experimento_camel_tcc.ambiente_iot.StatusArCondicionado;
import com.cristborges.experimento_camel_tcc.generated.CentroGerenciamentoTurmas;
import com.cristborges.experimento_camel_tcc.gerenciamentoturma.smartclass.ProcessarMatriculasAlunoResponse;
import com.cristborges.experimento_camel_tcc.matricula.StatusMatricula;
import com.google.gson.Gson;

@Configuration
public class ConfigIntegracao extends CamelConfiguration {

	@Inject
	private javax.sql.DataSource dataSource;

	@Inject
	private Environment environment;

	private Namespaces matriculasAlunoNamespaces = new Namespaces("m", "http://www.cristborges.com/experimento_camel_tcc/MatriculasAluno");

	@Bean
	public ConnectionFactory connectionFactory() {
		return new ActiveMQConnectionFactory(environment.getProperty("activeMQ.brokerURL"));
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public PooledConnectionFactory pooledConnectionFactory() {
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
		pooledConnectionFactory.setConnectionFactory(connectionFactory());
		pooledConnectionFactory.setMaxConnections(Integer.parseInt(environment.getProperty("pooledConnectionFactory.maxConnections")));
		return pooledConnectionFactory;
	}

	@Bean
	public JmsConfiguration jmsConfiguration() {
		JmsConfiguration jmsConfiguration = new JmsConfiguration();
		jmsConfiguration.setConnectionFactory(pooledConnectionFactory());
		return jmsConfiguration;
	}

	@Bean
	public ActiveMQComponent activeMQ() {
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setConfiguration(jmsConfiguration());
		return activeMQComponent;
	}

	@Bean
	public SqlComponent sql() {
		SqlComponent sqlComponent = new SqlComponent();
		sqlComponent.setDataSource(dataSource);
		return sqlComponent;
	}

	@Bean
	public RouteBuilder novasMatriculasAlunoWebsiteRoute() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(
					"sql:select distinct aluno_id as idaluno from matriculas.matricula " +
					"where status = '" + StatusMatricula.NOVA.getCodigo() + "'" +
						"?consumer.delay=2000" +
						"&consumer.onConsume=update matriculas.matricula " +
							"set status = '" +  StatusMatricula.EM_PROCESSAMENTO.getCodigo() + "' " +
							"where aluno_id = :#idaluno " + 
							"and status = '" + StatusMatricula.NOVA.getCodigo() + "'"
				).beanRef(
					"matriculasWebsiteProcessor", 
					"transformSqlResultAlunosComMatriculasNovasToXmlNovasMatriculasAluno"
				).to("activemq:queue:PROCESSAMENTO_NOVAS_MATRICULAS");
			}
		};
	}

	@Bean
	public RouteBuilder centroGerenciamentoTurmasContentBasedRoute() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("activemq:queue:PROCESSAMENTO_NOVAS_MATRICULAS").choice()
					.when().xpath(
						"/m:MatriculasAluno/m:MatriculasAlunoType/m:CentroGerenciamentoTurmas = " +
						"'" + CentroGerenciamentoTurmas.SMART_CLASS.value() + "'", 
						matriculasAlunoNamespaces
					).to("activemq:queue:REQUISICOES_GERENCIAMENTO_TURMAS_SMARTCLASS")
					.when().xpath(
						"/m:MatriculasAluno/m:MatriculasAlunoType/m:CentroGerenciamentoTurmas = " +
						"'" + CentroGerenciamentoTurmas.XYZ.value() + "'", 
						matriculasAlunoNamespaces
					).to("activemq:queue:REQUISICOES_GERENCIAMENTO_TURMAS_XYZ")
					.otherwise().to("activemq:queue:ERROS_REQUISICOES_GERENCIAMENTO_TURMAS");
			}
		};
	}

	@Bean
	public RouteBuilder centroGerenciamentoTurmasSmartClassRoute() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("activemq:queue:REQUISICOES_GERENCIAMENTO_TURMAS_SMARTCLASS")
					.setProperty("numerosMatriculas", matriculasAlunoNamespaces.xpath(
						"/m:MatriculasAluno/m:MatriculasAlunoType/m:Matriculas/*[self::m:Numero]/text()"
					))
					.beanRef("smartClassMessageProcessor", "transformXmlMatriculasAlunoToJson")
					.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
					.to("http4://localhost:8090/gerenciamentoTurmas/processarMatriculasAluno")
						.filter(exchange -> 
							(new Gson().fromJson(
								exchange.getIn().getBody(String.class), 
								ProcessarMatriculasAlunoResponse.class
							)).getResponseCode() == HttpStatus.SC_OK
                        )
						.setBody(property("numerosMatriculas"))
						.split(body()).convertBodyTo(String.class)
						.to("sql:update matriculas.matricula " +
							"set status = '" +  StatusMatricula.PROCESSADA.getCodigo() + "' " +
							"where numeromatricula = :#${body}");
			}
		};
	}

	@Bean
	public RouteBuilder centroGerenciamentoTurmasXyzRoute() {
		return new RouteBuilder() {
			@SuppressWarnings("unchecked")
			@Override
			public void configure() throws Exception {
				onException(CamelExchangeException.class)
					.to("activemq:queue:ERROS_REQUISICOES_GERENCIAMENTO_TURMAS_XYZ");

				from("activemq:queue:REQUISICOES_GERENCIAMENTO_TURMAS_XYZ")
					.aggregate((Exchange oldExchange, Exchange newExchange) -> {
						Object newExchangeBody = newExchange.getIn().getBody();
						if (oldExchange == null) {
							newExchange.getIn()
								.setBody(Stream.of(newExchangeBody).collect(Collectors.toList()));
							return newExchange;
						} else {
							oldExchange.getIn().getBody(List.class).add(newExchangeBody);
							return oldExchange;
						}
					}).xpath(
						"/m:MatriculasAluno/m:MatriculasAlunoType/m:CentroGerenciamentoTurmas" +
						"[text() = '" + CentroGerenciamentoTurmas.XYZ.value() + "']", 
						String.class, matriculasAlunoNamespaces
					).completionInterval(10000)
						.beanRef("xyzMessageProcessor", "transformXmlsMatriculasAlunoToKeyValueList")
						.marshal().csv()
						.to(
							"file://C:/experimento_camel_tcc/xyz/out?" +
								"fileName=matriculas_xyz_${date:now:yyyyMMddhhmmss}.csv"
						)
							.setHeader(
								"CamelFileName", 
								header("CamelFileNameProduced").regexReplaceAll("(.+\\\\)*", "")
							).to("sftp://localhost:22?username=cristiano&password=123456");
			}
		};
	}

	@Bean
	public RouteBuilder classroomCourseSchedulingRoute() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("activemq:topic:Classroom.Course.Scheduling.Set")
					.unmarshal().json(JsonLibrary.Gson, AgendamentoAmbiente.class)
					.to(
						"sql:select " +
							"arcondicionado.sala as sala, " +
							"arcondicionado.comandodesligar as comando_desligar_array, " +
							"configuracaoarcondicionado.comando as comando_ligar_array, " +
							"configuracao.temperatura as temperatura " +
							"from equipamentos.arcondicionado " +
							"inner join equipamentos.configuracaoarcondicionado on configuracaoarcondicionado.arcondicionado_id = arcondicionado.id " +
							"and configuracaoarcondicionado.padrao = true " +
							"inner join equipamentos.configuracao on configuracao.id = configuracaoarcondicionado.configuracao_id " +
							"where arcondicionado.sala = :#${body.sala}" +
							"?outputType=SelectOne&outputClass=com.cristborges.experimento_camel_tcc.ambiente_iot.ComandosAgendamentoAmbiente"
					)
						.marshal().json(JsonLibrary.Gson, ComandosAgendamentoAmbiente.class)
						.to("activemq:topic:Classroom.Course.Scheduling.IRCommands");
			}
		};
	}

	@Bean
	public RouteBuilder classroomAirConditionerRoute() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("activemq:topic:Classroom.AirConditioner.Status.Set")
					.unmarshal().json(JsonLibrary.Gson, StatusArCondicionado.class).choice()
					.when().simple("${body.arCondicionadoLigado}")
						.to(
							"sql:select " +
								"arcondicionado.sala as sala, " +
								"configuracaoarcondicionado.comando as comando_array, " +
								"configuracao.temperatura as temperatura " +
								"from equipamentos.arcondicionado " +
								"inner join equipamentos.configuracaoarcondicionado on configuracaoarcondicionado.arcondicionado_id = arcondicionado.id " +
								"and configuracaoarcondicionado.padrao = true " +
								"inner join equipamentos.configuracao on configuracao.id = configuracaoarcondicionado.configuracao_id " +
								"where arcondicionado.sala = :#${body.sala} " +
								"?outputType=SelectOne&outputClass=com.cristborges.experimento_camel_tcc.ambiente_iot.ComandoArCondicionado"
						)
					.otherwise()
						.to(
							"sql:select " +
								"arcondicionado.sala as sala, " +
								"arcondicionado.comandodesligar as comando_array " +
								"from equipamentos.arcondicionado " +
								"where arcondicionado.sala = :#${body.sala} " +
								"?outputType=SelectOne&outputClass=com.cristborges.experimento_camel_tcc.ambiente_iot.ComandoArCondicionado"
						)
					.end()
						.marshal().json(JsonLibrary.Gson, ComandoArCondicionado.class)
						.to("activemq:topic:Classroom.AirConditioner.IRCommand");
			}
		};
	}
}
