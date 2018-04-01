<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:url var="consultarMatricula" value="/consultarMatricula" scope="request" />

<script type="text/javascript">
	$(function() {
		$('[data-target="#modalDetalhesMatricula"]').off('click').on('click', function(id) {
			$.ajax({
				type: 'GET',
				contentType: 'application/json',
				url: '${consultarMatricula}/' + $(this).data('id'),
				dataType: 'json',
				timeout: 100000,
				success: function(matricula) {
					$('#matricula\\.numeroMatricula').text(matricula.numeroMatricula);
					$('#matricula\\.aluno').text(matricula.aluno.cpf + ' - ' + matricula.aluno.nome + ' ' + matricula.aluno.sobrenome + '\n' + matricula.aluno.email);
					$('#matricula\\.curso').text(matricula.curso.codigo + ' - ' +  matricula.curso.nome + ' (' + matricula.curso.cargaHoraria + 'h)' + ' - ' + matricula.curso.periodo);
					$('#matricula\\.data').text(moment(matricula.data).format("DD/MM/YYYY HH:mm:ss"));
					$('#matricula\\.turmaReduzida').text(matricula.turmaReduzida ? 'Sim' : 'Não');
					$('#matricula\\.status').text(matricula.status);
				}
			});
		});

		var labelTelaPesquisaElement = $('#labelTelaPesquisa');
		if (labelTelaPesquisaElement) {
			labelTelaPesquisaElement.popover({
				title: 'Cadastro efetuado!',
				content: '${msg}', 
				trigger: 'manual', 
				placement: 'left'
			});
			labelTelaPesquisaElement.popover('show');

			setTimeout(function(){
				labelTelaPesquisaElement.popover('hide');
			}, 5000);	
		}
	});
</script>

<div class="starter-template">
	<div class="row">
		<div class="col-sm-12">
			<br/>
			<h1 class="pull-left">Lista de Matrículas</h1>
			<h1 class="pull-right padding">
				<a class="btn btn-primary" href="<c:url value="/cadastroMatriculas" />" role="button"> Cadastrar </a>
			</h1>
			<c:if test="${not empty msg}">
				<h1 id="labelTelaPesquisa" class="pull-right">&nbsp;&nbsp;</h1>
			</c:if>
		</div>
	</div>

	<hr />

	<div class="table-responsive">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Número</th>
					<th>Aluno</th>
					<th>Curso</th>
					<th>Data</th>
					<th>Status</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${matriculas}" var="matricula">
					<tr>
						<td>${matricula.numeroMatricula}</td>
						<td>${matricula.aluno.nome} ${matricula.aluno.sobrenome}</td>
						<td>${matricula.curso.nome}</td>
						<td><fmt:formatDate value="${matricula.data}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
						<td>${matricula.status}</td>
						<td><a href="#" data-toggle="modal" data-target="#modalDetalhesMatricula" data-id="${matricula.id}"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div class="modal fade" id="modalDetalhesMatricula" tabindex="-1" role="dialog" aria-labelledby="modalDetalhesMatriculaLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="modalDetalhesMatriculaLabel">Detalhes da Matrícula</h4>
				</div>
				<div class="modal-body">
					<dl class="dl-horizontal">
					  <dt>Número</dt>
					  <dd id="matricula.numeroMatricula"></dd>
					  <dt>Aluno</dt>
					  <dd id="matricula.aluno"></dd>
					  <dt>Curso</dt>
					  <dd id="matricula.curso"></dd>
					  <dt>Data</dt>
					  <dd id="matricula.data"></dd>
					  <dt>Turma reduzida</dt>
					  <dd id="matricula.turmaReduzida"></dd>
					  <dt>Status</dt>
					  <dd id="matricula.status"></dd>
					</dl>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
				</div>
			</div>
		</div>
	</div>
</div>
