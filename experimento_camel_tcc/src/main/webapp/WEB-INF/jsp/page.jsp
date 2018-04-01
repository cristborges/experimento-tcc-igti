<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html>
<html>
	<head lang="pt-br">
	<title><tiles:insertAttribute name="title" defaultValue="Experimento Camel TCC" /></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.css" />">
	<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap-theme.css" />">
	<link rel="stylesheet" href="<c:url value="/resources/css/app.css" />">
    <script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />"></script>
    <script src="<c:url value="/resources/js/jquery-ui-1.10.4.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap.js" />"></script>
    <script src="<c:url value="/resources/js/underscore-min.js" />"></script>
    <script src="<c:url value="/resources/js/jquery.mask.min.js" />"></script>
    <script src="<c:url value="/resources/js/moment.min.js" />"></script>
</head>
<body>
	<div id="wrap">
		<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			<tiles:insertAttribute name="header" />
		</div>
		<div class="container">
			<tiles:insertAttribute name="content" />
		</div>
		<div id="push"></div>
	</div>
</body>
</html>