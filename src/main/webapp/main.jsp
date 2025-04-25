<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="center" value="${requestScope.center}" />
<c:if test="${empty center}">
	<c:set var="center" value="center.jsp" />
</c:if>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>도서관 메인</title>
<link href="../css/common.css" rel="stylesheet">
<script src="../js/common.js"></script>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
	<header id="header">
		<jsp:include page="head.jsp" />
	</header>
	<div id="wrapper">
		<jsp:include page="${center}" />
	</div>
	<header id="tail">
		<jsp:include page="tail.jsp" />
	</header>
</body>
</html>