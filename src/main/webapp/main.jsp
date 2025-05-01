<%@page import="Vo.MemberVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="center" value="${requestScope.center}" />
<c:if test="${empty center}">
	<c:set var="center" value="center.jsp" />
</c:if>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>도서관 메인</title>
<link href="${contextPath}/css/common.css" rel="stylesheet">
<link href="${contextPath}/css/head.css" rel="stylesheet">
<link href="${contextPath}/css/index.css" rel="stylesheet">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script src="${contextPath}/js/common.js"></script>
</head>
<body>
	<header id="header">
		<jsp:include page="head.jsp" />
	</header>
	<div id="wrapper">
		<div class="container">
			<jsp:include page="${center}" />
		</div>
	</div>
	<footer id="footer">
		<jsp:include page="tail.jsp" />
	</footer>
</body>
</html>