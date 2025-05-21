<%@ page import="Vo.MemberVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.security.SecureRandom"%>
<%@ page import="java.math.BigInteger"%>
<%@ page import="java.sql.Date"%>

<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
MemberVo memberVo = (MemberVo) request.getAttribute("memberVo");
String memberId = (String) request.getAttribute("id");
String message = (String) request.getAttribute("message");
%>

<div class="container">
	<div class="form-wrap find-idpw">
		<div class="inner">
			<h3>회원님의 아이디는 다음과 같습니다.</h3>
			<p class="find-id"><%=memberId%></p>
			<p>비밀번호를 잃어버리셨나요? <a href="<%=contextPath%>/member/forgotPwForm">비밀번호 찾기</a><p>
		</div>
	</div>
</div>