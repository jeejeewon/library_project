<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="center" value="${requestScope.center}" />
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
String id = (String) session.getAttribute("id");
System.out.println(id);
if (id == null || !id.equals("admin")) {
%>
<script>
	alert("접근 권한이 없습니다.");
	history.back();
</script>
<%
}
%>

<!DOCTYPE html>
<html class="adm-page">
<head>
<meta charset="UTF-8">
<title>관리자 메인</title>
<link href="<%=contextPath%>/css/common.css" rel="stylesheet" />
<link href="<%=contextPath%>/css/head.css" rel="stylesheet" />
<link href="<%=contextPath%>/css/index.css" rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" />
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=contextPath%>/js/common.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
</head>
<body>
	<header id="header">
		<div class="inner">
			<a href="<%=contextPath%>/admin/main"><h3>관리자 메인</h3></a>
			<div class="btn-wrap">
				<a href="<%=contextPath%>/member/logout.me" class="item">로그아웃</a> <a
					href="<%=contextPath%>/view/main">홈으로</a>
			</div>
		</div>

	</header>
	<div id="wrapper">
		<div class="adm-side-menu">
			<ul>
				<li><a href="<%=contextPath%>/admin/memberSearch">회원 관리</a></li>
				<li><a href="<%=contextPath%>/books/adminBook.do" targer="_blank">도서 관리</a></li>
				<li><a>시설 관리</a></li>
				<li><a href="<%=contextPath%>/bbs/noticeList.do" targer="_blank">게시판 관리</a></li>
			</ul>
		</div>
		<div class="content">
			<jsp:include page="${center}" />
		</div>
	</div>
</body>
</html>