<%@page import="Vo.MemberVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
String id = (String) session.getAttribute("id"); // 세션에서 사용자 ID를 가져옴

if (id == null) { // 로그인하지 않은 경우
%>
<script>
	alert("로그인을 하셔야 접근 가능 합니다."); // 알림 창 표시
	history.back(); // 이전 페이지로 이동
</script>
<%
}
%>

<div id="myPageMain" class="mypage">
	<jsp:include page="mypageMenu.jsp" />
	<div class="page">
		<div class="inner">		
		마이페이지 기본 메인
		</div>
	</div>
</div>