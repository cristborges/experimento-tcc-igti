<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse" 
			data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="<c:url value="/home" />">Experimento Camel TCC</a>
	</div>
	<div class="navbar-collapse collapse">
		<ul class="nav navbar-nav">
			<li class="dropdown active">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">Matrículas <b class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href='<c:url value="/pesquisaMatriculas" />'>Pesquisa</a>
					<li><a href='<c:url value="/cadastroMatriculas" />'>Cadastro</a></li>
				</ul>
			</li>					
		</ul>
	</div>
</div>
