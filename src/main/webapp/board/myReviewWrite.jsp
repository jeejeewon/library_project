<%@page import="Vo.boardVO"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%
request.setCharacterEncoding("UTF-8");
%>

<html>
<head>
	<title>문의글 글쓰기 - questionWrite.jsp</title>
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
	<form name="WriteForm" action="${contextPath}/bbs/myReviewWrite.do" method="post">
		<!-- bookNo전달 -->
		<input type="hidden" name="bookNo" value="${param.bookNo}">
		
		<!-- 제목 입력 -->
		<div>
			<label for="title">제목:</label>
       		<input type="text" placeholder="제목을 입력하세요." id="title" name="title" required> <%-- required는 필수 입력 필드로 만드는 HTML5 속성 --%>
		</div>
		<!-- 내용 입력 -->
		<div>
			<label for="content">내용:</label>
			<textarea name="content" placeholder="내용을 입력하세요."  rows="5" required></textarea>
		</div>
		
		<%-- 서평 등록 버튼 --%>
    	<div>
        	<button type="submit">서평 등록</button>
    	</div>
	</form>
</body>
</html>
