<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:url var="salvar" value="/salvarMatriculasAluno" />
<c:url var="consultarAlunoByCpf" value="/consultarAlunoByCpf" scope="request" />

<script type="text/javascript">
	$(function() {
		var index = $("#matriculasSection").children().length;

		$('.cpf').mask('000.000.000-00');

		$('.cpf').off('blur').on('blur', function() {
			updateAlunoByCpf(false);
		});

		$('#adicionarMatricula').off('click').on('click', function() {
			$('#matriculasSection').append(
				'<div class="row" id="matriculas' + index + '-wrapper">'
					+ '<hr class="visible-xs"/>'
					+ '<div class="col-sm-8">'
						+ '<div class="form-group">'
							+ '<label for="matriculas' + index + '.curso">'
								+ 'Curso <span class="text-danger">*</span>'
							+ '</label>'
							+ $('#matriculas0\\.curso').prop('outerHTML')
								.replace('id="matriculas0.curso"', 'id="matriculas' + index + '.curso"')
								.replace('name="matriculas[0].curso"', 'name="matriculas[' + index + '].curso"')
								.replace('selected="selected"', '')
								.replace('checked', '')
						+ '</div>'
					+ '</div>'
					+ '<div class="col-sm-3">'
						+ '<div class="form-group">'
							+ '<label for="matriculas' + index + '.turmaReduzida" class="hidden-xs">&nbsp;</label>'
							+ '<div class="checkbox">'
								+ '<label>'
									+ '<input id="matriculas' + index + '.turmaReduzida" name="matriculas[' + index + '].turmaReduzida" type="checkbox"> Turma reduzida'
								+ '</label>'
							+ '</div>'
						+ '</div>'
					+ '</div>'
					+ '<div class="col-sm-1">'
						+ '<div class="form-group hidden-xs">'
							+ '<label>'
								+ '&nbsp;'
							+ '</label>'
							+ '<div>'
								+ '<a class="btn btn-default btn-excluir-matricula" href="#" role="button" data-index="' + index + '"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'
							+ '</div>'
						+ '</div>'
						+ '<a class="btn btn-default btn-excluir-matricula visible-xs" href="#" role="button" data-index="' + index + '">Remover</a>'
					+ '</div>'
				+ '</div>'
			);

			index++;

			updateListenersToRemoveEvent();
		});

		init();

		function init() {
			updateListenersToRemoveEvent();
			updateAlunoByCpf(true);
		}

		function updateListenersToRemoveEvent() {
			$('.btn-excluir-matricula').off('click').on('click', function() {
				var indexToRemove = $(this).data('index');

				$('#matriculas' + indexToRemove + '-wrapper').remove();

				for (var i = indexToRemove; i < $("#matriculasSection").children().length; i++) {
					$('#matriculas' + (i + 1) + '-wrapper')
						.attr('id', '#matriculas' + i + '-wrapper');

					$('label[for="matriculas' + (i + 1) + '\\.curso"]')
						.attr('for', 'matriculas' + i + '.curso');
					$('#matriculas' + (i + 1) + '\\.curso')
						.attr('id', 'matriculas' + i + '\.curso')
						.attr('name', 'matriculas[' + i + '].curso');

					$('label[for="matriculas' + (i + 1) + '\\.turmaReduzida"]')
						.attr('for', 'matriculas' + i + '.turmaReduzida');
					$('#matriculas' + (i + 1) + '\\.turmaReduzida')
						.attr('id', 'matriculas' + i + '.turmaReduzida')
						.attr('name', 'matriculas[' + i + '].turmaReduzida');

					$('.btn-excluir-matricula:not(.visible-xs)[data-index="' + (i + 1) + '"]')
						.attr('data-index', i);
					$('.btn-excluir-matricula.visible-xs[data-index="' + (i + 1) + '"]')
						.attr('data-index', i);
				}

				index--;
			});
		}

		function updateAlunoByCpf(onlyId) {
			var cpf = $("#aluno\\.cpf").val();
			$('#aluno\\.id').val('');
	
			if (cpf && cpf.length === 14) {
				$.ajax({
					type: 'POST',
					contentType: 'application/json',
					url: '${consultarAlunoByCpf}',
					data: cpf,
					dataType: 'json',
					timeout: 100000,
					success: function(aluno) {
						$('#aluno\\.id').val(aluno.id);

						if (!onlyId) {
							$('#aluno\\.nome').val(aluno.nome);
							$('#aluno\\.sobrenome').val(aluno.sobrenome);
							$('#aluno\\.email').val(aluno.email);
						}
					}
				});
			}
		}
	});
</script>

<div class="starter-template container">
	<div class="row">
		<h1>Cadastro de Matrículas</h1>
	</div>
	<form:form action="${salvar}" modelAttribute="matriculasAlunoForm">
		<div class="erro-cadastro-matriculas">
			<form:errors element="div" path="aluno.cpf" cssClass="alert alert-warning alert-dismissible" role="alert" htmlEscape="false" />
			<form:errors element="div" path="aluno.nome" cssClass="alert alert-warning alert-dismissible" role="alert" htmlEscape="false" />
			<form:errors element="div" path="aluno.sobrenome" cssClass="alert alert-warning alert-dismissible" role="alert" htmlEscape="false" />
			<form:errors element="div" path="aluno.email" cssClass="alert alert-warning alert-dismissible" role="alert" htmlEscape="false" />
			<c:set var="cursoErrors"/>
			<c:forEach items="${matriculasAlunoForm.matriculas}" varStatus="loop">
				<c:if test="${empty cursoErrors}">
					<c:set var="cursoErrors"><form:errors element="div" path="matriculas[${loop.index}].curso" cssClass="alert alert-warning alert-dismissible" role="alert" htmlEscape="false" /></c:set>
					<c:out value="${cursoErrors}" escapeXml="false" />
				</c:if>
			</c:forEach>
		</div>
		<div class="row">
			<h3>Aluno</h3>
		</div>
		<input type="hidden" id="aluno.id" name="aluno.id" />
		<div class="row">
			<div class="col-sm-3">
				<div class="form-group">
					<form:label path="aluno.cpf">
						<spring:message text="CPF" /> <span class="text-danger">*</span>
					</form:label>
					<form:input path="aluno.cpf" class="form-control cpf" />
				</div>
			</div>
			<div class="col-sm-3">
				<div class="form-group">
					<form:label path="aluno.nome">	
						<spring:message text="Nome" /> <span class="text-danger">*</span>
					</form:label>
					<form:input path="aluno.nome" class="form-control" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<form:label path="aluno.sobrenome">
						<spring:message text="Sobrenome" /> <span class="text-danger">*</span>
					</form:label>
					<form:input path="aluno.sobrenome" class="form-control" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<form:label path="aluno.email">
						<spring:message text="E-mail" /> <span class="text-danger">*</span>
					</form:label>
					<form:input path="aluno.email" class="form-control" type="email" />
				</div>
			</div>
		</div>

		<div class="row">
			<h3 class="pull-left">Matrículas &nbsp;</h3>
			<h3 class="pull-left">
				<button id="adicionarMatricula" type="button" class="btn btn-success btn-sm">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					Adicionar
				</button>
			</h3>
		</div>

		<div id="matriculasSection">
			<c:forEach items="${matriculasAlunoForm.matriculas}" varStatus="loop">
				<div class="row" id="matriculas${loop.index}-wrapper">
					<hr class="visible-xs" />
					<div class="col-sm-8">
						<div class="form-group">
							<label for="matriculas${loop.index}.curso">Curso</label> <span class="text-danger">*</span>
							<form:select class="form-control" path="matriculas[${loop.index}].curso">
								<form:option value="">&nbsp;</form:option>
			                	<form:options itemValue="codigo" itemLabel="nome" items="${cursos}" />
			           	     </form:select>
			            </div>
	                </div>
					<div class="col-sm-3">
						<div class="form-group">
							<label for="matriculas${loop.index}.turmaReduzida" class="hidden-xs">&nbsp;</label>
							<div class="checkbox">
					    		<label>
					                <input id="matriculas${loop.index}.turmaReduzida" name="matriculas[${loop.index}].turmaReduzida" type="checkbox" <c:if test="${matriculasAlunoForm.matriculas[loop.index].turmaReduzida}">checked</c:if>> Turma reduzida
					    		</label>
					  		</div>
			            </div>
					</div>
					<c:if test="${loop.index > 0}">
						<div class="col-sm-1">
							<div class="form-group hidden-xs">
								<label>
									&nbsp;
								</label>
								<div>
									<a class="btn btn-default btn-excluir-matricula" href="#" role="button" data-index="${loop.index}"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
								</div>
							</div>
							<a class="btn btn-default btn-excluir-matricula visible-xs" href="#" role="button" data-index="${loop.index}">Remover</a>
						</div>
					</c:if>
				</div>
			</c:forEach>
		</div>

		<hr />

		<div id="footer" class="row pull-right">
			<div class="col-sm-12 hidden-xs">
				<input class="btn btn-primary" type="submit" value="<spring:message text="Salvar"/>" />
				<a class="btn btn-default" href="<c:url value="/pesquisaMatriculas" />" role="button"> <spring:message text="Cancelar" /> </a>
			</div>
		</div>
		<input class="btn btn-primary btn-block visible-xs" type="submit" value="<spring:message text="Salvar"/>" />
		<a class="btn btn-default btn-block visible-xs" href="<c:url value="/pesquisaMatriculas" />" role="button"><spring:message text="Cancelar" /></a>
	</form:form>
</div>
