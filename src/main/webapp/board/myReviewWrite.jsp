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
	<style type="text/css">
		.Non-member-form{
			margin-top:10px;
			text-align: center;
		}
		.login-btn{
			margin: 10px;
			display: inline-block;
			padding: 6px 14px;
    		font-size: 14px;
  			border-radius: 4px;
    		background-color: #003c83;
    		color: white;
    		text-decoration: none;
		}
	</style>
</head>
<body>
	<c:if test="${not empty sessionScope.id}">
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
	</c:if>
	<c:if test="${empty sessionScope.id}">
		<div class="Non-member-form">
			<span>💡 서평은 회원만 작성할 수 있습니다. 로그인 하시면 서평을 남길 수 있어요!</span>
    		<span class="login-btn"><a href="${contextPath}/member/login">로그인 하러 가기</a></span>
		</div>
	</c:if>
	
</body>
</html>
